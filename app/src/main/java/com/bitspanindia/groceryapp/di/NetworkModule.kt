package com.bitspanindia.groceryapp.di

import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.services.HomeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideBaseUrl() = Constant.BASE_URL


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        val credentials = Credentials.basic("grossry123", "grossry123") // Replace with your actual username and password
        val authorizationInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestWithAuthorization = originalRequest.newBuilder()
                .header("Authorization", credentials)
                .build()
            chain.proceed(requestWithAuthorization)
        }

        val client = OkHttpClient.Builder()
        client.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        client.addInterceptor(authorizationInterceptor)
        client.connectTimeout(3, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .writeTimeout(3, TimeUnit.MINUTES) // write timeout
            .readTimeout(3, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService = retrofit.create(HomeApiService::class.java)

}