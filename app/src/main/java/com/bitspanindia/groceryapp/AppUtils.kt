package com.bitspanindia.groceryapp

import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.facebook.shimmer.ShimmerFrameLayout
import java.io.IOException
import java.util.Locale
import java.util.regex.Pattern

object AppUtils {

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

    fun isValidPinCode(pinCode: String): Boolean {
        val pinCodePattern = Pattern.compile("^[1-9]{1}[0-9]{2}\\s{0, 1}[0-9]{3}$")
        return pinCodePattern.matcher(pinCode).matches()
    }
    fun cartArrowEnable(activity: FragmentActivity, enable: Boolean) {
        (activity as MainActivity).cartArrowEnable(enable)
    }

    fun Int.toDp(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun adjustItemWidth(designType:Int,item:ConstraintLayout) {
        val layoutParams = item.layoutParams
        layoutParams.width =  if (designType == 5) ConstraintLayout.LayoutParams.MATCH_PARENT else 120.toDp() //5 for grid item
        item.layoutParams = layoutParams
    }

    fun showShortToast(context:Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    fun cartLayoutVisibility(activity: FragmentActivity,visibility: Int){
        try {
            (activity as MainActivity).bottomCartVisibility(visibility)
        } catch (e: Exception) {
        }
    }

    fun startShimmer(shimmer: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
    fun stopShimmer(shimmer: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    fun getAddressFromLocation(context: Context,latitude: Double, longitude: Double):android.location.Address {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty()==true) {
              return addresses[0]
            }
        } catch (e: IOException) {
            Log.e("Geocoder", "Error getting address: ${e.message}")
        }
        return android.location.Address(Locale(""))
    }

}