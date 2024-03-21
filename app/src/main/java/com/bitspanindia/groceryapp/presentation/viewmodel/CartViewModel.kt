package com.bitspanindia.groceryapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitspanindia.groceryapp.data.model.Cart
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.request.ApplyCouponReq
import com.bitspanindia.groceryapp.data.model.request.CartValidateReq
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.ConfirmOrderReq
import com.bitspanindia.groceryapp.data.model.request.PaymentReq
import com.bitspanindia.groceryapp.data.model.request.PaymentVerifyReq
import com.bitspanindia.groceryapp.data.model.response.ApplyCouponResp
import com.bitspanindia.groceryapp.data.model.response.CartValidateResponse
import com.bitspanindia.groceryapp.data.model.response.ConfirmOrderResponse
import com.bitspanindia.groceryapp.data.model.response.CouponListResp
import com.bitspanindia.groceryapp.data.model.response.DeliveryChargeResp
import com.bitspanindia.groceryapp.data.model.response.PaymentResponse
import com.bitspanindia.groceryapp.data.model.response.PaymentVerifyResponse
import com.bitspanindia.groceryapp.data.repositories.CartRepository
import com.bitspanindia.groceryapp.presentation.CartManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {


    suspend fun doPayment(paymentReq: PaymentReq): Response<PaymentResponse> {
        return cartRepository.doPayment(paymentReq)
    }

    suspend fun doPaymentVerification(paymentVerifyReq: PaymentVerifyReq): Response<PaymentVerifyResponse> {
        return cartRepository.doPaymentVerification(paymentVerifyReq)
    }

    suspend fun doConfirmOrder(confirmOrderReq: ConfirmOrderReq): Response<ConfirmOrderResponse> {
        return cartRepository.doConfirmOrder(confirmOrderReq)
    }

    suspend fun validateCart(cartValidateReq: CartValidateReq): Response<CartValidateResponse> {
        return cartRepository.validateCart(cartValidateReq)
    }

    suspend fun getDeliveryCharges(commonDataReq: CommonDataReq): Response<DeliveryChargeResp> {
        return cartRepository.getDeliveryCharges(commonDataReq)
    }


    suspend fun checkCoupon(applyCouponReq: ApplyCouponReq): Response<ApplyCouponResp> {
        return cartRepository.checkCoupon(applyCouponReq)
    }

    suspend fun getCouponList(): Response<CouponListResp> {
        return cartRepository.getCouponList()
    }


}