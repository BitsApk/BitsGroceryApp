package com.bitspan.bitsjobkaro.data.models.chat


import com.google.gson.annotations.SerializedName

data class RecToEmpChatResp (
    @SerializedName("data") val data: List<EmpChatData>? = listOf(),
    @SerializedName("status") val status: Int? = 0,
    @SerializedName("message") val mess: String? = ""
)


data class EmpChatData(
    @SerializedName("id") val id: String? = "",
    @SerializedName("c_name") val cName: String? = "",
    @SerializedName("p_job_roles") val jobRole: String? = "",
    @SerializedName("log_in_time") val logInTime: String? = "",
)