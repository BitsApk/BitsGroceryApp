package com.bitspanindia.groceryapp.data.model

import com.google.gson.annotations.SerializedName

data class SearchProductResponse(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0,
    @SerializedName("total product Found")
    var totalProductFound: Int? = 0,
    @SerializedName("nextpage")
    var nextPage: Int? = 0,
    @SerializedName("search_product")
    var searchProduct: List<ProductData>? = listOf(),
)