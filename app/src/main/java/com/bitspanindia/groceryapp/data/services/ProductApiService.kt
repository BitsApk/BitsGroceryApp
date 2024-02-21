package com.bitspanindia.groceryapp.data.services

import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.response.GetProductDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
interface ProductApiService {
    @POST("product-page")
    suspend fun getProductDetails(
        @Body commonDataReq: CommonDataReq
    ): Response<GetProductDetailsResponse>

}