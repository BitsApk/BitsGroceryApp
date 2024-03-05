package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class EmpExpData(
    @SerializedName("all_previous_com") val allPreviousCom: List<AllPreviousCom>? = listOf(),
    @SerializedName("new_p_details") val newPDetails: List<NewPDetail>? = listOf()
)

data class NewPDetail(
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("created_by") val createdBy: String? = "",
    @SerializedName("curr_com_desig") val currComDesig: String? = "",
    @SerializedName("curr_com_name") val currComName: String? = "",
    @SerializedName("curr_d_from") val currDFrom: String? = "",
    @SerializedName("curr_d_to") val currDTo: String? = "",
    @SerializedName("curr_notice") val currNotice: String? = "",
    @SerializedName("id") val id: String? = "",
    @SerializedName("old_com_desig") val oldComDesig: String? = "",
    @SerializedName("old_com_name") val oldComName: String? = "",
    @SerializedName("old_d_from") val oldDFrom: String? = "",
    @SerializedName("old_d_to") val oldDTo: String? = ""
)

data class AllPreviousCom(
    @SerializedName("c_name") val cName: String? = "",
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("created_by") val createdBy: String? = "",
    @SerializedName("date_from") val dateFrom: String? = "",
    @SerializedName("date_to") val dateTo: String? = "",
    @SerializedName("id") val id: String? = "",
    @SerializedName("p_desig") val pDesig: String? = "",
    @SerializedName("status") val status: String? = ""
)


// Emp carrer preferences
data class EmpCareerPrefeData(
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("created_by") val createdBy: String? = "",
    @SerializedName("eTYpe") val eTYpe: String? = "",
    @SerializedName("eng") val eng: String? = "",
    @SerializedName("id") val id: String? = "",
    @SerializedName("Objective") val objective: String? = "",
    @SerializedName("p_cities") val pCities: String? = "",
    @SerializedName("p_e_type") val pEType: String? = "",
    @SerializedName("p_job_roles") val pJobRoles: String? = "",
    @SerializedName("p_sal_L") val pSalL: String? = "",
    @SerializedName("p_sal_T") val pSalT: String? = "",
    @SerializedName("p_shift") val pShift: String? = "",
    @SerializedName("p_skills") val pSkills: Any? = Any(),
    @SerializedName("p_state") val pState: String? = "",
    @SerializedName("resume") val resume: String? = "",
    @SerializedName("skills") val skills: String? = "",
    @SerializedName("state_title") val stateTitle: String? = "",
    @SerializedName("t_exp_month") val tExpMonth: String? = "",
    @SerializedName("t_exp_type") val tExpType: String? = "",
    @SerializedName("t_exp_yr") val tExpYr: String? = ""
)
