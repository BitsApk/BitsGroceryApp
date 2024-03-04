package com.bitspanindia.groceryapp.data.model.request

import com.google.gson.annotations.SerializedName

data class AddAddressReq(
    @SerializedName("address_name")
    var addressName: String? = "",
    @SerializedName("city")
    var city: String? = "",
    @SerializedName("country")
    var country: String? = "",
    @SerializedName("landMark")
    var landMark: String? = "",
    @SerializedName("locality")
    var locality: String? = "",
    @SerializedName("per_add")
    var perAdd: String? = "",
    @SerializedName("phone")
    var phone: String? = "",
    @SerializedName("state")
    var state: String? = "",
    @SerializedName("user_id")
    var userId: String? = "",
    @SerializedName("zipcode")
    var zipcode: String? = "",
    @SerializedName("latitude")
    var latitude: String? = "",
    @SerializedName("longitude")
    var longitude: String? = "",
)