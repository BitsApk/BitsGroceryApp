package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class HomeDataReq(
    @SerializedName("domain")
    val domain: String? = "" ,
    @SerializedName("user_id")
    val userId: String? = ""
)