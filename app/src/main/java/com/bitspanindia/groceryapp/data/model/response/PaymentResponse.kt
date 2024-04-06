package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("CONV_CHARGE") val cONVCHARGE: Int = 0,
    @SerializedName("CUST_ID") val cUSTID: String? = "",
    @SerializedName("MAIN_AMOUNT") val mAINAMOUNT: Int? = 0,
    @SerializedName("MID") val mID: String? = "",
    @SerializedName("message") val message: String? = "",
    @SerializedName("order_id") val orderId: String? = "",
    @SerializedName("payment_type") val paymentType: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("TXN_AMOUNT") val tXNAMOUNT: Int? = 0,
    @SerializedName("WEB_CALLBACK_URL") val wEBCALLBACKURL: String? = "",
    @SerializedName("is_outofstock") val isOutOfStock: Int? = 0
)