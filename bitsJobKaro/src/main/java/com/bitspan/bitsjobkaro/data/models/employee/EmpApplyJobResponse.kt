package com.bitspan.bitsjobkaro.data.models.employee



import com.google.gson.annotations.SerializedName

data class EmpApplyJobResponse(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: Int? = 0
)