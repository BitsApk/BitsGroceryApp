package com.bitspan.bitsjobkaro.data.models.employee

import com.google.gson.annotations.SerializedName

data class EmpApplyJobReq(
    @SerializedName("emp_id") var empId: Int? = 0,
    @SerializedName("job_id") var jobId: Int? = 0,
    @SerializedName("rec_id") var recId: Int? = 0
)