package com.bitspan.bitsjobkaro.data.models.employee

import com.google.gson.annotations.SerializedName
data class AddCurrentCompanyReq(
    @SerializedName("curr_com_desig")
    var currComDesig: String? = "",
    @SerializedName("curr_com_name")
    var currComName: String? = "",
    @SerializedName("curr_d_from")
    var currDFrom: String? = "",
    @SerializedName("curr_d_to")
    var currDTo: String? = "",
    @SerializedName("curr_notice")
    var currNotice: Int? = 0,
    @SerializedName("emp_id")
    var empId: Int? = 0,
    @SerializedName("formid")
    var formid: String? = ""
)