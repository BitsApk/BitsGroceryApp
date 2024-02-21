package com.bitspanindia.groceryapp.data.model.response

import com.bitspanindia.groceryapp.data.model.ProductData
import com.google.gson.annotations.SerializedName

data class GetProductDetailsResponse(
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("multiimg")
    var multiimg: List<MultiImg>? = listOf(),
    @SerializedName("multiweight")
    var multiweight: List<MultiWeight>? = listOf(),
    @SerializedName("related")
    var related: MutableList<ProductData>? = mutableListOf(),
    @SerializedName("product_name")
    var productName: String? = "",
    @SerializedName("rating")
    var rating: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0,
    @SerializedName("stock")
    var stock: String? = ""
)

data class MultiImg(
    @SerializedName("image")
    var image: String? = ""
)

data class MultiWeight(
    @SerializedName("discount")
    var discount: String? = "",
    @SerializedName("discounted_price")
    var discountedPrice: Double? = 0.0,
    @SerializedName("price")
    var price: String? = "",
    @SerializedName("weight")
    var weight: String? = ""
)