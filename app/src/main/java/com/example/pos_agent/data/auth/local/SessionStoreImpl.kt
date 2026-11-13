package com.example.pos_agent.data.auth.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.model.AuthToken
import com.example.pos_agent.domain.auth.model.User
import com.example.pos_agent.domain.auth.port.SessionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

private val Context.authDataStore by preferencesDataStore(name = "auth_session")

/**
 * Stores the session in DataStore, encrypting tokens with [TokenCipher].
 *
 * A corrupt or undecryptable store is treated as "no session" and cleared so
 * the user can always sign in again.
 */
class SessionStoreImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val tokenCipher: TokenCipher,
) : SessionStore {

    override suspend fun read(): Response<AuthSession?> = withContext(Dispatchers.IO) {
        try {
            val preferences = context.authDataStore.data
                .catch { cause ->
                    if (cause is IOException) emit(emptyPreferences()) else throw cause
                }
                .first()

            val encryptedAccessToken = preferences[Keys.ACCESS_TOKEN]
                ?: return@withContext Response.Success(null)
            val userId = preferences[Keys.USER_ID]
                ?: return@withContext Response.Success(null)
            val username = preferences[Keys.USERNAME]
                ?: return@withContext Response.Success(null)

            Response.Success(
                AuthSession(
                    user = User(
                        id = userId,
                        username = username,
                        displayName = preferences[Keys.DISPLAY_NAME] ?: username,
                        role = preferences[Keys.ROLE] ?: DEFAULT_ROLE,
                    ),
                    accessToken = AuthToken(tokenCipher.decrypt(encryptedAccessToken)),
                    refreshToken = preferences[Keys.REFRESH_TOKEN]
                        ?.takeIf(String::isNotBlank)
                        ?.let { AuthToken(tokenCipher.decrypt(it)) },
                    expiresAtEpochMillis = preferences[Keys.EXPIRES_AT] ?: 0L,
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            clearQuietly()
            Response.Success(null)
        }
    }

    override suspend fun write(session: AuthSession): Response<Unit> = withContext(Dispatchers.IO) {
        try {
            val encryptedAccessToken = tokenCipher.encrypt(session.accessToken.raw)
            val encryptedRefreshToken = session.refreshToken?.let { tokenCipher.encrypt(it.raw) }

            context.authDataStore.edit { preferences ->
                preferences[Keys.ACCESS_TOKEN] = encryptedAccessToken
                preferences[Keys.USER_ID] = session.user.id
                preferences[Keys.USERNAME] = session.user.username
                preferences[Keys.DISPLAY_NAME] = session.user.displayName
                preferences[Keys.ROLE] = session.user.role
                preferences[Keys.EXPIRES_AT] = session.expiresAtEpochMillis
                if (encryptedRefreshToken != null) {
                    preferences[Keys.REFRESH_TOKEN] = encryptedRefreshToken
                } else {
                    preferences.remove(Keys.REFRESH_TOKEN)
                }
            }
            Response.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Failure(DomainError.Unknown("Could not save the sign-in session.", e))
        }
    }

    override suspend fun clear(): Response<Unit> = withContext(Dispatchers.IO) {
        try {
            context.authDataStore.edit { it.clear() }
            Response.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Failure(DomainError.Unknown("Could not clear the sign-in session.", e))
        }
    }

    private suspend fun clearQuietly() {
        try {
            context.authDataStore.edit { it.clear() }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // A failed cleanup must not block the user from signing in again.
        }
    }

    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val DISPLAY_NAME = stringPreferencesKey("display_name")
        val ROLE = stringPreferencesKey("role")
        val EXPIRES_AT = longPreferencesKey("expires_at")
    }

    private companion object {
        const val DEFAULT_ROLE = "OPERATOR"
    }
}
