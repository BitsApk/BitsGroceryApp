package com.bitspanindia.groceryapp.data.repositories

import com.bitspanindia.groceryapp.data.model.request.LoginBody
import com.bitspanindia.groceryapp.data.model.request.LoginMobReq
import com.bitspanindia.groceryapp.data.model.request.OtpVerifyReq
import com.bitspanindia.groceryapp.data.model.request.RegisterApiReq
import com.bitspanindia.groceryapp.data.model.response.LoginMobResp
import com.bitspanindia.groceryapp.data.model.response.LoginPassResponse
import com.bitspanindia.groceryapp.data.model.response.OtpVerifyResp
import com.bitspanindia.groceryapp.data.model.response.RegisterApiResponse
import com.bitspanindia.groceryapp.data.services.LoginApiService
import retrofit2.Response
import javax.inject.Inject


class LoginRepository @Inject constructor(private val loginApiService: LoginApiService) {


    suspend fun doPassLogin(loginBody: LoginBody): Response<LoginPassResponse> {
        return loginApiService.doPassLogin(loginBody)
    }

    suspend fun doMobLogin(loginMobReq: LoginMobReq): Response<LoginMobResp> {
        return loginApiService.doMobLogin(loginMobReq)
    }

    suspend fun doRegistration(registerApiReq: RegisterApiReq): Response<RegisterApiResponse> {
        return loginApiService.doRegistration(registerApiReq)
    }

    suspend fun doOtpVerify(otpVerifyReq: OtpVerifyReq): Response<OtpVerifyResp> {
        return loginApiService.doOtpVerify(otpVerifyReq)
    }

}