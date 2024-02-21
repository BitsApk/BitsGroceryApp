package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class PaymentVerifyReq(
    @SerializedName("conv_charge") val convCharge: Double? = 0.0,
    @SerializedName("orderno") val orderno: String? = "",
    @SerializedName("TXN_AMOUNT") val tXNAMOUNT: Double? = 0.0,
    @SerializedName("tarns_id") val tarnsId: String? = "",
    @SerializedName("user_id") val userId: Int? = 0
)