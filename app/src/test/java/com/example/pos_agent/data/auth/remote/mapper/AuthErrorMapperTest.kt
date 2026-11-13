package com.example.pos_agent.data.auth.remote.mapper

import com.example.pos_agent.domain.auth.model.AuthErrorCode
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthErrorMapperTest {

    @Test
    fun `401 should map to invalid credentials when authentication fails`() {
        // Arrange
        val exception = httpException(401)

        // Act
        val error = AuthErrorMapper.fromHttp(exception)

        // Assert
        assertEquals(AuthErrorCode.INVALID_CREDENTIALS, error.code)
    }

    @Test
    fun `423 should map to account locked when the account is locked`() {
        // Arrange
        val exception = httpException(423)

        // Act
        val error = AuthErrorMapper.fromHttp(exception)

        // Assert
        assertEquals(AuthErrorCode.ACCOUNT_LOCKED, error.code)
    }

    @Test
    fun `429 should map to too many attempts when the host rate limits`() {
        // Arrange
        val exception = httpException(429)

        // Act
        val error = AuthErrorMapper.fromHttp(exception)

        // Assert
        assertEquals(AuthErrorCode.TOO_MANY_ATTEMPTS, error.code)
    }

    @Test
    fun `other statuses should map to server error when the host fails`() {
        // Arrange
        val exception = httpException(500)

        // Act
        val error = AuthErrorMapper.fromHttp(exception)

        // Assert
        assertEquals(AuthErrorCode.SERVER, error.code)
    }

    private fun httpException(status: Int): HttpException = HttpException(
        Response.error<Unit>(
            status,
            "".toResponseBody("application/json".toMediaType()),
        )
    )
}
