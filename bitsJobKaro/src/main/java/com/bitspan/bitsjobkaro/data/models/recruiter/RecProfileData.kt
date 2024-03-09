package com.bitspan.bitsjobkaro.data.models.recruiter


import com.google.gson.annotations.SerializedName

data class RecProfileData(
    @SerializedName("adress") val adress: String? = "NA",
    @SerializedName("c_desc") val cDesc: String? = "",
    @SerializedName("c_gst") val cGst: String? = "NA",
    @SerializedName("c_logo") val cLogo: String? = "",
    @SerializedName("city") val city: String? = "NA",
    @SerializedName("com_type") val comType: String? = "",
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("created_by") val createdBy: String? = "",
    @SerializedName("District") val district: String? = "NA",
    @SerializedName("documents") val documents: String? = "",
    @SerializedName("doc_type") val docType: String? = "NA",
    @SerializedName("email") val email: String? = "NA",
    @SerializedName("id") val id: String? = "",
    @SerializedName("link") val link: String? = "NA",
    @SerializedName("n_emp") val nEmp: String? = "NA",
    @SerializedName("name") val name: String? = "NA",
    @SerializedName("number") val number: String? = "NA",
    @SerializedName("r_desig") val rDesig: String? = "NA",
    @SerializedName("r_mobile") val rMobile: String? = "NA",
    @SerializedName("r_name") val rName: String? = "NA",
    @SerializedName("state") val state: String? = "NA",
    @SerializedName("status") val status: String? = "NA",
    @SerializedName("zip") val zip: String? = "NA"
)