package com.bitspanindia.groceryapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitspanindia.groceryapp.data.model.Cart
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.presentation.CartManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartManageViewModel @Inject constructor(private val cartManager: CartManager) : ViewModel() {


    val countMap: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()
    // Temporary create for counting to show, it contains data like [prodId, [sizeId: count, sizeId: count, "-1": total]]
    // each item have a entry of -1 sizeId that contains sum of them

    var _cartTotalItem = MutableLiveData<Int>(0) // Temporary create for holding cart products
    val cartTotalItem: LiveData<Int>
        get() = _cartTotalItem


    var isCartVisible: Boolean = false

    fun setCartTotal(total: Int) {
        _cartTotalItem.postValue(total)
    }

    suspend fun getSavedCart(): Cart {
        return cartManager.getSavedCartData()
    }

    fun getCart(): Cart {
        return cartManager.getCart()
    }

    fun setCart(cart: Cart) {
        cartManager.setCart(cart)
    }

    fun clearCart() {
        viewModelScope.launch {
            cartManager.clearCart()
        }
    }

    fun getCartList(): MutableList<ProductData> {
        val list = mutableListOf<ProductData>()
        val cart = getCart()
        for (item in cart.cartItemsMap) {
            list.addAll(cart.cartItemsMap[item.key] ?: listOf())
        }
        return list
    }
    fun addItemToCart(product: ProductData) {
        viewModelScope.launch {
            cartManager.addItemToCart(product)
        }
    }

    fun decreaseCountOfItem(product: ProductData) {
        viewModelScope.launch {
            cartManager.decreaseCountOfItem(product)
        }
    }

    fun updateProductInCart(product: ProductData) {
        viewModelScope.launch {
            cartManager.updateProductInCart(product)
        }
    }
}