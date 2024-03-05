package com.bitspan.bitsjobkaro.data.models.employee

import com.google.gson.annotations.SerializedName

data class CareerEmpData(
    @SerializedName("id") val formId: String? = "",
    @SerializedName("p_job_roles") val empJobRoles: String? = "",
    @SerializedName("p_skills") val empPrefSkills: String? = "",
    @SerializedName("p_cities") val empPrefCities: String? = "",
    @SerializedName("p_state") val empPrefState: String? = "",
    @SerializedName("p_sal_L") val empSalaryLakh: String? = "",
    @SerializedName("p_sal_T") val empSalaryThousand: String? = "",
    @SerializedName("p_e_type") val empPrefEmpType: String? = "",
    @SerializedName("t_exp_type") val empTypeExp: String? = "",
    @SerializedName("t_exp_yr") val empExpYear: String? = "",
    @SerializedName("t_exp_month") val empExpMonth: String? = "",
    @SerializedName("Objective") val empObjective: String? = "",
    @SerializedName("eTYpe") val empTypeOH: String? = "",
    @SerializedName("p_shift") val empPrefShift: String? = "",
    @SerializedName("skills") val empSkills: String? = "",
    @SerializedName("eng") val empEnglishLabel: String? = "",
    @SerializedName("resume") val empResume: String? = "",
    @SerializedName("created_at") val empCreatedAt: String? = "",
    @SerializedName("created_by") val empCreatedBy: String? = "",
    @SerializedName("state_title") val empState: String? = ""
)