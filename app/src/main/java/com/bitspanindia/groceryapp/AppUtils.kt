package com.bitspanindia.groceryapp

import android.content.res.Resources
import androidx.fragment.app.FragmentActivity
import java.util.regex.Pattern

object AppUtils {

    fun cartArrowEnable(activity: FragmentActivity, enable: Boolean) {
        (activity as MainActivity).cartArrowEnable(enable)
    }

    fun Int.toDp(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun isValidEmail(email: String): Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    fun isValidNum(mobile: String): Boolean {
        val PHONE_NUMBER = Pattern.compile(
            "[0-9]{10}"
        )
        return PHONE_NUMBER.matcher(mobile).matches()
    }

}