package com.bitspanindia.groceryapp

import android.content.res.Resources
import androidx.fragment.app.FragmentActivity

object AppUtils {

//    fun showCart(activity: FragmentActivity, totalCount: Int) {
//        (activity as MainActivity).showCart(totalCount)
//    }

    fun Int.toDp(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

}