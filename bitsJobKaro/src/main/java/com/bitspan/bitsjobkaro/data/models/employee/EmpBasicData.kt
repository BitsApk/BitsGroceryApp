package com.bitspan.bitsjobkaro.data.models.employee

import com.google.gson.annotations.SerializedName

data class EmpBasicData(
    @SerializedName("id") val formId: String? = "",
    @SerializedName("c_name") val empName: String? = "",
    @SerializedName("c_contact") val empContact: String? = "",
    @SerializedName("c_dob") val empDob: String? = "",
    @SerializedName("c_email") val empEmail: String? = "",
    @SerializedName("c_exp") val empExp: String? = "",
    @SerializedName("gender") val empGender: String? = "",
    @SerializedName("highestQual") val empHigQualification: String? = "",
    @SerializedName("skills") val empSkills: String? = "",
    @SerializedName("uploadPic") val uploadPic: String? = "",
    @SerializedName("eng") val empEnglish: String? = "",
    @SerializedName("creted_at") val empCreatedAt: String? = "",
    @SerializedName("created_by") val empCreatedBy: String? = ""

)