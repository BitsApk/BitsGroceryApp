package com.bitspanindia.groceryapp.data.services

import com.bitspanindia.groceryapp.data.model.HomeDataX
import com.bitspanindia.groceryapp.data.model.SearchProductResponse
import com.bitspanindia.groceryapp.data.model.SubCatProductsData
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface CartApiService {

    @POST("paynow")
    suspend fun doPayment(
        @Body paymentReq: PaymentReq
    ): Response<PaymentResponse>

    @POST("paymentVerify")
    suspend fun doPaymentVerification(
        @Body paymentVerifyReq: PaymentVerifyReq
    ): Response<PaymentVerifyResponse>

    @POST("confirmOrder")
    suspend fun doConfirmOrder(
        @Body confirmOrderReq: ConfirmOrderReq
    ): Response<ConfirmOrderResponse>

    @POST("CartValidate")
    suspend fun validateCart(
        @Body cartValidateReq: CartValidateReq
    ): Response<CartValidateResponse>

    @POST("GetDeliveryCharge")
    suspend fun getDeliveryCharges(
        @Body commonDataReq: CommonDataReq
    ): Response<DeliveryChargeResp>


    @POST("ApplyCoupon")
    suspend fun checkCoupon(
        @Body applyCouponReq: ApplyCouponReq
    ): Response<ApplyCouponResp>

    @POST("coupon-code")
    suspend fun getCouponList(): Response<CouponListResp>
}