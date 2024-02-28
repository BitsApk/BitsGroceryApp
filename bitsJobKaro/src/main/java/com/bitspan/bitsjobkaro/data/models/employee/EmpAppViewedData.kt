package com.bitspan.bitsjobkaro.data.models.employee

import com.google.gson.annotations.SerializedName

data class EmpAppViewedData(
    @SerializedName("id") val jobViewedId: String?,
    @SerializedName("jTitle") val jobViewedTitle: String?,
    @SerializedName("nOpen") val noOfViewedOpenings: String?,
    @SerializedName("state") val jobViewedState: String?,
    @SerializedName("District") val jobViewedDistrict: String?,
    @SerializedName("city") val jobViewedCity: String?,
    @SerializedName("zip") val jobViewedZip: String?,
    @SerializedName("adress") val jobViewedAddress: String?,
    @SerializedName("emp_type") val jobViewedEmpType: String?,
    @SerializedName("jobtype") val jobViewedJobType: String?,
    @SerializedName("shift") val jobViewedShift: String?,
    @SerializedName("experience") val jobViewedExperience: String?,
    @SerializedName("quali") val jobViewedQualification: String?,
    @SerializedName("c_eng") val jobViewedEnglishLabel: String?,
    @SerializedName("sMin") val jobViewedExMinSalary: String?,
    @SerializedName("sMax") val jobViewedExMaxSalary: String?,
    @SerializedName("jobDescription") val jobViewedDescription: String?,
    @SerializedName("skills") val jobViewedSkills: String?,
    @SerializedName("inJobTime") val jobViewedInTime: String?,
    @SerializedName("inJobTimeType") val jobViewedInTimeType: String?,
    @SerializedName("outJobTime") val jobViewedOutTime: String?,
    @SerializedName("outJobTimeType") val jobViewedOutTimeType: String?,
    @SerializedName("dayFrom") val jobViewedDayFrom: String?,
    @SerializedName("dayTo") val jobViewedDayTo: String?,
    @SerializedName("status") val jobViewedStatus: String?,
    @SerializedName("created_at") val jobViewedCreatedAt: String?,
    @SerializedName("created_by") val jobViewedCreatedBy: String?,
)