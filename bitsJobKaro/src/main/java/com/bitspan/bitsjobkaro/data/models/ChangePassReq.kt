package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class ChangePassReq(
    @SerializedName("new_password") val newPassword: String? = "",
    @SerializedName("old_password") val oldPassword: String? = "",
    @SerializedName("rec_id") val recId: Int?,
    @SerializedName("emp_id") val empId: Int?

)