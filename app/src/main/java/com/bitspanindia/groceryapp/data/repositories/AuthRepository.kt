package com.bitspanindia.groceryapp.data.repositories

import com.bitspanindia.groceryapp.data.model.response.ApiAuthResponse
import com.bitspanindia.groceryapp.data.services.AuthApiServices
import retrofit2.Response
import javax.inject.Inject


class AuthRepository @Inject constructor(private val authApiServices: AuthApiServices) {


    suspend fun getAuthDetails(): Response<ApiAuthResponse> {
        return authApiServices.getAuth()
    }



}