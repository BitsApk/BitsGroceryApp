package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.AppUtils.adjustItemWidth
import com.bitspanindia.groceryapp.AppUtils.toDp
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bumptech.glide.Glide

class ProductPagingAdapter(
    private val context: Context,
    private val designType:Int
) : PagingDataAdapter<ProductData, ProductPagingAdapter.ViewHolder>(Diff) {

    inner class ViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductData) {
            binding.apply {
                tvProductName.text = data.productName
                tvWeight.text = data.weight
                tvPrice.text = context.getString(R.string.rupee,data.discountedPrice.toString())
                offeredField.visibility = if (data.price.toString().isNullOrEmpty()) View.GONE else View.VISIBLE
                offeredField.text = context.getString(R.string.rupee,data.price.toString())
                offeredField.paintFlags = binding.offeredField.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                Glide.with(context).load(data.productImage).placeholder(R.drawable.product_placeholder).into(binding.ivProduct)

                adjustItemWidth(designType,clItem)

            }

        }

    }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            if (item != null) {
                holder.bind(item)
            }
        }

        object Diff : DiffUtil.ItemCallback<ProductData>() {
            override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
                return oldItem == newItem
            }

        }

}