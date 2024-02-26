package com.bitspanindia.groceryapp.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileReq(
    @SerializedName("email") var email: String? = "",
    @SerializedName("gender") var gender: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("phone") var phone: String? = "",
    @SerializedName("user_id") var userId: String? = ""
)