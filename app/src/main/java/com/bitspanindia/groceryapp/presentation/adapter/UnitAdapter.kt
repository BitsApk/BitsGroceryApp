package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.model.response.MultiWeight
import com.bitspanindia.groceryapp.databinding.ItemUnitBinding

class UnitAdapter(
    private val context:Context,
    private val unitList: List<MultiWeight>,
    private var callBack:(data:MultiWeight)->Any
): RecyclerView.Adapter<UnitAdapter.ViewHolder>() {

    private var selectedItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUnitBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = unitList[position]
        holder.bind(item)

    }
    inner class ViewHolder(val binding: ItemUnitBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MultiWeight) {
            binding.tvWeight.text = item.weight
            binding.tvPrice.text = context.getString(R.string.rupee,item.discountedPrice.toString())
            binding.tvDiscount.visibility = if (item.price.toString().isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.tvDiscount.text = context.getString(R.string.rupee,item.price.toString())
            binding.tvDiscount.paintFlags = binding.tvDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            itemView.setOnClickListener {
                selectedItem = absoluteAdapterPosition
                notifyDataSetChanged()
                callBack(item)
            }

            if (selectedItem==absoluteAdapterPosition){
                changeDrawableColor(ContextCompat.getColor(context,R.color.red_400),ContextCompat.getColor(context,R.color.green_50))
            }else{
                changeDrawableColor(context.getColor(R.color.grey_700),ContextCompat.getColor(context,R.color.white))
            }

        }
        private fun changeDrawableColor(strokeColor: Int, backgroundColor: Int) {
            val drawable = binding.clItem.background

            if (drawable is GradientDrawable) {
                // Change the stroke color
                drawable.setStroke(2, strokeColor) // Specify the width and color

                // Change the background tint color
                drawable.setTint(backgroundColor)
            }
        }
    }

    override fun getItemCount(): Int = unitList.size

}