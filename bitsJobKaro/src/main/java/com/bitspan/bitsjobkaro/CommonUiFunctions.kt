package com.bitspan.bitsjobkaro

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.chip.Chip
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.models.DistrictData
import com.bitspan.bitsjobkaro.data.models.StateData
import com.bitspan.bitsjobkaro.databinding.ErrorResponseBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.roundToInt


object CommonUiFunctions {

    var statesList: List<StateData> = listOf<StateData>()
    // District List
    var distList: List<DistrictData> = listOf<DistrictData>()

    fun getSingleState(stateId: String): String {
        for (item in statesList) {
            if (item.stateId == stateId) return item.stateTitle ?: ""
        }
        return ""
    }

    fun getSingleDistrict(distId: String): String {
        for (item in distList) {
            if (item.districtid == distId) return item.districtTitle ?: ""
        }
        return ""
    }

    fun showDataNotAvailable(context: Context, mess: String?) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
    }

    fun changeStatusBarColor(activity: FragmentActivity, color: Int) {
        try {
            activity.window.let {
//                it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                it.statusBarColor = ContextCompat.getColor(activity, color)
            }
//            activity.window.decorView.systemUiVisibility = View.VISIBLE
        } catch (e: Exception){}
    }

    fun startShimmer(shimmer: ShimmerFrameLayout, recView: RecyclerView) {
        recView.visibility = View.GONE
        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE
    }

    fun stopShimmer(shimmer: ShimmerFrameLayout, recView: RecyclerView?) {
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        recView?.visibility = View.VISIBLE
    }

    fun bottomNavBarVisibility(activity: FragmentActivity, visibility: Int) {
        try {
            (activity as JobMainActivity).bottomNavBarVisibility(visibility)
        } catch (e: Exception) {}
    }

    fun askNotifPermission(activity: FragmentActivity) {
        try {
            (activity as JobMainActivity).askNotifPermission()
        } catch (e: Exception) {}
    }

    fun showHomeNav(activity: FragmentActivity, id: Int) {
        try {
            (activity as JobMainActivity).showHomeNav(id)
        } catch (e: Exception) {}
    }

    fun setUpBottomNavMenu(activity: FragmentActivity) {
        try {
            (activity as JobMainActivity).setUpBottomNavMenu()
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Problem while setting up bottom nav: Common Ui functions")
        }
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

    fun dpToFloat(context: Context, value: Int): Float {
        return value * context.resources.displayMetrics.density
    }

    fun dpToInt(context: Context, value: Int): Int {
        return dpToFloat(context, value).roundToInt()
    }

    fun setMargin(context: Context, view: View, left: Int, top: Int, right: Int, bottom: Int) {
        val p = (view.layoutParams as? ViewGroup.MarginLayoutParams)
        p?.setMargins(dpToInt(context, left), dpToInt(context, top), dpToInt(context, right), dpToInt(context, bottom))
        view.layoutParams = p
    }

    fun multiPartText(content: String): RequestBody {
        return content.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun multiPartImage(imagePath: String, imageNameInApi: String): MultipartBody.Part {
        val body: RequestBody = File(imagePath).asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(imageNameInApi, File(imagePath).name, body)
    }

    fun multiPartDoc(docPath: String, docName: String): MultipartBody.Part {
        val body: RequestBody = File(docPath).asRequestBody("application/pdf".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(docName, File(docPath).name, body)
    }

    fun multiPartDrawable(file: File, docName: String): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(docName, file.name, requestFile)
    }




    fun getRealPathFromURI(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream?.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)

        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path
    }




    fun getDateMonthName(strDate: String?): String {
        if (strDate == null) return "NA"
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(strDate)

        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val monthNameFormat = SimpleDateFormat("MMM", Locale.getDefault())
        return "$day ${monthNameFormat.format(calendar.time)}"
    }

    fun calculateAge(dobString: String): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dob: Date = dateFormat.parse(dobString) ?: return -1 // Invalid date format

        val today: Calendar = Calendar.getInstance()
        val birthDate: Calendar = Calendar.getInstance()

        birthDate.time = dob

        if (birthDate.after(today)) {
            return -1 // Invalid birth date
        }

        var age: Int = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        // Check if the birthday has occurred this year
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }




    fun showErrorMsgDialog(context: Context, msg: String, callback: () -> Any) {
        val pDialog = Dialog(context)

        val binding: ErrorResponseBinding = ErrorResponseBinding.inflate(LayoutInflater.from(context), null, false)
        pDialog.setContentView(binding.root)
        pDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setCancelable(false)

        pDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        binding.tvError.text = msg
        binding.tvGoBack.setOnClickListener {
            pDialog.dismiss()
            callback()
        }

        pDialog.show()
    }

    fun createChip(textValue: String, color: Int, context: Context): Chip {
        val chip = Chip(context)
        chip.apply {
            text = textValue
            chipBackgroundColor = getColorStateList(context, color)
            chipStrokeWidth = dpToFloat(context, 1)
            setChipStrokeColorResource(R.color.sub_views)
        }
        return chip;
    }

    private val stateList = arrayOf(
        intArrayOf(-android.R.attr.state_checked)
    )

    private fun getColorStateList(context: Context, color: Int): ColorStateList {
        val colorList = intArrayOf(
            ContextCompat.getColor(context, color),
        )
        return ColorStateList(stateList, colorList)
    }

    fun handleKeyBoardState(context: Context, editView: EditText) {
        val inputMethodManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        editView.requestFocus()
        inputMethodManager.showSoftInput(editView, InputMethodManager.SHOW_IMPLICIT)
    }

    fun showKeyBoard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    fun hideKeyBoard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun beforEqualToday(dob: String): Boolean {
        val chooseDateList = dob.split("-")
        val selectedDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, chooseDateList[2].toInt())
            set(Calendar.MONTH, chooseDateList[1].toInt() - 1)
            set(Calendar.YEAR, chooseDateList[0].toInt())
        }
        val currentDate = Calendar.getInstance()
        return selectedDate.before(currentDate)
    }

//    fun changeStatusBarColor(activity: FragmentActivity, color: Int) {
//        try {
//            activity.window.let {
////                it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
////                it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                it.statusBarColor = ContextCompat.getColor(activity, color)
//            }
////            activity.window.decorView.systemUiVisibility = View.VISIBLE
//        } catch (e: Exception){}
//    }


//    fun selectBottomNavMenus(activity: FragmentActivity, tabIndex: Int) {
//        try {
//            (activity as MainActivity).selectInteractionMenu(tabIndex)
//        } catch (e: Exception) {}
//    }


}