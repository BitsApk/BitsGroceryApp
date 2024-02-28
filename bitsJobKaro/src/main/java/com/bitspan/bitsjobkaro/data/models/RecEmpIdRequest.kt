package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class RecEmpIdRequest(
    @SerializedName("emp_id") var empId: Int? = null,
    @SerializedName("rec_id") val recId: Int? = null
)