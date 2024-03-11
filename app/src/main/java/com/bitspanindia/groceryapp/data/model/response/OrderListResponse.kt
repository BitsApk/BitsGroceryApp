package com.bitspanindia.groceryapp.data.model.response

import com.google.gson.annotations.SerializedName
data class OrderListResponse(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("nextpage")
    var nextpage: Int? = 0,
    @SerializedName("order")
    var order: List<Order>? = listOf(),
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0,
    @SerializedName("total_order_found")
    var totalOrderFound: Int? = 0
)

data class Order(
    @SerializedName("amount")
    var amount: String? = "",
    @SerializedName("cancel_reason")
    var cancelReason: String? = "",
    @SerializedName("createdate")
    var createdate: String? = "",
    @SerializedName("domain")
    var domain: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("order_id")
    var orderId: String? = "",
    @SerializedName("order_status")
    var orderStatus: String? = "",
    @SerializedName("orderimage")
    var orderimage: String? = "",
    @SerializedName("pay_mode")
    var payMode: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("viewdetail")
    var viewdetail: String? = "",
    @SerializedName("latitude")
    var latitude: String? = "",
    @SerializedName("longitude")
    var longitude: String? = "",
)