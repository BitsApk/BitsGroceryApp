package com.bitspanindia.groceryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bitspanindia.groceryapp.data.model.request.AddAddressReq
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.model.request.UpdateProfileReq
import com.bitspanindia.groceryapp.data.model.response.AddressListResponse
import com.bitspanindia.groceryapp.data.model.response.CommonResponse
import com.bitspanindia.groceryapp.data.model.response.GetProfileResponse
import com.bitspanindia.groceryapp.data.model.response.Order
import com.bitspanindia.groceryapp.data.model.response.OrderDetailsResponse
import com.bitspanindia.groceryapp.data.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {

    suspend fun updateProfile(updateProfileReq: UpdateProfileReq): Response<CommonResponse> {
        return profileRepository.updateProfile(updateProfileReq)
    }

    suspend fun getProfileData(homeDataReq: HomeDataReq): Response<GetProfileResponse> {
        return profileRepository.getProfileData(homeDataReq)
    }

    suspend fun getAddressList(homeDataReq: HomeDataReq): Response<AddressListResponse> {
        return profileRepository.getAddressList(homeDataReq)
    }

    suspend fun removeAddress(commonDataReq: CommonDataReq): Response<CommonResponse> {
        return profileRepository.removeAddress(commonDataReq)
    }

    suspend fun getOrderDetails(commonDataReq: CommonDataReq): Response<OrderDetailsResponse> {
        return profileRepository.getOrderDetails(commonDataReq)
    }

    suspend fun addAddress(addressReq: AddAddressReq): Response<CommonResponse> {
        return profileRepository.addAddress(addressReq)
    }

    fun getOrderList(productDataReq: ProductDataReq): Flow<PagingData<Order>> {
        return profileRepository.getOrderList(productDataReq).cachedIn(viewModelScope)
    }

}