package com.bitspanindia.groceryapp.data.model.request

import com.google.gson.annotations.SerializedName

data class HomeDataReq(
    @SerializedName("added_by_web")
    val addedByWeb: String? = "",
    @SerializedName("seller_id")
    val sellerId: String? = "",
    @SerializedName("seller_auto_id")
    val sellerAutoId: String? = "",

    @SerializedName("user_id")
    val userId: String? = null
)