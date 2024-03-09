package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class ApiAuthResponse(
    @SerializedName("api_auth") val apiAuth: List<ApiAuth>? = listOf(),
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0
)


data class ApiAuth(
    @SerializedName("id") val id: String? = "",
    @SerializedName("password") val password: String? = "",
    @SerializedName("username") val username: String? = ""
)