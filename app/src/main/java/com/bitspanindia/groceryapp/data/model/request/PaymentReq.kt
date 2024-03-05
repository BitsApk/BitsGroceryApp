package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class PaymentReq(
    @SerializedName("added_by_web") val addedByWeb: String? = "",
    @SerializedName("conv_charge") val convCharge: Double? = 0.0,
    @SerializedName("paymenttype") val paymenttype: String? = "",
    @SerializedName("total_payble") val totalPayble: Double? = 0.0,
    @SerializedName("user_id") val userId: Int? = 0,
    @SerializedName("cart") val cart: List<CartValidateData>? = listOf()

)