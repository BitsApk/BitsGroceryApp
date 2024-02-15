package com.bitspanindia.groceryapp.data.repositories

import com.bitspanindia.groceryapp.data.model.HomeDataX
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.services.HomeApiService
import retrofit2.Response
import javax.inject.Inject


class HomeRepository @Inject constructor(private val homeApiService: HomeApiService) {


    suspend fun getHomeData(homeDataReq: HomeDataReq): Response<HomeDataX> {
        return homeApiService.getHomeData(homeDataReq)
    }



}