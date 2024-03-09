package com.bitspan.bitsjobkaro.data.models.chat


import com.google.gson.annotations.SerializedName

data class EmpDataResponse(
    @SerializedName("data") val data: List<EmpData>? = listOf(),
    @SerializedName("file_path") val filePath: String? = "",
    @SerializedName("status") val status: Int? = 0
)

data class EmpData(
    @SerializedName("c_contact") val cContact: String? = "",
    @SerializedName("resume") val resume: String? = ""
)