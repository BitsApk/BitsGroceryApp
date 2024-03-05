package com.bitspan.bitsjobkaro.data.models

import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("email")
    val email: String,

    @SerializedName("pass")
    val password: String
)
