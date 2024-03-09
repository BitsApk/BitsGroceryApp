package com.bitspan.bitsjobkaro.di

import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.services.*
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
    @Named("jobKaro")
    fun provideBaseUrl() = Constant.BASE_URL

    @Provides
    @Named("other")
    fun provideBaseUrlOther() = Constant.BASE_URL_OTHER

    @Provides
    @Named("auth")
    fun provideBaseUrlAuth() = Constant.BASE_URL_AUTH

    @Provides
    @Named("RecNew")
    fun provideRecNewUrl() = Constant.BASE_URL_NEW_REC


    @Provides
    @Singleton
    @Named("jobKaro")
    fun provideRetrofit(@Named("jobKaro") baseUrl: String): Retrofit {
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
    @Named("other")
    fun provideOtherRetrofit(@Named("other") baseUrl: String): Retrofit {
        val credentials = Credentials.basic("hiring", "897742dc840bcbf1278501480f2f6e79") // Replace with your actual username and password
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
    @Named("RecNew")
    fun provideRecNewRetrofit(@Named("RecNew") baseUrl: String): Retrofit {
        val credentials = Credentials.basic(Constant.AAU, Constant.AAP) // Replace with your actual username and password
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
    fun provideAuthRetrofit(@Named("auth") baseUrl: String): Retrofit {
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
    fun provideOtherApiService(@Named("other") retrofit: Retrofit): OtherApiServices =
        retrofit.create(OtherApiServices::class.java)


    @Provides
    @Singleton
    fun provideRecNewApiService(@Named("RecNew") retrofit: Retrofit): RecruiterNewApiService =
        retrofit.create(RecruiterNewApiService::class.java)


    @Provides
    @Singleton
    fun provideAuthApiService(@Named("auth") retrofit: Retrofit): AuthApiServices =
        retrofit.create(AuthApiServices::class.java)

    @Provides
    @Singleton
    fun provideReferenceApiService(@Named("jobKaro") retrofit: Retrofit): ReferenceApiService =
        retrofit.create(ReferenceApiService::class.java)

    @Provides
    @Singleton
    fun provideRecruiterApiService(@Named("jobKaro") retrofit: Retrofit): RecruiterApiService =
        retrofit.create(RecruiterApiService::class.java)

    @Provides
    @Singleton
    fun provideEmpProffApiService(@Named("jobKaro") retrofit: Retrofit): EmpProffService =
        retrofit.create(EmpProffService::class.java)


    @Provides
    @Singleton
    fun provideEmpLoginApiService(@Named("jobKaro") retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideRecruitManageApiService(@Named("jobKaro") retrofit: Retrofit): RecruitManageApiService =
        retrofit.create(RecruitManageApiService::class.java)

    @Provides
    @Singleton
    fun provideChatApiService(@Named("jobKaro") retrofit: Retrofit): ChatApiService =
        retrofit.create(ChatApiService::class.java)

}