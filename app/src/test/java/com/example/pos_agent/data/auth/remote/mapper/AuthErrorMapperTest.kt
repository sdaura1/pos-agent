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
    fun `401 maps to invalid credentials`() {
        assertEquals(
            AuthErrorCode.INVALID_CREDENTIALS,
            AuthErrorMapper.fromHttp(httpException(401)).code,
        )
    }

    @Test
    fun `423 maps to account locked`() {
        assertEquals(
            AuthErrorCode.ACCOUNT_LOCKED,
            AuthErrorMapper.fromHttp(httpException(423)).code,
        )
    }

    @Test
    fun `429 maps to too many attempts`() {
        assertEquals(
            AuthErrorCode.TOO_MANY_ATTEMPTS,
            AuthErrorMapper.fromHttp(httpException(429)).code,
        )
    }

    @Test
    fun `other statuses map to server error`() {
        assertEquals(
            AuthErrorCode.SERVER,
            AuthErrorMapper.fromHttp(httpException(500)).code,
        )
    }

    private fun httpException(status: Int): HttpException = HttpException(
        Response.error<Unit>(
            status,
            "".toResponseBody("application/json".toMediaType()),
        )
    )
}
