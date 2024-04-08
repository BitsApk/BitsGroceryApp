package com.bitspanindia.groceryapp.data.model.response

import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(
    @SerializedName("addedon") var addedon: String? = "",
    @SerializedName("address") var address: String? = "",
    @SerializedName("amount") var amount: String? = "",
    @SerializedName("conv_charge") var convCharge: String? = "",
    @SerializedName("coupon_discount") var couponDiscount: String? = "",
    @SerializedName("couponamt") var couponamt: String? = "",
    @SerializedName("couponcode") var couponcode: String? = "",
    @SerializedName("createddate") var createddate: String? = "",
    @SerializedName("delevary_charge") var delevaryCharge: String? = "",
    @SerializedName("delivercancel_date") var delivercancelDate: String? = "",
    @SerializedName("email") var email: String? = "",
    @SerializedName("message") var message: String? = "",
    @SerializedName("order_id") var orderId: String? = "",
    @SerializedName("order_status") var orderStatus: String? = "",
    @SerializedName("orderstatus") var orderStatusMess: String? = "",
    @SerializedName("pay_mode") var payMode: String? = "",
    @SerializedName("phone") var phone: String? = "",
    @SerializedName("preamount") var preamount: String? = "",
    @SerializedName("latitude") var latitude: String? = "",
    @SerializedName("longitude") var longitude: String? = "",
    @SerializedName("product_order") var productOrder: List<ProductOrder>? = listOf(),
    @SerializedName("status") var status: String? = "",
    @SerializedName("statusCode") var statusCode: Int? = 0
)

data class ProductOrder(
    @SerializedName("discount") var discount: String? = "",
    @SerializedName("image") var image: String? = "",
    @SerializedName("discounted_price") var discountedPrice: String? = "",
    @SerializedName("product_id") var productId: String? = "",
    @SerializedName("product_name") var productName: String? = "",
    @SerializedName("product_price") var productPrice: String? = "",
    @SerializedName("product_qty") var productQty: String? = "",
    @SerializedName("total") var total: Double? = 0.0
)