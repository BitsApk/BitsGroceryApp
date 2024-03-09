package com.bitspan.bitsjobkaro.data.models

import com.google.gson.annotations.SerializedName


data class StateData(
    @SerializedName("state_description") val stateDescription: String? = "",
    @SerializedName("state_id") val stateId: String? = "",
    @SerializedName("state_title") val stateTitle: String? = "",
    @SerializedName("status") val status: String? = ""
)