package com.bitspan.bitsjobkaro.data.models.employee


import com.google.gson.annotations.SerializedName

data class EmpBasicDataReq(
    @SerializedName("contactno") val contactno: String,
    @SerializedName("dob") val dob: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("emp_id") val empId: Int?,
    @SerializedName("formid") val formid: Int?,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("qualif") val qualif: String = ""
)