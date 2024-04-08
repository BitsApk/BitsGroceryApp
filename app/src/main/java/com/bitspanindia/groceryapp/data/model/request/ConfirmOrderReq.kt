package com.bitspanindia.groceryapp.data.model.request


import com.google.gson.annotations.SerializedName

data class ConfirmOrderReq(
    @SerializedName("added_by_web") val addedByWeb: String? = "",
    @SerializedName("addressData") val addressData: List<AddressData>? = listOf(),
    @SerializedName("address_id") val addressId: Int? = 0,
    @SerializedName("cart") val cart: List<Cart>? = listOf(),
    @SerializedName("conv_charge") val convCharge: Double? = 0.0,
    @SerializedName("coupon_amount") val couponAmount: String? = "",
    @SerializedName("coupon_code") val couponCode: String? = "",
    @SerializedName("coupon_discount") val couponDiscount: String? = "",
    @SerializedName("coupon_id") val couponId: String? = "",
    @SerializedName("delivery_charge") val deliveryCharge: Double? = 0.0,
    @SerializedName("email") val email: String? = "",
    @SerializedName("grand_total") val grandTotal: Double? = 0.0,
    @SerializedName("order_id") val orderId: String? = "",
    @SerializedName("payment_mode") val paymentMode: String? = "",
    @SerializedName("phone") val phone: String? = "",
    @SerializedName("seller_auto_id") val sellerAutoId: Int? = 0,
    @SerializedName("seller_id") val sellerId: String? = "",
    @SerializedName("slotdelivery_date") val slotdeliveryDate: String? = "",
    @SerializedName("slotdelivery_time") val slotdeliveryTime: String? = "",
    @SerializedName("total_amount") val totalAmount: Double? = 0.0,
    @SerializedName("trans_id") val transId: String? = "",
    @SerializedName("txn_amount") val txnAmount: Double? = 0.0,
    @SerializedName("user_id") val userId: Int? = 0
)


data class Cart(
    @SerializedName("discount") val discount: Double? = 0.0,
    @SerializedName("netprice") val netprice: Double? = 0.0,
    @SerializedName("price") val price: Double? = 0.0,
    @SerializedName("product_id") val productId: Int? = 0,
    @SerializedName("product_image") val productImage: String? = "",
    @SerializedName("product_name") val productName: String? = "",
    @SerializedName("qty") val qty: Int? = 0,
    @SerializedName("size_id") val sizeId: Int? = 0,
    @SerializedName("total_amount") val totalAmount: Double? = 0.0,
    @SerializedName("weight") val weight: String? = "",
    @SerializedName("seller_credit_amount") val sellerCreditAmount: Double? = 0.0,
    @SerializedName("seller_auto_id") val sellerAutoId: Int? = 0,
    @SerializedName("seller_id") val sellerId: String? = "",
    @SerializedName("admin_profit") val adminProfit: String? = ""
)


data class AddressData(
    @SerializedName("address_name") val addressName: String? = "",
    @SerializedName("city") val city: String? = "",
    @SerializedName("country") val country: String? = "",
    @SerializedName("district") val district: String? = "",
    @SerializedName("landMark") val landMark: String? = "",
    @SerializedName("locality") val locality: String? = "",
    @SerializedName("per_add") val perAdd: String? = "",
    @SerializedName("phone") val phone: String? = "",
    @SerializedName("state") val state: String? = "",
    @SerializedName("user_id") val userId: Int? = 0,
    @SerializedName("zipcode") val zipcode: String? = ""
)