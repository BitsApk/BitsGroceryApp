package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.AuthReq
import com.bitspan.bitsjobkaro.data.models.SendOtpReq
import com.bitspan.bitsjobkaro.data.models.chat.EmpDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface AuthApiServices {

    @POST("get_auth")
    suspend fun getAuth(
        @Body authReq: AuthReq
    ): Response<String>


}