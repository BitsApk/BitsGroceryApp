package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class ProfileImageResponse(
    @SerializedName("logo") val logo: String? = "",
    @SerializedName("message") val message: String? = "",
    @SerializedName("profile") val profile: String? = "",
    @SerializedName("resume") val resume: String? = "",
    @SerializedName("status") val status: Int? = 0
)