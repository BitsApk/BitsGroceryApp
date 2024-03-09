package com.bitspan.bitsjobkaro.di

import android.content.Context
import com.bitspan.bitsjobkaro.storage.DataStoreManager
import com.bitspan.bitsjobkaro.storage.DataStoreManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn (SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext appContext : Context): DataStoreManager {
        return DataStoreManagerImpl(appContext)
    }
}