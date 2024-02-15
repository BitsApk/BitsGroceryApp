package com.bitspanindia.groceryapp.data.model

import com.google.gson.annotations.SerializedName

data class CategoryData(
    @SerializedName("category") val category: MutableList<Category>? = mutableListOf(),
    @SerializedName("id") val id: String? = "",
    @SerializedName("maincat_banner") val maincatBanner: String? = "",
    @SerializedName("maincat_image") val maincatImage: String? = "",
    @SerializedName("maincat_name") val maincatName: String? = ""
)
data class Category(
    @SerializedName("banner_cat") val bannerCat: String? = "",
    @SerializedName("cat_id") val catId: String? = "",
    @SerializedName("cat_image") val catImage: String? = "",
    @SerializedName("cat_name") val catName: String? = "",
    @SerializedName("service_id") val serviceId: String? = ""
)