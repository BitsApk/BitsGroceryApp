package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class LoginMobReq(
    @SerializedName("phone")
    val phone: Long? = 0
)