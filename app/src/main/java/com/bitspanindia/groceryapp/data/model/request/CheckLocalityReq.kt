package com.bitspanindia.groceryapp.data.model.request

import com.google.gson.annotations.SerializedName

data class CheckLocalityReq(
    @SerializedName("latitude")
    var latitude: Double? = 0.0,
    @SerializedName("longitude")
    var longitude: Double? = 0.0
)