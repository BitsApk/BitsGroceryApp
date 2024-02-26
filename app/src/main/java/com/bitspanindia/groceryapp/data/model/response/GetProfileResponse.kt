package com.bitspanindia.groceryapp.data.model.response

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("gender")
    var gender: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("phone")
    var phone: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0,
    @SerializedName("user_id")
    var userId: String? = ""
)