package com.bitspanindia.groceryapp.data.model

import com.google.gson.annotations.SerializedName

data class SubCategoryData (
    @SerializedName("status") val status: String? = "",
    @SerializedName("message") val message: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("subcategory") val subCategory: List<SubCategory>? = listOf(),
    )

data class SubCategory(
    @SerializedName("id") val id: String? = "",
    @SerializedName("subcat_name") val subCatName: String? = "",
    @SerializedName("subcat_image") val subCatImage: String? = "",
    @SerializedName("cat_banner") val catBanner: String? = ""
)