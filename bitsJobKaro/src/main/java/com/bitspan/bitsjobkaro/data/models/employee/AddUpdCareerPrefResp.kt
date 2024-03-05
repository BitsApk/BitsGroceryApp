package com.bitspan.bitsjobkaro.data.models.employee


import com.google.gson.annotations.SerializedName

data class AddUpdCareerPrefResp(
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0
)