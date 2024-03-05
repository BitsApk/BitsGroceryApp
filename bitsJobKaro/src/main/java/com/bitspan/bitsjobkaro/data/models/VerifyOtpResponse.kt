package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("message") val message: String? = "",
    @SerializedName("number") val number: Long? = 0,
    @SerializedName("status") val status: Int? = 0
)