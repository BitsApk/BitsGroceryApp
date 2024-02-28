package com.bitspan.bitsjobkaro.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bitspan.bitsjobkaro.R
import com.google.android.material.textfield.TextInputLayout
import com.bitspan.bitsjobkaro.data.constants.enums.DateFormatType
import java.util.*

class DialogHelper(private val context: Context) {

    fun showDifferentStoreAlert() {
        val dialog = Dialog(context).apply {
            setContentView(R.layout.dialogue_helper_desk)
            setCancelable(false)
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val close = dialog.findViewById<ImageView>(R.id.close)

        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showCalendar(formatType: DateFormatType = DateFormatType.DayMonthYear,callBack: (date: String) -> Unit?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, selectedYear, monthOfYear, dayOfMonth ->
            val monthSelected =
                if (monthOfYear < 9) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
            val dateSelected = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            when(formatType) {
                DateFormatType.DayMonthYear -> callBack("$dateSelected-$monthSelected-$selectedYear")
                DateFormatType.YearMonthDay -> callBack("$selectedYear-$monthSelected-$dateSelected")
            }
            // Display Selected date in textbox
        }, year, month, day)
        dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    fun showAddPrevComp(callBack: (name: String, yourDesig: String, from: String, to: String) -> Any) {
        val dialog = Dialog(context).apply {
            setContentView(R.layout.dialog_add_previous_comp)
            setCancelable(false)
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val close = dialog.findViewById<ImageView>(R.id.close)
        val name = dialog.findViewById<TextInputLayout>(R.id.eEditCompEdTxt)
        val desig = dialog.findViewById<TextInputLayout>(R.id.eEditComDesigEdTxt)
        val from = dialog.findViewById<TextInputLayout>(R.id.eEditFromEdTxt)
        val to = dialog.findViewById<TextInputLayout>(R.id.eEditToEdTxt)
        val add = dialog.findViewById<TextView>(R.id.eAddPrevCompBtn)

        from.editText?.setOnClickListener {
            showCalendar(DateFormatType.YearMonthDay) { date ->
                from.editText?.setText(date)
            }
        }
        to.editText?.setOnClickListener {
            showCalendar(DateFormatType.YearMonthDay) { date ->
                to.editText?.setText(date)
            }
        }

        add.setOnClickListener {
            if (checkFields(name, desig, from, to)) {
                callBack(name.editText?.text.toString(),
                    desig.editText?.text.toString(),
                    from.editText?.text.toString(),
                    to.editText?.text.toString()
                )
                dialog.dismiss()
            }
        }

        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun checkFields(name: TextInputLayout, desig: TextInputLayout, from: TextInputLayout, to: TextInputLayout): Boolean {
        val check = if (name.editText?.text.toString().isBlank()) {
            name.editText?.error = "Name can't be empty"
            false
        } else if (desig.editText?.text.toString().isBlank()) {
            desig.editText?.error = "Enter your role"
            false
        } else if (from.editText?.text.toString().isBlank()) {
            from.editText?.error = "Choose when you started"
            false
        } else if (to.editText?.text.toString().isBlank()) {
            to.editText?.error = "Choose when you ended"
            false
        } else true
        return check
    }


}