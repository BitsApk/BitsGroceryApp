package com.bitspan.bitsjobkaro.data.models

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("status") val loginStatus: Int?,
    @SerializedName("message") val loginMessage: String?,
    @SerializedName("data") val data: List<LoginResponceBody>? = listOf(),
)

data class LoginResponceBody(
    @SerializedName("id") val id: String? = "",
    @SerializedName("c_name") val name: String? = "",
    @SerializedName("c_email") val email: String? = "",
    @SerializedName("c_contact") val contact: String? = "",
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("log_in_time") val loginTime: String? = ""
)
