package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class OtpVerifyReq(
    @SerializedName("fcm_token") val fcmToken: String? = "",
    @SerializedName("otp") val otp: Int? = 0,
    @SerializedName("phone") val phone: Long? = 0
)