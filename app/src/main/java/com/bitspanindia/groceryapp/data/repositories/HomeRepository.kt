package com.bitspanindia.groceryapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.HomeDataX
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.SubCategoryData
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.pagingSource.ProductPagingSource
import com.bitspanindia.groceryapp.data.services.HomeApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(private val homeApiService: HomeApiService) {
    suspend fun getHomeData(homeDataReq: HomeDataReq): Response<HomeDataX> {
        return homeApiService.getHomeData(homeDataReq)
    }

    suspend fun getSubCatList(commonDataReq: CommonDataReq): Response<SubCategoryData> {
        return homeApiService.getSubCatList(commonDataReq)
    }

    fun getSubCatProducts(productDataReq: ProductDataReq) : Flow<PagingData<ProductData>> {
        return Pager(config = PagingConfig(Constant.PAGE_SIZE, enablePlaceholders = false)) {
            ProductPagingSource(homeApiService, productDataReq,"subCatProduct")
        }.flow
    }

    fun getSearchProduct(productDataReq: ProductDataReq) : Flow<PagingData<ProductData>> {
        return Pager(config = PagingConfig(Constant.PAGE_SIZE, enablePlaceholders = false)) {
            ProductPagingSource(homeApiService, productDataReq,"searchProduct")
        }.flow
    }

}