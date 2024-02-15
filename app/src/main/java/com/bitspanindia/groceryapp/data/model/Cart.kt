package com.bitspanindia.groceryapp.data.model

data class Cart(

    val cartItemsMap: MutableMap<String, MutableList<ProductData>> = mutableMapOf()
)
