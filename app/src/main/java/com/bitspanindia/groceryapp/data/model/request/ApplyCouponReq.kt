package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class ApplyCouponReq(
    @SerializedName("cart") val cart: List<CartValidateData>? = listOf(),
    @SerializedName("couponcode") val couponcode: String? = "",
    @SerializedName("seller_auto_id") val sellerAutoId: Int? = 0,
    @SerializedName("seller_id") val sellerId: String? = ""
)