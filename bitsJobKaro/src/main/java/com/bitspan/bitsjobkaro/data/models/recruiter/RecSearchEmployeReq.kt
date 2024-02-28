package com.bitspan.bitsjobkaro.data.models.recruiter


import com.google.gson.annotations.SerializedName

data class RecSearchEmployeReq(
    @SerializedName("age") var age: String? = "",
    @SerializedName("eTYpe") var eTYpe: String? = "",
    @SerializedName("education") var education: String? = "",
    @SerializedName("english") var english: String? = "",
    @SerializedName("exp") var exp: String? = "",
    @SerializedName("j_profile") var jProfile: String? = "",
    @SerializedName("jType") var jType: String? = "",
    @SerializedName("pageno") var pageno: Int? = 1,
    @SerializedName("rec_id") var recId: Int? = 0,
    @SerializedName("search_jobTitle") var searchJobTitle: String? = "",
    @SerializedName("skills") var skills: String? = ""
)