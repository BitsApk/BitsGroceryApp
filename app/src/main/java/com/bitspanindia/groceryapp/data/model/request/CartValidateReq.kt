package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class CartValidateReq(
    @SerializedName("cart") val cart: List<CartValidateData>? = listOf(),
    @SerializedName("seller_id") var sellerId: String? = null,
    @SerializedName("seller_auto_id") var sellerAutoId: String? = null,
)

data class CartValidateData(
    @SerializedName("product_id") val productId: Int? = 0,
    @SerializedName("qty") val qty: Int? = 0,
    @SerializedName("size_id") val sizeId: Int? = 0
)