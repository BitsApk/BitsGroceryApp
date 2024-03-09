package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class VerifyLoginOtpResponse(
    @SerializedName("data") val data: List<LoginResponceBody>? = listOf(),
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0,
    @SerializedName("user_type") val userType: String? = ""
)

