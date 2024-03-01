package com.bitspanindia.groceryapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitspanindia.groceryapp.data.model.Cart
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.request.ConfirmOrderReq
import com.bitspanindia.groceryapp.data.model.request.LoginBody
import com.bitspanindia.groceryapp.data.model.request.LoginMobReq
import com.bitspanindia.groceryapp.data.model.request.OtpVerifyReq
import com.bitspanindia.groceryapp.data.model.request.PaymentReq
import com.bitspanindia.groceryapp.data.model.request.PaymentVerifyReq
import com.bitspanindia.groceryapp.data.model.request.RegisterApiReq
import com.bitspanindia.groceryapp.data.model.response.ConfirmOrderResponse
import com.bitspanindia.groceryapp.data.model.response.LoginMobResp
import com.bitspanindia.groceryapp.data.model.response.LoginPassResponse
import com.bitspanindia.groceryapp.data.model.response.OtpVerifyResp
import com.bitspanindia.groceryapp.data.model.response.PaymentResponse
import com.bitspanindia.groceryapp.data.model.response.PaymentVerifyResponse
import com.bitspanindia.groceryapp.data.model.response.RegisterApiResponse
import com.bitspanindia.groceryapp.data.repositories.CartRepository
import com.bitspanindia.groceryapp.data.repositories.LoginRepository
import com.bitspanindia.groceryapp.presentation.CartManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {


    lateinit var registerApiReq: RegisterApiReq
    lateinit var loginBody: LoginBody
    val fcmToken: String = ""

    suspend fun doPassLogin(loginBody: LoginBody): Response<LoginPassResponse> {
        return loginRepository.doPassLogin(loginBody)
    }

    suspend fun doMobLogin(loginMobReq: LoginMobReq): Response<LoginMobResp> {
        return loginRepository.doMobLogin(loginMobReq)
    }

    suspend fun doRegistration(registerApiReq: RegisterApiReq): Response<RegisterApiResponse> {
        return loginRepository.doRegistration(registerApiReq)
    }

    suspend fun doRegistrationOtp(registerApiReq: RegisterApiReq): Response<RegisterApiResponse> {
        return loginRepository.doRegistration(registerApiReq)
    }


    suspend fun doOtpVerify(otpVerifyReq: OtpVerifyReq): Response<OtpVerifyResp> {
        return loginRepository.doOtpVerify(otpVerifyReq)
    }

}