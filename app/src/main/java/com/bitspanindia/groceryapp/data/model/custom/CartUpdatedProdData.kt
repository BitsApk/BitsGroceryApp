package com.bitspanindia.groceryapp.data.model.custom


import com.google.gson.annotations.SerializedName

data class CartUpdatedProdData(
    @SerializedName("id") val id: String = "",
    @SerializedName("image") val image: String? = "",
    @SerializedName("product_name") val productName: String? = "",
    @SerializedName("rating") val rating: String? = "",
    @SerializedName("weight") val weight: String? = "",
    @SerializedName("discounted_price") var discountedPrice: Double? = 0.0,
    @SerializedName("sizeid") val sizeId: String = "-1",
    var stockChange: Pair<Int, Int>? = null
)