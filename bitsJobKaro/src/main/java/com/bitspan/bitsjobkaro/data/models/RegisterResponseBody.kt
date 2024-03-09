package com.bitspan.bitsjobkaro.data.models

import com.google.gson.annotations.SerializedName

data class RegisterResponseBody(
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("mobile")
    val mobile: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("password")
    val password: String? = ""
) {
    override fun toString(): String {
        return "RegisterResponseBody(email=$email, id=$id, mobile=$mobile, name=$name, password=$password)"
    }
}