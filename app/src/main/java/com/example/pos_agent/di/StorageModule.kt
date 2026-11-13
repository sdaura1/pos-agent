package com.example.pos_agent.di

import com.example.pos_agent.data.auth.local.SessionStoreImpl
import com.example.pos_agent.domain.auth.port.SessionStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun bindSessionStore(impl: SessionStoreImpl): SessionStore
}
