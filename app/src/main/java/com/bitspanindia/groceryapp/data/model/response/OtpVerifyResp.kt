package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class OtpVerifyResp(
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("phone")
    val phone: String? = "",
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("user_id")
    val userId: String? = ""
)