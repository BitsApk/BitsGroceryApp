package com.bitspan.bitsjobkaro.data.models.recruiter

import com.google.gson.annotations.SerializedName

data class PostAboutCompanyData(
    @SerializedName("adress") var adress: String? = "",
    @SerializedName("c_desc") var cDesc: String? = "",
    @SerializedName("c_gst") var cGst: String? = "",
    @SerializedName("c_gst_old") val cGstOld: String? = "",
    @SerializedName("c_logo_old") val cLogoOld: String? = "",
    @SerializedName("city") var city: String? = "",
    @SerializedName("com_type") var comType: String? = "",
    @SerializedName("District") var district: String? = "",
    @SerializedName("email") var email: String? = "",
    @SerializedName("formId") var formId: String? = null,
    var documents11: String? = null,
    var cLogo11: String? = null,
    var docType: String? = null,
    @SerializedName("link") var link: String? = "",
    @SerializedName("n_emp") var nEmp: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("number") var number: String? = "",
    @SerializedName("r_desig") var rDesig: String? = "",
    @SerializedName("r_mobile") var rMobile: String? = "",
    @SerializedName("r_name") var rName: String? = "",
    @SerializedName("rec_id") var recId: String? = "",
    @SerializedName("state") var state: String? = "",
    @SerializedName("zip") var zip: String? = "",
    @SerializedName("created_at") var createdAt: String? = "",
    @SerializedName("created_by") var createdBy: String? = ""
) {
    override fun toString(): String {
        return "PostAboutCompanyData(adress=$adress, cDesc=$cDesc, cGst=$cGst, city=$city, comType=$comType, district=$district, email=$email, formId=$formId, link=$link, nEmp=$nEmp, name=$name, number=$number, rDesig=$rDesig, rMobile=$rMobile, rName=$rName, recId=$recId, state=$state, zip=$zip)"
    }
}