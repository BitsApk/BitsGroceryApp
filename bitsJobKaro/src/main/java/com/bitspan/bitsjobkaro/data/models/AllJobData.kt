package com.bitspan.bitsjobkaro.data.models

import com.google.gson.annotations.SerializedName

data class AllJobData(
    @SerializedName("id") val jobId: String?,
    @SerializedName("jTitle") val jobTitle: String?,
    @SerializedName("nOpen") val noOfOpenings: String?,
    @SerializedName("state") val jobState: String?,
    @SerializedName("District") val jobDistrict: String?,
    @SerializedName("city") val jobCity: String?,
    @SerializedName("zip") val jobZip: String?,
    @SerializedName("adress") val jobAddress: String?,
    @SerializedName("emp_type") val jobEmpType: String?,
    @SerializedName("jobtype") val jobJobType: String?,
    @SerializedName("shift") val jobShift: String?,
    @SerializedName("experience") val jobExperience: String?,
    @SerializedName("quali") val jobQualification: String?,
    @SerializedName("c_eng") val jobEnglishLabel: String?,
    @SerializedName("sMin") val jobExMinSalary: String?,
    @SerializedName("sMax") val jobExMaxSalary: String?,
    @SerializedName("jobDescription") val jobDescription: String?,
    @SerializedName("skills") val jobSkills: String?,
    @SerializedName("inJobTime") val jobInTime: String?,
    @SerializedName("inJobTimeType") val jobInTimeType: String?,
    @SerializedName("outJobTime") val jobOutTime: String?,
    @SerializedName("outJobTimeType") val jobOutTimeType: String?,
    @SerializedName("dayFrom") val jobDayFrom: String?,
    @SerializedName("dayTo") val jobDayTo: String?,
    @SerializedName("status") val jobStatus: String?,
    @SerializedName("created_at") val jobCreatedAt: String?,
    @SerializedName("created_by") val jobCreatedBy: String?,
    var isShortListed: Boolean = false
    )
