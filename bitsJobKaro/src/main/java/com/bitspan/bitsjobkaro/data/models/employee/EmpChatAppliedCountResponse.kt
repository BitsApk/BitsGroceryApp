package com.bitspan.bitsjobkaro.data.models.employee


import com.google.gson.annotations.SerializedName

data class EmpChatAppliedCountResponse(
    @SerializedName("chat_with_rec") var chatWithRec: Int? = 0,
    @SerializedName("message") var message: String? = "",
    @SerializedName("status") var status: Int? = 0,
    @SerializedName("total_job_applied") var totalJobApplied: Int? = 0,
    @SerializedName("total_save_job") var totalSaveJob: String? = "0",
    @SerializedName("total_posted") var totalPosted: String? = "0",
)