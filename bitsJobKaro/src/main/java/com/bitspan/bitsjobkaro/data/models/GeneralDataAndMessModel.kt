package com.bitspan.bitsjobkaro.data.models

import com.google.gson.annotations.SerializedName

data class GeneralDataAndMessModel <T> (
    @SerializedName("status") val status: Int,
    @SerializedName("message") val mess: String?,
    @SerializedName("img_path") val imgPath: String?,
    @SerializedName("data") val data: T?
)