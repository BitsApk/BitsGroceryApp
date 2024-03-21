package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class ApplyCouponResp(
    @SerializedName("couponid") val couponId: Int? = 0,
    @SerializedName("couponamt") val couponAmt: Double? = 0.0,
    @SerializedName("couponcode") val couponCode: String? = "",
    @SerializedName("coupon_discount") val couponDiscount: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("message") val mess: String? = ""
)