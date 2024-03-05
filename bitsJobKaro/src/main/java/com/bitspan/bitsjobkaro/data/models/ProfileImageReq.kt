package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class ProfileImageReq(
    @SerializedName("employee_id") val employeeId: Int?,
    @SerializedName("rec_id") val recId: Int?
)