package com.bitspan.bitsjobkaro.data.models.employee


import com.google.gson.annotations.SerializedName

data class EmpSearchJobReq(
    @SerializedName("city") var city: List<String>? = listOf(),
    @SerializedName("jd") var jd: List<String>? = listOf(),
    @SerializedName("emp_id") var empId: Int? = 0,
    @SerializedName("emp_type") var empType: String? = "",
    @SerializedName("job_type") var jobType: String? = "",
    @SerializedName("multipleSea") var multipleSea: String? = "",
    @SerializedName("salary") var salary: String? = "",
    @SerializedName("shift") var shift: String? = ""
)