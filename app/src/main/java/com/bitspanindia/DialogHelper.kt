package com.bitspanindia

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.DialogErrorBinding
import com.bitspanindia.groceryapp.databinding.LoaderDialogBinding

class DialogHelper(private val context: Context,private val mActivity:FragmentActivity) {
    private val pDialog: Dialog = Dialog(context)

    fun showProgressDialog() {
        val binding: LoaderDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.loader_dialog,
            null,
            false
        )
        pDialog.setContentView(binding.root)
        pDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setCancelable(false)
        pDialog.window!!.setLayout(450, 450)
        pDialog.show()
    }

    fun hideProgressDialog() {
        if (pDialog.isShowing) {
            pDialog.dismiss()
        }
    }

    fun showErrorMsgDialog( msg: String, callback: () -> Any) {
        val pDialog = Dialog(mActivity)

        val binding: DialogErrorBinding = DialogErrorBinding.inflate(mActivity.layoutInflater)
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

}