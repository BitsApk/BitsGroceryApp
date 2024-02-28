package com.bitspan.bitsjobkaro.data.repositories

import com.bitspan.bitsjobkaro.data.models.AuthReq
import com.bitspan.bitsjobkaro.data.services.AuthApiServices
import retrofit2.Response
import javax.inject.Inject


class AuthRepository @Inject constructor(private val authApiServices: AuthApiServices) {


    suspend fun getAuthDetails(authReq: AuthReq): Response<String> {
        return authApiServices.getAuth(authReq)
    }



}