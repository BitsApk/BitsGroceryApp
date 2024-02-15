package com.bitspanindia.groceryapp.data.model


import com.google.gson.annotations.SerializedName

data class BannerData(
    val banner_name: String? = "",
    val appbanner_image: String? = "",
    val heading_1: String? = "",
    val heading_2: String? = "",
    val heading_3: String? = "",
    val link: String? = "",
    val category_id: String? = "",
)