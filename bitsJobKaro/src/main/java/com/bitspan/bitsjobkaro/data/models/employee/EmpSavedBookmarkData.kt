package com.bitspan.bitsjobkaro.data.models.employee


import com.google.gson.annotations.SerializedName

data class EmpSavedBookmarkData(
    @SerializedName("id") val id: String? = "",
    @SerializedName("title") val title: String? = ""
)