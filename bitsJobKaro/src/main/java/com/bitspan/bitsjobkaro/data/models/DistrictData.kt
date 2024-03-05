package com.bitspan.bitsjobkaro.data.models


import com.google.gson.annotations.SerializedName

data class DistrictData(
    @SerializedName("district_description") val districtDescription: String? = "",
    @SerializedName("district_status") val districtStatus: String? = "",
    @SerializedName("district_title") val districtTitle: String? = "",
    @SerializedName("districtid") val districtid: String? = "",
    @SerializedName("state_id") val stateId: String? = ""
)