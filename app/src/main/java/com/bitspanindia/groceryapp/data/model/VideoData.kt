package com.bitspanindia.groceryapp.data.model


import com.google.gson.annotations.SerializedName

data class VideoData(
    @SerializedName("discount") val discount: String? = "",
    @SerializedName("discounted_price") val discountedPrice: Int? = 0,
    @SerializedName("id") val id: String? = "",
    @SerializedName("image") val image: String? = "",
    @SerializedName("price") val price: String? = "",
    @SerializedName("product_name") val productName: String? = "",
    @SerializedName("rating") val rating: String? = "",
    @SerializedName("stock") val stock: String? = "",
    @SerializedName("video") val video: String? = ""
)