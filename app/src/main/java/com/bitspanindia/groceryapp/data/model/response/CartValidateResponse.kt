package com.bitspanindia.groceryapp.data.model.response


import com.google.gson.annotations.SerializedName

data class CartValidateResponse(
    @SerializedName("list") val list: List<CartProdBackendData>? = listOf(),
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0
)


data class CartProdBackendData(
    @SerializedName("admin_profit") val adminProfit: String? = "",
    @SerializedName("discount") val discount: Double? = 0.0,
    @SerializedName("discounted_price") val discountedPrice: Double? = 0.0,
    @SerializedName("id") val id: String? = "",
    @SerializedName("image") val image: String? = "",
    @SerializedName("is_outofstock") val isOutofstock: Int? = 0,
    @SerializedName("netprice") val netprice: Double? = 0.0,
    @SerializedName("old_qty") val oldQty: Int? = 0,
    @SerializedName("price") val price: String? = "",
    @SerializedName("product_name") val productName: String? = "",
    @SerializedName("qty") val qty: Int? = 0,
    @SerializedName("seller_auto_id") val sellerAutoId: Int? = 0,
    @SerializedName("seller_credit_amount") val sellerCreditAmount: Double? = 0.0,
    @SerializedName("seller_id") val sellerId: String? = "",
    @SerializedName("sizeid") val sizeid: String? = "",
    @SerializedName("stock") val stock: String? = "",
    @SerializedName("weight") val weight: String? = ""
)