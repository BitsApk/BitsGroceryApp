package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class PaymentVerifyResponse(
    @SerializedName("message") val message: String? = "",
    @SerializedName("payment_verify") val paymentVerify: Boolean = false,
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0
)