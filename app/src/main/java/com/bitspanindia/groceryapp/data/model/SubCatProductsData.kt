package com.bitspanindia.groceryapp.data.model

import com.google.gson.annotations.SerializedName

data class SubCatProductsData (
    @SerializedName("status") val status: String? = "",
    @SerializedName("message") val message: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("nextpage") val nextPage: Int? = 0,
    @SerializedName("product") val products: List<ProductData>? = listOf(),
    )