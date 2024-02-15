package com.bitspanindia.groceryapp.di

import android.content.Context
import com.bitspanindia.groceryapp.storage.DataStoreManager
import com.bitspanindia.groceryapp.storage.DataStoreManagerImpl
import com.bitspanindia.groceryapp.storage.SharedPreferenceUtil
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


    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext appContext : Context): SharedPreferenceUtil {
        return SharedPreferenceUtil(appContext)
    }
}