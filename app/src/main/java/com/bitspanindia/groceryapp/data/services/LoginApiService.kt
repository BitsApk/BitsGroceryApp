package com.bitspanindia.groceryapp.data.services

import com.bitspanindia.groceryapp.data.model.request.LoginBody
import com.bitspanindia.groceryapp.data.model.request.LoginMobReq
import com.bitspanindia.groceryapp.data.model.request.OtpVerifyReq
import com.bitspanindia.groceryapp.data.model.request.RegisterApiReq
import com.bitspanindia.groceryapp.data.model.response.LoginMobResp
import com.bitspanindia.groceryapp.data.model.response.LoginPassResponse
import com.bitspanindia.groceryapp.data.model.response.OtpVerifyResp
import com.bitspanindia.groceryapp.data.model.response.RegisterApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface LoginApiService {

    @POST("LoginWithPassword")
    suspend fun doPassLogin(
        @Body loginBody: LoginBody
    ): Response<LoginPassResponse>

    @POST("LoginWithOTP")
    suspend fun doMobLogin(
        @Body loginMobReq: LoginMobReq
    ): Response<LoginMobResp>

    @POST("CustomerRegister")
    suspend fun doRegistration(
        @Body registerApiReq: RegisterApiReq
    ): Response<RegisterApiResponse>

    @POST("OTPVerify")
    suspend fun doOtpVerify(
        @Body otpVerifyReq: OtpVerifyReq
    ): Response<OtpVerifyResp>

}