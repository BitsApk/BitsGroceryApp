package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class ForgotPassReq(
    @SerializedName("confirmpss") val confirmpss: String?,
    @SerializedName("number") val number: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("server") val server: String?
)