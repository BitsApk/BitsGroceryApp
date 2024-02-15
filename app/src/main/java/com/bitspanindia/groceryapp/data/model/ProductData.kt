package com.bitspanindia.groceryapp.data.model


import com.google.gson.annotations.SerializedName

data class ProductData(
    @SerializedName("discount") val discount: String? = "",
    @SerializedName("discounted_price") val discountedPrice: Double? = 0.0,
    @SerializedName("id") val id: String = "",
    @SerializedName("image") val image: String? = "",
    @SerializedName("price") val price: String? = "",
    @SerializedName("product_name") val productName: String? = "",
    @SerializedName("rating") val rating: String? = "",
    @SerializedName("stock") val stock: String? = "",

    @SerializedName("product_image") val productImage: String? = "",
    @SerializedName("weight") val weight: String? = "",
    @SerializedName("sizeid") val sizeId: String? = "",
    @SerializedName("returnable") val returnable: String? = "",
    @SerializedName("wishlist_exist") val wishlistExist: String? = "",
    var count: Int = 0
)