package com.bitspanindia.groceryapp.di

import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.services.AuthApiServices
import com.bitspanindia.groceryapp.data.services.CartApiService
import com.bitspanindia.groceryapp.data.services.HomeApiService
import com.bitspanindia.groceryapp.data.services.ProductApiService
import com.bitspanindia.groceryapp.data.services.ProfileApiService
import com.bitspanindia.groceryapp.data.services.LoginApiService
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
    @Named("auth")
    fun provideAuthRetrofit(baseUrl: String): Retrofit {
        val client = OkHttpClient.Builder()
        client.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
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

    @Provides
    @Singleton
    fun provideCartApiService(retrofit: Retrofit): CartApiService = retrofit.create(CartApiService::class.java)

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService = retrofit.create(LoginApiService::class.java)

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService = retrofit.create(ProductApiService::class.java)

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService = retrofit.create(ProfileApiService::class.java)


    @Provides
    @Singleton
    fun provideAuthApiService(@Named("auth") retrofit: Retrofit): AuthApiServices = retrofit.create(AuthApiServices::class.java)


}