package com.bitspanindia.groceryapp.data.model.request

import com.google.gson.annotations.SerializedName

data class CommonDataReq(
    @SerializedName("category_id")
    val categoryId: String? = "",


    @SerializedName("product_name")
    var productName: String? = "",

    @SerializedName("user_id")
    var userId: String? = "",

    //for product-page api
    @SerializedName("product_id")
    var productId: String? = "",

    //for remove-address api
    @SerializedName("address_id")
    var addressId: String? = "",

    //for orderDetail api
    @SerializedName("order_id")
    var orderId: String? = "",


    )