package com.bitspanindia.groceryapp.data.services

import com.bitspanindia.groceryapp.data.model.response.ApiAuthResponse
import retrofit2.Response
import retrofit2.http.POST


interface AuthApiServices {

    @POST("Apiauth")
    suspend fun getAuth(): Response<ApiAuthResponse>

}