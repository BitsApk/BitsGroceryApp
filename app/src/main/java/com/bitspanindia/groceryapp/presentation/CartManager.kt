package com.bitspanindia.groceryapp.presentation

import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bitspanindia.groceryapp.data.model.Cart
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.storage.DataStoreManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CartManager @Inject constructor(private val dataStore: DataStoreManager) {
    private val cartName = "ORDER_CART"
    private val cartPreferenceKey = stringPreferencesKey(cartName)

    companion object{
        var cart = Cart()
    }


    suspend fun addItemToCart(product: ProductData){
        if(isCartEmpty()) {
            Log.d("Rishabh", "Inside add cart new add")
            product.count += 1
            val items = mutableListOf(product)
            cart.cartItemsMap[product.id] = items
            saveDataToCart()
        } else {
            Log.d("Rishabh", "Inside add cart update add")
            updateOrAddItemToCart(product)
        }
    }

    suspend fun decreaseCountOfItem(product: ProductData){
        val index = getProdIndex(product)
        if(index == -1) return   // No need never be empty, but only for testing purpose

        cart.cartItemsMap[product.id]!![index].count -= 1
        doCartRemoval(product, index)

        saveDataToCart()
    }

    suspend fun updateProductInCart(product: ProductData) {
        val index = getProdIndex(product)
        cart.cartItemsMap[product.id]!![index] = product
        doCartRemoval(product, index)
        saveDataToCart()
    }

    private fun doCartRemoval(product: ProductData, index: Int) {
        if (cart.cartItemsMap[product.id]!![index].count == 0) {
            if (cart.cartItemsMap.size == 1 && cart.cartItemsMap[product.id]!!.size == 1) {
                cart.cartItemsMap.clear()
            } else if (cart.cartItemsMap[product.id]!!.size == 1) {
                cart.cartItemsMap.remove(product.id)
            } else {
                cart.cartItemsMap[product.id]!!.removeAt(index)
            }
        }
    }

    private suspend fun updateOrAddItemToCart(product: ProductData) {
        if (cart.cartItemsMap[product.id] == null) {
            product.count += 1
            cart.cartItemsMap[product.id] = mutableListOf(product)
        } else {
            val index = getProdIndex(product)
            if (index == -1) {
                product.count += 1
                cart.cartItemsMap[product.id]!!.add(product)
            } else {
                cart.cartItemsMap[product.id]!![index].count += 1
            }
        }
        saveDataToCart()
    }

    private fun getProdIndex(product: ProductData): Int {
        cart.cartItemsMap[product.id]!!.forEachIndexed {ind, prod ->
            if (prod.sizeId == product.sizeId) return ind
        }
        return -1
    }


    private suspend fun saveDataToCart() {
        Log.d("Rishabh", "CM cart before save ${cart.cartItemsMap}")
        val cartJSON = Gson().toJson(cart).toString()
        dataStore.put(cartPreferenceKey, cartJSON)
    }

    suspend fun getSavedCartData(): Cart {
        val savedCart = dataStore.get(cartPreferenceKey, "").first()
        Log.d("Rishabh", "CM Inside get saved cart")
        if(savedCart.isEmpty()) return Cart()

        return Gson().fromJson(savedCart, Cart::class.java)
    }



    private fun isCartEmpty(): Boolean {
        return cart.cartItemsMap.isEmpty()
    }

    fun getCart(): Cart{
        return cart
    }

    fun setCart(cartt: Cart) {
        cart = cartt
    }

    private fun findIndexOfItemInCart(item: ProductData): Int {
        var savedItemIndex = -1
        cart.cartItemsMap[item.id]?.forEachIndexed { index, cartItem ->
            if (cartItem === item)
                savedItemIndex = index
        }
        return savedItemIndex
    }

    suspend fun clearCart(){
        dataStore.delete(cartPreferenceKey)
        cart = Cart()
    }


}
