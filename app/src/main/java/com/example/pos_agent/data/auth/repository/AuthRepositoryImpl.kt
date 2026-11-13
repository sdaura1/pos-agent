package com.example.pos_agent.data.auth.repository

import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.core.time.Clock
import com.example.pos_agent.data.auth.remote.AuthApi
import com.example.pos_agent.data.auth.remote.dto.LoginRequestDto
import com.example.pos_agent.data.auth.remote.mapper.AuthErrorMapper
import com.example.pos_agent.data.auth.remote.mapper.AuthMapper
import com.example.pos_agent.domain.auth.model.AuthErrorCode
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.model.LoginCredentials
import com.example.pos_agent.domain.auth.port.AuthRepository
import com.example.pos_agent.domain.auth.port.SessionStore
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

/**
 * Authenticates against the remote auth service and persists the resulting
 * session locally.
 *
 * Transport exceptions are translated into [DomainError] here so nothing
 * Retrofit-specific escapes into the domain.
 */
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val sessionStore: SessionStore,
    private val clock: Clock,
) : AuthRepository {

    override suspend fun login(credentials: LoginCredentials): Response<AuthSession> {
        val payload = try {
            api.login(
                LoginRequestDto(
                    username = credentials.username,
                    password = credentials.password,
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            return Response.Failure(AuthErrorMapper.fromHttp(e))
        } catch (e: IOException) {
            return Response.Failure(
                DomainError.Network(
                    message = "Unable to reach the server",
                    cause = e,
                    code = AuthErrorCode.NETWORK,
                )
            )
        } catch (e: Exception) {
            return Response.Failure(
                DomainError.Unknown(
                    message = "Sign-in failed",
                    cause = e,
                    code = AuthErrorCode.SERVER,
                )
            )
        }

        val session = try {
            AuthMapper.toSession(payload, clock.nowEpochMillis())
        } catch (e: Exception) {
            return Response.Failure(
                DomainError.Unknown(
                    message = "Invalid sign-in response",
                    cause = e,
                    code = AuthErrorCode.INVALID_RESPONSE,
                )
            )
        }

        return when (val saved = sessionStore.write(session)) {
            is Response.Failure -> saved
            is Response.Success -> Response.Success(session)
        }
    }

    override suspend fun restoreSession(): Response<AuthSession?> {
        val stored = when (val result = sessionStore.read()) {
            is Response.Failure -> return result
            is Response.Success -> result.value
        } ?: return Response.Success(null)

        if (stored.isExpired(clock.nowEpochMillis())) {
            // TODO: Rotate the access token with `stored.refreshToken` instead of clearing the session.
            sessionStore.clear()
            return Response.Success(null)
        }
        return Response.Success(stored)
    }

    override suspend fun logout(): Response<Unit> = sessionStore.clear()
}
