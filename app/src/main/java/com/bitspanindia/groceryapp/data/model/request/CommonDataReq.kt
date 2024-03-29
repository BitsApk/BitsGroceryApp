package com.bitspanindia.groceryapp.data.model.request

import com.google.gson.annotations.SerializedName

data class CommonDataReq(
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("added_by_web") var addedByWeb: String? = null,

    @SerializedName("product_name") var productName: String? = null,

    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("seller_id") var sellerId: String? = null,
    @SerializedName("seller_auto_id") var sellerAutoId: String? = null,

    //for product-page api
    @SerializedName("product_id") var productId: String? = null,

    //for remove-address api
    @SerializedName("address_id") var addressId: String? = null,

    //for orderDetail api
    @SerializedName("order_id") var orderId: String? = null,

    @SerializedName("total_cart_amount") var totalCartAmount: Double? = null,


    )