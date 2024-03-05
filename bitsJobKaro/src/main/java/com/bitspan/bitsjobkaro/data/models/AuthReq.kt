package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class AuthReq(
    @SerializedName("auth") val auth: Boolean
)