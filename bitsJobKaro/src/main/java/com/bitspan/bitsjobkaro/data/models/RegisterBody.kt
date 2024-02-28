package com.bitspan.bitsjobkaro.data.models

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("name") val name: String,

    @SerializedName("email") val email: String,

    @SerializedName("mobile") val mobile: String,

    @SerializedName("password") val password: String
)