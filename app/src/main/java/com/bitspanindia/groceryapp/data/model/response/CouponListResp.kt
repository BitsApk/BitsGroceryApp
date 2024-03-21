package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class CouponListResp(
    @SerializedName("coupon_code") val couponCode: List<CouponCode>? = listOf(),
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0
)

data class CouponCode(
    @SerializedName("expiry_date") val expiryDate: String? = "",
    @SerializedName("expiry_time") val expiryTime: String? = "",
    @SerializedName("id") val id: String? = "",
    @SerializedName("image") val image: String? = "",
    @SerializedName("promo_code") val promoCode: String? = "",
    @SerializedName("promo_code_discount") val promoCodeDiscount: String? = "",
    @SerializedName("promo_code_discription") val promoCodeDiscription: String? = "",
    @SerializedName("promo_code_maxapplication") val promoCodeMaxapplication: String? = "",
    @SerializedName("promo_code_minapplication") val promoCodeMinapplication: String? = "",
    @SerializedName("promo_code_name") val promoCodeName: String? = "",
    @SerializedName("promo_code_noofredeemsallowed") val promoCodeNoofredeemsallowed: String? = "",
    @SerializedName("start_date") val startDate: String? = "",
    @SerializedName("start_time") val startTime: String? = "",
    @SerializedName("status") val status: String? = "",
    var canApply: Boolean? = null,
    var mess: String? = ""

)