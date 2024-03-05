package com.bitspan.bitsjobkaro.data.models

import com.google.gson.annotations.SerializedName

data class GeneralDataModel<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: T
)