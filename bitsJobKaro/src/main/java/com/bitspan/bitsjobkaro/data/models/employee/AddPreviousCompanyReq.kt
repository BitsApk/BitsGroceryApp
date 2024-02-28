package com.bitspan.bitsjobkaro.data.models.employee

import com.google.gson.annotations.SerializedName

data class AddPreviousCompanyReq(
    @SerializedName("prev_com_desig")
    var preComDesig: String? = "",
    @SerializedName("prev_com_name")
    var preComName: String? = "",
    @SerializedName("prev_d_from")
    var preDFrom: String? = "",
    @SerializedName("prev_to")
    var preDTo: String? = "",
    @SerializedName("emp_id")
    var empId: Int? = 0,
    @SerializedName("formid")
    var formid: String? = "",
    @SerializedName("id")
    var id: String? = "",
)