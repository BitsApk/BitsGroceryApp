package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("email") val email: String = "",
    @SerializedName("fcm_token") val fcmToken: String? = "",
    @SerializedName("password") val password: String = ""
)