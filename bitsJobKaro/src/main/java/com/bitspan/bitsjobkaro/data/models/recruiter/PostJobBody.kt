package com.bitspan.bitsjobkaro.data.models.recruiter

import com.google.gson.annotations.SerializedName

data class PostJobBody(
    @SerializedName("adress")
    var adress: String? = "",
    @SerializedName("c_eng")
    var cEng: String? = "",
    @SerializedName("city")
    var city: String? = "",
    @SerializedName("dayFrom")
    var dayFrom: String? = "",
    @SerializedName("dayTo")
    var dayTo: String? = "",
    @SerializedName("District")
    var district: String? = "",
    @SerializedName("emp_type")
    var empType: String? = "",
    @SerializedName("experience")
    var experience: String? = "",
    @SerializedName("formId")
    var formId: String? = null,
    @SerializedName("jTitle")
    var jTitle: String? = "",
    @SerializedName("jobDescription")
    var jobDescription: String? = "",
    @SerializedName("jobtype")
    var jobtype: String? = "",
    @SerializedName("nOpen")
    var nOpen: String? = "",
    @SerializedName("quali")
    var quali: String? = "",
    @SerializedName("rec_id")
    var recId: String? = "",
    @SerializedName("sMax")
    var sMax: String? = "",
    @SerializedName("sMin")
    var sMin: String? = "",
    @SerializedName("shift")
    var shift: String? = "",
    @SerializedName("skills")
    var skills: String? = "",
    @SerializedName("state")
    var state: String? = "",
    @SerializedName("zip")
    var zip: String? = ""
) {
    override fun toString(): String {
        return "PostJobBody(adress=$adress, cEng=$cEng, city=$city, dayFrom=$dayFrom, dayTo=$dayTo, district=$district, empType=$empType, experience=$experience, formId=$formId, jTitle=$jTitle, jobDescription=$jobDescription, jobtype=$jobtype, nOpen=$nOpen, quali=$quali, recId=$recId, sMax=$sMax, sMin=$sMin, shift=$shift, skills=$skills, state=$state, zip=$zip)"
    }
}