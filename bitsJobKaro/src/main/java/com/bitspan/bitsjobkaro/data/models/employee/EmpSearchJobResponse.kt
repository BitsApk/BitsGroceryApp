package com.bitspan.bitsjobkaro.data.models.employee


import com.google.gson.annotations.SerializedName
import com.bitspan.bitsjobkaro.data.models.AllJobData

data class EmpSearchJobResponse(
    @SerializedName("list") val list: List<AllJobData>? = listOf(),
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0
)