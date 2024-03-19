package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class RegisterApiResponse(
    @SerializedName("email") val email: String? = "",
    @SerializedName("message") val message: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("phone") val phone: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("user_id") val userId: Int? = 0
)