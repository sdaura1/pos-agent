package com.example.pos_agent.di

import com.example.pos_agent.data.device.keystore.InMemorySecretKeyStore
import com.example.pos_agent.data.remote.nibss.iso.hash.MessageHasher
import com.example.pos_agent.data.remote.nibss.iso.hash.UnconfiguredMessageHasher
import com.example.pos_agent.domain.terminal.port.SecretKeyStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Key material wiring.
 *
 * Both bindings are intentionally conservative until the key-storage decision
 * and the hash composition are settled:
 * - keys live in memory and are lost on restart, requiring a fresh sign-on;
 * - hashing fails closed rather than sending a guessed DE128.
 */
@Module
@InstallIn(SingletonComponent::class)
object TerminalSecurityModule {

    @Provides
    @Singleton
    fun provideSecretKeyStore(): SecretKeyStore = InMemorySecretKeyStore()

    @Provides
    @Singleton
    fun provideMessageHasher(): MessageHasher = UnconfiguredMessageHasher()
}
