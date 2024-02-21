package com.bitspanindia.groceryapp

import android.content.res.Resources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.databinding.ItemProductBinding

object AppUtils {

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

}