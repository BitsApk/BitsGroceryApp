package com.bitspanindia.groceryapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.model.request.UpdateProfileReq
import com.bitspanindia.groceryapp.data.model.response.AddressListResponse
import com.bitspanindia.groceryapp.data.model.response.CommonResponse
import com.bitspanindia.groceryapp.data.model.response.GetProductDetailsResponse
import com.bitspanindia.groceryapp.data.model.response.GetProfileResponse
import com.bitspanindia.groceryapp.data.model.response.Order
import com.bitspanindia.groceryapp.data.model.response.OrderDetailsResponse
import com.bitspanindia.groceryapp.data.model.response.OrderListResponse
import com.bitspanindia.groceryapp.data.pagingSource.OrderListPagingSource
import com.bitspanindia.groceryapp.data.pagingSource.ProductPagingSource
import com.bitspanindia.groceryapp.data.services.ProfileApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val profileApiService: ProfileApiService) {
    suspend fun updateProfile(updateProfileReq: UpdateProfileReq): Response<CommonResponse> {
        return profileApiService.updateProfile(updateProfileReq )
    }

    suspend fun getProfileData(homeDataReq: HomeDataReq): Response<GetProfileResponse> {
        return profileApiService.getProfileData(homeDataReq)
    }

    suspend fun getAddressList(homeDataReq: HomeDataReq): Response<AddressListResponse> {
        return profileApiService.getAddressList(homeDataReq)
    }

    suspend fun removeAddress(commonDataReq: CommonDataReq): Response<CommonResponse> {
        return profileApiService.removeAddress(commonDataReq)
    }

    suspend fun getOrderDetails(commonDataReq: CommonDataReq): Response<OrderDetailsResponse> {
        return profileApiService.getOrderDetails(commonDataReq)
    }

    fun getOrderList(productDataReq: ProductDataReq) : Flow<PagingData<Order>> {
        return Pager(config = PagingConfig(Constant.PAGE_SIZE, enablePlaceholders = false)) {
            OrderListPagingSource(profileApiService, productDataReq)
        }.flow
    }



}