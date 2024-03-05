package com.bitspan.bitsjobkaro.data.models.chat


import com.google.gson.annotations.SerializedName

data class EmpToRecChatResp (
    @SerializedName("data") val data: List<RecChatData>? = listOf(),
    @SerializedName("status") val status: Int? = 0,
    @SerializedName("message") val mess: String? = ""
)


data class RecChatData(
    @SerializedName("c_contact") val cContact: String? = "",
    @SerializedName("c_email") val cEmail: String? = "",
    @SerializedName("c_name") val cName: String? = "",
    @SerializedName("c_pas") val cPas: String? = "",
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("id") val id: String? = "",
    @SerializedName("log_in_time") val logInTime: String? = "",
    @SerializedName("status") val status: String? = ""
)