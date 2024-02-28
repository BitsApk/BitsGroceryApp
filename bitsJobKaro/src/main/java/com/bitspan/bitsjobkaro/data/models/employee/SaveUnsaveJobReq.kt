package com.bitspan.bitsjobkaro.data.models.employee


import com.google.gson.annotations.SerializedName

data class SaveUnsaveJobReq(
    @SerializedName("emp_id") val empId: Int? = 0,
    @SerializedName("job_id") val jobId: String? = "",
    @SerializedName("server") val server: String? = ""
)