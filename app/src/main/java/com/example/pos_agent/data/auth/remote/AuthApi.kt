package com.example.pos_agent.data.auth.remote

import com.example.pos_agent.data.auth.remote.dto.LoginRequestDto
import com.example.pos_agent.data.auth.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Auth service contract.
 *
 * `POST {AUTH_BASE_URL}auth/login`
 * ```
 * { "username": "...", "password": "..." }
 * ```
 * A successful response is [LoginResponseDto]. Invalid credentials are
 * expected to return `401`; locked accounts `423`; throttling `429`.
 */
interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
}
