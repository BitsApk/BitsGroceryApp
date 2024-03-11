package com.bitspanindia.groceryapp.data.model.response

import com.google.gson.annotations.SerializedName

data class CheckLocalityResponse(
    @SerializedName("MatchSELLER")
    var matchSELLER: MatchSeller? = MatchSeller(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0
)

data class MatchSeller(
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("Sellerid")
    var sellerid: String? = ""
)