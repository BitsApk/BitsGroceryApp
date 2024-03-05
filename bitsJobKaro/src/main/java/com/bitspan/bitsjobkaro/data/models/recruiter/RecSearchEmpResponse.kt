package com.bitspan.bitsjobkaro.data.models.recruiter


import com.google.gson.annotations.SerializedName

data class RecSearchEmpResponse(
    @SerializedName("list") val list: List<RecruiterEmpData>? = listOf(),
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0
)