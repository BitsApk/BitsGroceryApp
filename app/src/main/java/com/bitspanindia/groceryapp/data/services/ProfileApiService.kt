package com.bitspanindia.groceryapp.data.services

import com.bitspanindia.groceryapp.data.model.request.AddAddressReq
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.model.request.UpdateProfileReq
import com.bitspanindia.groceryapp.data.model.response.AddressListResponse
import com.bitspanindia.groceryapp.data.model.response.CommonResponse
import com.bitspanindia.groceryapp.data.model.response.GetProfileResponse
import com.bitspanindia.groceryapp.data.model.response.OrderDetailsResponse
import com.bitspanindia.groceryapp.data.model.response.OrderListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
interface ProfileApiService {
    @POST("profile-update")
    suspend fun updateProfile(
        @Body updateProfileReq: UpdateProfileReq
    ): Response<CommonResponse>

    @POST("profile_data")
    suspend fun getProfileData(
        @Body homeDataReq: HomeDataReq
    ): Response<GetProfileResponse>

    @POST("my-address")
    suspend fun getAddressList(
        @Body homeDataReq: HomeDataReq
    ): Response<AddressListResponse>

    @POST("remove-address")
    suspend fun removeAddress(
        @Body commonDataReq: CommonDataReq
    ): Response<CommonResponse>

    @POST("my-orders")
    suspend fun getOrderList(
        @Body productDataReq: ProductDataReq
    ): Response<OrderListResponse>

    @POST("orderDetail")
    suspend fun getOrderDetails(
        @Body commonDataReq: CommonDataReq
    ): Response<OrderDetailsResponse>

    @POST("add-address")
    suspend fun addAddress(
        @Body addAddressReq: AddAddressReq
    ): Response<CommonResponse>

}