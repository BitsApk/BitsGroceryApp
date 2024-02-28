package com.bitspan.bitsjobkaro.data.models.chat


import com.google.gson.annotations.SerializedName

data class SendMessResponse(
    @SerializedName("last_msg_id") val lastMsgId: Int? = 0,
    @SerializedName("status") val status: String? = ""
)