package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class HomeDataReq(
    @SerializedName("added_by_web") val addedByWeb: String? = "",

    @SerializedName("user_id") val userId: String? = null,

    @SerializedName("seller_id") var sellerId: String? = null,
    @SerializedName("seller_auto_id") var sellerAutoId: String? = null,
)