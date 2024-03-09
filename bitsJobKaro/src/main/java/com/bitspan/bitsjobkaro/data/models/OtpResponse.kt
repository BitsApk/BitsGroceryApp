package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class OtpResponse(
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0,
    @SerializedName("number") val number: String? = "",
)