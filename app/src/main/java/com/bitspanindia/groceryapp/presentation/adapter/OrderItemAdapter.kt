package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.ItemLocationBinding
import com.bitspanindia.groceryapp.data.model.response.ProductOrder
import com.bitspanindia.groceryapp.databinding.ItemOrderProductBinding
import com.bitspanindia.groceryapp.databinding.ItemOrdersBinding
import com.bumptech.glide.Glide

class OrderItemAdapter(
    private val mContext:Context,
    private val orderItemList: List<ProductOrder>,
): RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = orderItemList[position]
        holder.bind(item)

    }


    inner class ViewHolder(val binding: ItemOrderProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductOrder) {

            Glide.with(mContext).load(item.image).placeholder(R.drawable.product_placeholder).into(binding.ivProduct)
            binding.tvProductName.text = item.productName
            binding.tvQuantity.text = mContext.getString(R.string.quantity_price,item.productQty,item.discountedPrice)
            binding.tvPrice.text = mContext.getString(R.string.rupee,item.total.toString())
            binding.tvDiscountPrice.text = mContext.getString(R.string.rupee,item.productPrice.toString())
            binding.tvDiscountPrice.paintFlags = binding.tvDiscountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvDiscountPrice.visibility = if (!item.discount.isNullOrEmpty() && item.discount!="0") View.VISIBLE else View.GONE

        }
    }


    override fun getItemCount(): Int = orderItemList.size
}