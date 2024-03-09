package com.bitspanindia.groceryapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.request.AddAddressReq
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.model.request.UpdateProfileReq
import com.bitspanindia.groceryapp.data.model.response.AddressListResponse
import com.bitspanindia.groceryapp.data.model.response.CommonResponse
import com.bitspanindia.groceryapp.data.model.response.GetProfileResponse
import com.bitspanindia.groceryapp.data.model.response.MyAddress
import com.bitspanindia.groceryapp.data.model.response.Order
import com.bitspanindia.groceryapp.data.model.response.OrderDetailsResponse
import com.bitspanindia.groceryapp.data.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor() : ViewModel() {

    var myAddress = MutableLiveData<MyAddress>()
    var redirectFrom = ""

}