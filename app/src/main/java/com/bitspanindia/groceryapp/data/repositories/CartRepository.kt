package com.bitspanindia.groceryapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.HomeDataX
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.SearchProductResponse
import com.bitspanindia.groceryapp.data.model.SubCategoryData
import com.bitspanindia.groceryapp.data.model.request.ApplyCouponReq
import com.bitspanindia.groceryapp.data.model.request.CartValidateReq
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.ConfirmOrderReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.PaymentReq
import com.bitspanindia.groceryapp.data.model.request.PaymentVerifyReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.model.response.ApplyCouponResp
import com.bitspanindia.groceryapp.data.model.response.CartValidateResponse
import com.bitspanindia.groceryapp.data.model.response.ConfirmOrderResponse
import com.bitspanindia.groceryapp.data.model.response.CouponListResp
import com.bitspanindia.groceryapp.data.model.response.DeliveryChargeResp
import com.bitspanindia.groceryapp.data.model.response.PaymentResponse
import com.bitspanindia.groceryapp.data.model.response.PaymentVerifyResponse
import com.bitspanindia.groceryapp.data.pagingSource.ProductPagingSource
import com.bitspanindia.groceryapp.data.services.CartApiService
import com.bitspanindia.groceryapp.data.services.HomeApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject


class CartRepository @Inject constructor(private val cartApiService: CartApiService) {


    suspend fun doPayment(paymentReq: PaymentReq): Response<PaymentResponse> {
        return cartApiService.doPayment(paymentReq)
    }

    suspend fun doPaymentVerification(paymentVerifyReq: PaymentVerifyReq): Response<PaymentVerifyResponse> {
        return cartApiService.doPaymentVerification(paymentVerifyReq)
    }

    suspend fun doConfirmOrder(confirmOrderReq: ConfirmOrderReq): Response<ConfirmOrderResponse> {
        return cartApiService.doConfirmOrder(confirmOrderReq)
    }

    suspend fun validateCart(cartValidateReq: CartValidateReq): Response<CartValidateResponse> {
        return cartApiService.validateCart(cartValidateReq)
    }

    suspend fun getDeliveryCharges(commonDataReq: CommonDataReq): Response<DeliveryChargeResp> {
        return cartApiService.getDeliveryCharges(commonDataReq)
    }


    suspend fun checkCoupon(applyCouponReq: ApplyCouponReq): Response<ApplyCouponResp> {
        return cartApiService.checkCoupon(applyCouponReq)
    }

    suspend fun getCouponList(): Response<CouponListResp> {
        return cartApiService.getCouponList()
    }

}