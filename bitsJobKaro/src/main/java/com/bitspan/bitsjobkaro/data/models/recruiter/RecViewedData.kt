package com.bitspan.bitsjobkaro.data.models.recruiter


import com.google.gson.annotations.SerializedName

data class RecViewedData(
    @SerializedName("job_applied") val jobApplied: List<RecNotification>? = listOf(),
    @SerializedName("job_viewed") val jobViewed: List<RecNotification>? = listOf(),
    @SerializedName("status") val status: Int? = 0
)


data class RecNotification(
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("created_by") val createdBy: String? = "",
    @SerializedName("District") val district: String? = "",
    @SerializedName("eTYpe") val eTYpe: String? = "",
    @SerializedName("emp_id") val empId: String? = "",
    @SerializedName("emp_name") val empName: String? = "",
    @SerializedName("eng") val eng: String? = "",
    @SerializedName("id") val id: String? = "",
    @SerializedName("jTitle") val jTitle: String? = "",
    @SerializedName("job_id") val jobId: String? = "",
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
    @SerializedName("row_id") val rowId: String? = "",
    @SerializedName("sMax") val sMax: String? = "",
    @SerializedName("sMin") val sMin: String? = "",
    @SerializedName("skills") val skills: String? = "",
    @SerializedName("state") val state: String? = "",
    @SerializedName("t_exp_month") val tExpMonth: String? = "",
    @SerializedName("t_exp_type") val tExpType: String? = "",
    @SerializedName("t_exp_yr") val tExpYr: String? = ""
)