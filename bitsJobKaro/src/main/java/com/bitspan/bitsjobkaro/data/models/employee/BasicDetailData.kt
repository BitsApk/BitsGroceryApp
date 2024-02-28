package com.bitspan.bitsjobkaro.data.models.employee

import com.google.gson.annotations.SerializedName

data class BasicDetailData(
    @SerializedName("email") var emailId: String? = "",
    @SerializedName("c_num") var cNum: String? = "",
    @SerializedName("c_gst") var gender: String? = "",
    @SerializedName("c_gst_old") val dob: String? = "",
    @SerializedName("c_logo_old") val qual: String? = "",
)
