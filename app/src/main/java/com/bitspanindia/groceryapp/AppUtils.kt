package com.bitspanindia.groceryapp

import android.content.res.Resources
import androidx.fragment.app.FragmentActivity

object AppUtils {

    fun cartArrowEnable(activity: FragmentActivity, enable: Boolean) {
        (activity as MainActivity).cartArrowEnable(enable)
    }

    fun Int.toDp(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

}