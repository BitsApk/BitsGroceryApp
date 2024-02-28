package com.bitspan.bitsjobkaro.data.models.recruiter


import com.google.gson.annotations.SerializedName

data class AllShortlistedEmpResponse(
    @SerializedName("new_view") val shortList: MutableList<ShortListData>? = mutableListOf(),
    @SerializedName("status") val status: Int? = 0
)


data class ShortListData(
    @SerializedName("c_name") val cName: String? = "",
    @SerializedName("id") val id: String? = ""
)