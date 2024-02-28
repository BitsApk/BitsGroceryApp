package com.bitspan.bitsjobkaro.data.models.employee

import com.google.gson.annotations.SerializedName

data class GetAllProfessDetails(
    @SerializedName("all_pre_com") var allPreCom: List<AllPreCom>? = listOf(),
    @SerializedName("curr_details") var currDetails: List<CurrDetail>? = listOf(),
    @SerializedName("message") var message: String? = "",
    @SerializedName("status") var status: Int? = 0
)

data class AllPreCom(
    @SerializedName("c_name") var cName: String? = "",
    @SerializedName("created_at") var createdAt: String? = "",
    @SerializedName("created_by") var createdBy: String? = "",
    @SerializedName("date_from") var dateFrom: String? = "",
    @SerializedName("date_to") var dateTo: String? = "",
    @SerializedName("id") var id: String? = "",
    @SerializedName("p_desig") var pDesig: String? = "",
    @SerializedName("status") var status: String? = ""
)

data class CurrDetail(
    @SerializedName("created_at") var createdAt: String? = "",
    @SerializedName("created_by") var createdBy: String? = "",
    @SerializedName("curr_com_desig") var currComDesig: String? = "",
    @SerializedName("curr_com_name") var currComName: String? = "",
    @SerializedName("curr_d_from") var currDFrom: String? = "",
    @SerializedName("curr_d_to") var currDTo: String? = "",
    @SerializedName("curr_notice") var currNotice: String? = "",
    @SerializedName("id") var id: String? = "",
    @SerializedName("old_com_desig") var oldComDesig: String? = "",
    @SerializedName("old_com_name") var oldComName: String? = "",
    @SerializedName("old_d_from") var oldDFrom: String? = "",
    @SerializedName("old_d_to") var oldDTo: String? = ""
)