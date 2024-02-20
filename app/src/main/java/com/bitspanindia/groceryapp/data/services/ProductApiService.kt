package com.bitspanindia.groceryapp.data.services

import com.bitspanindia.groceryapp.data.model.HomeDataX
import com.bitspanindia.groceryapp.data.model.SearchProductResponse
import com.bitspanindia.groceryapp.data.model.SubCatProductsData
import com.bitspanindia.groceryapp.data.model.SubCategoryData
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ProductApiService {

    @POST("homepage_dynamic")
    suspend fun getHomeData(
        @Body homeReq: HomeDataReq
    ): Response<HomeDataX>



}