package com.bitspanindia.groceryapp.data.repositories

import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.response.GetProductDetailsResponse
import com.bitspanindia.groceryapp.data.services.ProductApiService
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productApiService: ProductApiService) {

    suspend fun getProductDetails(commonDataReq: CommonDataReq): Response<GetProductDetailsResponse> {
        return productApiService.getProductDetails(commonDataReq)
    }

}