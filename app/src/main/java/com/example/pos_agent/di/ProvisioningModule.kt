package com.example.pos_agent.di

import com.example.pos_agent.data.device.provisioning.UnavailableTerminalProvisioning
import com.example.pos_agent.domain.terminal.port.TerminalProvisioning
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provisioning wiring.
 *
 * Until the Kozen key-sendAndReceive adapter lands (S4a-4), the app binds a
 * placeholder that reports the terminal as unavailable, so a purchase can
 * never start unprovisioned. Tests and emulator demos inject
 * `FakeTerminalProvisioning` directly.
 */
@Module
@InstallIn(SingletonComponent::class)
object ProvisioningModule {

    @Provides
    @Singleton
    fun provideTerminalProvisioning(): TerminalProvisioning =
        UnavailableTerminalProvisioning()
}
