package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class DeliveryChargeResp(
    @SerializedName("conv_charge") val convCharge: Double? = 0.0,
    @SerializedName("delivery_charge") val deliveryCharge: Double? = 0.0,
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0
)