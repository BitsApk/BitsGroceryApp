package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class ConfirmOrderResponse(
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("order_id")
    val orderId: String? = "",
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("statusCode")
    val statusCode: Int? = 0
)