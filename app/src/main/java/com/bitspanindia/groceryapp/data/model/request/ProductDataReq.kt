package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class ProductDataReq(
    @SerializedName("pageno")
    var pageno: Int? = 0,
    @SerializedName("subcategory_id")
    var subcategoryId: String? = "",
    @SerializedName("user_id")
    var userId: String? = "",


    //for search product
    @SerializedName("product_name")
    var productName: String? = "",
)