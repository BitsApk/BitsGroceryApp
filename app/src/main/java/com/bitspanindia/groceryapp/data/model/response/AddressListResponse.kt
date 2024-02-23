package com.bitspanindia.groceryapp.data.model.response

import com.google.gson.annotations.SerializedName
data class AddressListResponse(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("my_address")
    var myAddress: List<MyAddress>? = listOf(),
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0
)

data class MyAddress(
    @SerializedName("address_name")
    var addressName: String? = "",
    @SerializedName("city")
    var city: String? = "",
    @SerializedName("country")
    var country: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("landMark")
    var landMark: String? = "",
    @SerializedName("locality")
    var locality: String? = "",
    @SerializedName("permanent_add")
    var permanentAdd: String? = "",
    @SerializedName("phone")
    var phone: String? = "",
    @SerializedName("zipcode")
    var zipcode: String? = ""
)