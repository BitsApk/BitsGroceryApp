package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class PaymentReq(
    @SerializedName("added_by_web") val addedByWeb: String? = "",
    @SerializedName("conv_charge") val convCharge: Int = 0,
    @SerializedName("paymenttype") val paymenttype: String? = "",
    @SerializedName("total_payble") val totalPayble: Int = 0,
    @SerializedName("user_id") val userId: Int? = 0,
    @SerializedName("seller_auto_id") val sellerAutoId: String? = "",
    @SerializedName("seller_id") val sellerId: String? = "",
    @SerializedName("cart") val cart: List<CartValidateData>? = listOf()

)