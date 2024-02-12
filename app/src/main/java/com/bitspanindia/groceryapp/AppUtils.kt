package com.bitspanindia.groceryapp

import androidx.fragment.app.FragmentActivity

object AppUtils {

    fun showCart(activity: FragmentActivity, totalCount: Int) {
        (activity as MainActivity).showCart(totalCount)
    }

}