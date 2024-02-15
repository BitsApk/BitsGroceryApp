package com.bitspanindia.groceryapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bitspanindia.groceryapp.data.model.HomeDataX
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.SearchProductResponse
import com.bitspanindia.groceryapp.data.model.SubCategoryData
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    suspend fun getHomeData(homeDataReq: HomeDataReq): Response<HomeDataX> {
        return homeRepository.getHomeData(homeDataReq)
    }
    suspend fun getSubCatList(commonDataReq: CommonDataReq): Response<SubCategoryData> {
        return homeRepository.getSubCatList(commonDataReq)
    }

    suspend fun searchProduct(commonDataReq: CommonDataReq): Response<SearchProductResponse> {
        return homeRepository.searchProduct(commonDataReq)
    }

    fun getSubCatProducts(productDataReq: ProductDataReq): Flow<PagingData<ProductData>> {
        return homeRepository.getSubCatProducts(productDataReq).cachedIn(viewModelScope)
    }

    fun getSearchProduct(productDataReq: ProductDataReq): Flow<PagingData<ProductData>> {
        return homeRepository.getSearchProduct(productDataReq).cachedIn(viewModelScope)
    }

}