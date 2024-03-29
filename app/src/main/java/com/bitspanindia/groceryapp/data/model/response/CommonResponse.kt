package com.bitspanindia.groceryapp.data.model.response

import com.google.gson.annotations.SerializedName
data class CommonResponse(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0,

    //for getting add address response

    @SerializedName("my_address")
    var myAddress: List<MyAddress>? = listOf(),
)