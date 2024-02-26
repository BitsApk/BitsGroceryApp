package com.bitspanindia.groceryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.response.GetProductDetailsResponse
import com.bitspanindia.groceryapp.data.model.response.MultiImg
import com.bitspanindia.groceryapp.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    var prodImageList = listOf<MultiImg>()

    suspend fun getProductDetails(commonDataReq: CommonDataReq): Response<GetProductDetailsResponse> {
        return productRepository.getProductDetails(commonDataReq)
    }

}