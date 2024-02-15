package com.bitspanindia.groceryapp.data.model.request

import com.google.gson.annotations.SerializedName

data class CommonDataReq(
    @SerializedName("category_id")
    val categoryId: String? = "",


    @SerializedName("product_name")
    var productName: String? = "",

    @SerializedName("user_id")
    var userId: String? = "",

    )