package com.bitspanindia.groceryapp.data.services

import com.bitspanindia.groceryapp.data.model.HomeDataX
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface HomeApiService {

    @POST("homepage_dynamic")
    suspend fun getHomeData(
        @Body homeReq: HomeDataReq
    ): Response<HomeDataX>


}