package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class VerifyOtpReq(
    @SerializedName("otp") val otp: Int?,
    @SerializedName("number") val number: Long?,
    @SerializedName("server") val server: String?,
)