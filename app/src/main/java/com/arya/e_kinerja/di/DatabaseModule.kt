package com.arya.e_kinerja.di

import android.content.Context
import com.arya.e_kinerja.data.local.datastore.SessionDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): SessionDataStore = SessionDataStore(context)
}