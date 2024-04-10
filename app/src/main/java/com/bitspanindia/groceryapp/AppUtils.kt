package com.bitspanindia.groceryapp

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.IntentSender
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.DialogCommonMessBinding
import com.bitspanindia.groceryapp.databinding.DialogErrorBinding
import com.bitspanindia.groceryapp.databinding.DialogLogoutBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.roundToInt

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

    fun showCalendar(context: Context, showPrev: Boolean = false, callBack: (date: String) -> Unit?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, selectedYear, monthOfYear, dayOfMonth ->
                val monthSelected =
                    if (monthOfYear < 9) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                val dateSelected = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                callBack("$selectedYear-$monthSelected-$dateSelected")

            },
            year,
            month,
            day
        )
        if (showPrev) dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
        else dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    fun getVisibleTimeRanges(): List<Boolean> {
        val timeRanges = mutableListOf<Boolean>(
            true,
            true,
            true
        )
        val rangeStartHours = listOf(7, 12, 17)
        val rangeEndHours = listOf(10, 15, 21)

        val currentTime = Calendar.getInstance()

        if (currentTime.before(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, rangeStartHours.first()) })) {
            return timeRanges
        }

        for (index in rangeStartHours.indices) {
            val rangeStart = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, rangeStartHours[index])
                set(Calendar.MINUTE, 0)
            }

            val rangeEnd = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, rangeEndHours[index])
                set(Calendar.MINUTE, 0)
            }

            val rangeAfter = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, if (index == 2) rangeEndHours[index] else rangeEndHours[index + 1])
                set(Calendar.MINUTE, 0)
            }

            if (currentTime.after(rangeStart) && currentTime.before(rangeEnd)) {
                return timeRanges
            } else timeRanges[index] = false

            if (currentTime.before(rangeAfter)) return timeRanges
        }

        return timeRanges
    }

    fun getTodayAndTomorrowDates(): Pair<String, String> {
        val todayCalendar = Calendar.getInstance()
        val tomorrowCalendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = dateFormat.format(todayCalendar.time)
        val tomorrowStr = dateFormat.format(tomorrowCalendar.time)

        return Pair(todayStr, tomorrowStr)
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
    fun checkGpsStatus(activity: FragmentActivity):Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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

    fun gpsPermission(
        context: Context,
        locationSettingsResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
        callback: () -> Any
    ) {

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
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution.intentSender).build()
                    locationSettingsResultLauncher.launch(intentSenderRequest)
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

    fun showErrorMsgDialog(context: Context, msg: String, callback: () -> Any) {
        val pDialog = Dialog(context)

        val binding: DialogErrorBinding = DialogErrorBinding.inflate(LayoutInflater.from(context), null, false)
        pDialog.setContentView(binding.root)
        pDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setCancelable(false)

        pDialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        binding.tvError.text = msg
        binding.btnOk.setOnClickListener {
            pDialog.dismiss()
            callback()
        }

        pDialog.show()
    }

    fun showCommonMess(context: Context, mainMess: String, subMainMess: String = "", negBtnVisib: Int = View.VISIBLE, callBack: () -> Unit) {
        val dialog = Dialog(context)
        val binding: DialogCommonMessBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_common_mess,
            null,
            false
        )
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            300.toDp(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        binding.negBtn.visibility = negBtnVisib

        if (subMainMess.isNotEmpty()) {
            binding.subMainMessTxt.visibility = View.VISIBLE
            binding.subMainMessTxt.text = subMainMess
        }
        binding.mainMessTxt.text = mainMess
        binding.close.setOnClickListener {
            dialog.dismiss()
        }

        binding.posBtn.setOnClickListener {
            callBack()
            dialog.dismiss()
        }

        binding.negBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showLogoutDialog(context: Context, callBack: () -> Unit) {
        val dialog = Dialog(context)
        val dBinding = DialogLogoutBinding.inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(dBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
                300.toDp(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        dBinding.apply {
            yesBtn.setOnClickListener {
                callBack()
                dialog.dismiss()
            }

            noBtn.setOnClickListener {
                dialog.dismiss()
            }

            closeImg.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

}