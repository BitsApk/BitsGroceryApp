package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class LoginMobResp(
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("otp")
    val otp: String? = "",
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("statusCode")
    val statusCode: Int? = 0
)