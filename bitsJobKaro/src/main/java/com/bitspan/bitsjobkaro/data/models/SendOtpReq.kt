package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class SendOtpReq(
    @SerializedName("name") val name: String?,
    @SerializedName("number") val number: Long? = 0,
    @SerializedName("server") val server: String?,
    @SerializedName("is_login") val isLogin : String?
)