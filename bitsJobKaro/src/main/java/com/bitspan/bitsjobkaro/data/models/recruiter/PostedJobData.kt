package com.bitspan.bitsjobkaro.data.models.recruiter


import com.google.gson.annotations.SerializedName

data class PostedJobData(
    @SerializedName("adress") val adress: String? = "",
    @SerializedName("c_eng")  val cEng: String? = "",
    @SerializedName("city") val city: String? = "",
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("created_by") val createdBy: String? = "",
    @SerializedName("dayFrom") val dayFrom: String? = "",
    @SerializedName("dayTo") val dayTo: String? = "",
    @SerializedName("District") val district: String? = "",
    @SerializedName("emp_type") val empType: String? = "",
    @SerializedName("experience") val experience: String? = "",
    @SerializedName("id") val jobId: String = "",
    @SerializedName("inJobTime") val inJobTime: String? = "",
    @SerializedName("inJobTimeType") val inJobTimeType: String? = "",
    @SerializedName("jTitle") val jTitle: String = "",
    @SerializedName("jobDescription") val jobDescription: String? = "",
    @SerializedName("jobtype") val jobtype: String? = "",
    @SerializedName("nOpen") val nOpen: String? = "",
    @SerializedName("number_of_response") val numberOfResponse: String? = "",
    @SerializedName("outJobTime") val outJobTime: String? = "",
    @SerializedName("outJobTimeType") val outJobTimeType: String? = "",
    @SerializedName("quali") val quali: String? = "",
    @SerializedName("sMax") val sMax: String? = "",
    @SerializedName("sMin") val sMin: String? = "",
    @SerializedName("shift") val shift: String? = "",
    @SerializedName("skills") val skills: String? = "",
    @SerializedName("state") val state: String? = "",
    @SerializedName("status") var status: String? = "",
    @SerializedName("zip") val zip: String? = ""
)