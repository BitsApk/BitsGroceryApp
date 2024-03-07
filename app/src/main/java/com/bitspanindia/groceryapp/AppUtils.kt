package com.bitspanindia.groceryapp

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.res.Resources
import android.location.Geocoder
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bitspanindia.groceryapp.ui.map.MapFragment
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.Locale
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

    fun isValidPinCode(pinCode: String): Boolean {
        val pinCodePattern = Pattern.compile("^[1-9]{1}[0-9]{2}\\s{0, 1}[0-9]{3}$")
        return pinCodePattern.matcher(pinCode).matches()
    }

    fun isValidNum(mobile: String): Boolean {
        val PHONE_NUMBER = Pattern.compile(
            "[0-9]{10}"
        )
        return PHONE_NUMBER.matcher(mobile).matches()
    }

    fun adjustItemWidth(designType:Int,item:ConstraintLayout) {
        val layoutParams = item.layoutParams
        layoutParams.width =  if (designType == 5) ConstraintLayout.LayoutParams.MATCH_PARENT else 120.toDp() //5 for grid item
        item.layoutParams = layoutParams
    }

    fun showShortToast(context:Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    fun cartLayoutVisibility(activity: FragmentActivity,visibility: Int) {
        try {
            (activity as MainActivity).bottomCartVisibility(visibility)
        } catch (_: Exception) {
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

    fun gpsPermission(context: Context,activity: FragmentActivity,callback:()->Any) {
        val REQUEST_CHECK_SETTINGS = 132

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // Location settings are satisfied, the client can initialize location requests here
            callback()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                showShortToast(context,"Permission denied to access location")
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult()
                    exception.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            }
        }
    }


    fun requestLocationPermissions(activity: FragmentActivity,permissionReqCode:Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            permissionReqCode
        )
    }

    fun stopLocationUpdates(fusedLocationClient: FusedLocationProviderClient,locationCallback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}