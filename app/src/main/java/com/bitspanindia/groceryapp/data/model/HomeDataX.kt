package com.bitspanindia.groceryapp.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

data class HomeDataX(
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("ViewtypeList") val viewtypeList: List<Viewtype>? = listOf()
)


data class Viewtype(
    @SerializedName("data") val data: List<LinkedTreeMap<String, Any>> = listOf(),
    @SerializedName("datatype") val datatype: String? = "",
    @SerializedName("title") val title: String? = "",
    @SerializedName("designtype") val designType: String? = "",
    @SerializedName("viewtype") val viewtype: String? = ""
)

