package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("key") val key: String? = "",
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("username") val username: String? = ""
)