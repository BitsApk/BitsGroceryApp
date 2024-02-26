package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class RegisterApiReq(
    @SerializedName("added_by_web") val addedByWeb: String? = "",
    @SerializedName("confpasswords") val confpasswords: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("fcm_token") val fcmToken: String? = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("password") val password: String = "",
    @SerializedName("phone") val phone: String = ""
)