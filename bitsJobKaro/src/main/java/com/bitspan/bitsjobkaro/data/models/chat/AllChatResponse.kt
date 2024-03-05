package com.bitspan.bitsjobkaro.data.models.chat


import com.google.gson.annotations.SerializedName

data class AllChatResponse(
    @SerializedName("all_msg") val allMsg: MutableList<AllMsgData>? = mutableListOf(),
    @SerializedName("last_msg_id") val lastMsgId: String? = "",
    @SerializedName("status") val status: Int? = 0
)


data class AllMsgData(
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("created_by_em") val createdByEm: String? = "",
    @SerializedName("created_by_rec") val createdByRec: String? = "",
    @SerializedName("ee_id") val eeId: String? = "",
    @SerializedName("id") val id: String? = "",
    @SerializedName("msg") val msg: String? = "",
    @SerializedName("read_by_emp") val readByEmp: String? = "",
    @SerializedName("read_by_rec") val readByRec: String? = "",
    @SerializedName("rec_id") val recId: String? = "",
    @SerializedName("status") val status: String? = ""
)