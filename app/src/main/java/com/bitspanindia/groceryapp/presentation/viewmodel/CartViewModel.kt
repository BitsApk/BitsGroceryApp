package com.bitspanindia.groceryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitspanindia.groceryapp.data.model.Cart
import com.bitspanindia.groceryapp.data.model.HomeDataX
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.repositories.HomeRepository
import com.bitspanindia.groceryapp.presentation.CartManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartManager: CartManager) : ViewModel() {


    val countMap: MutableMap<String, Int> = mutableMapOf()  // Temporary create for counting to show

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
}