package com.bitspanindia.groceryapp.data.model


import com.google.gson.annotations.SerializedName

data class ProductData(
    @SerializedName("discount") var discount: String? = "",
    @SerializedName("discounted_price") var discountedPrice: Double? = 0.0,
    @SerializedName("id") val id: String = "",
    @SerializedName("image") val image: String? = "",
    @SerializedName("price") val price: String? = "",
    @SerializedName("product_name") val productName: String? = "",
    @SerializedName("rating") val rating: String? = "",
    @SerializedName("stock") var stock: String? = "",

    @SerializedName("weight") val weight: String? = "",
    @SerializedName("sizeid") val sizeId: String = "-1",
    @SerializedName("returnable") val returnable: String? = "",
    @SerializedName("wishlist_exist") val wishlistExist: String? = "",
    var count: Int = 0,
    var priceChange: Pair<Double, Double>? = null
)