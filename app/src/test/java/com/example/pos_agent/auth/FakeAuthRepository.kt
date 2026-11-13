package com.example.pos_agent.auth

import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.model.AuthToken
import com.example.pos_agent.domain.auth.model.LoginCredentials
import com.example.pos_agent.domain.auth.model.User
import com.example.pos_agent.domain.auth.port.AuthRepository

class FakeAuthRepository(
    var loginResult: Response<AuthSession> = Response.Success(session()),
    var restoreResult: Response<AuthSession?> = Response.Success(null),
) : AuthRepository {
    var lastCredentials: LoginCredentials? = null
    var logoutCalled = false

    override suspend fun login(credentials: LoginCredentials): Response<AuthSession> {
        lastCredentials = credentials
        return loginResult
    }

    override suspend fun restoreSession(): Response<AuthSession?> = restoreResult

    override suspend fun logout(): Response<Unit> {
        logoutCalled = true
        return Response.Success(Unit)
    }

    companion object {
        fun session(expiresAtEpochMillis: Long = Long.MAX_VALUE): AuthSession = AuthSession(
            user = User(
                id = "user-1",
                username = "operator",
                displayName = "Terminal Operator",
                role = "OPERATOR",
            ),
            accessToken = AuthToken("access-token"),
            refreshToken = AuthToken("refresh-token"),
            expiresAtEpochMillis = expiresAtEpochMillis,
        )
    }
}
