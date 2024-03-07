package com.bitspanindia.groceryapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.model.SliderModel
import com.bitspanindia.groceryapp.data.model.custom.CartUpdatedProdData
import com.bitspanindia.groceryapp.databinding.ItemCartUpdatedHorizBinding
import com.bitspanindia.groceryapp.databinding.ItemHorizontalProductBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CartOutOfStockAdapter(private val context: Context, private val data: List<CartUpdatedProdData>) :
    RecyclerView.Adapter<CartOutOfStockAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCartUpdatedHorizBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CartUpdatedProdData) {
            Glide.with(context)
                .load(data.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProduct)

            binding.apply {
                tvProductName.text = data.productName
                tvQuantity.text = data.weight
                tvPrice.text = data.discountedPrice.toString()
                stockInfo.text = context.getString(R.string.reduce_quantity_from_d_to_d, data.stockChange!!.first, data.stockChange!!.second)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartUpdatedHorizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}