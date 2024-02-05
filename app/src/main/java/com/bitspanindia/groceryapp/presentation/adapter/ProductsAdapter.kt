package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bitspanindia.groceryapp.data.model.SliderModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductsAdapter(
    private val data: List<ProductData>,
    private val context: Context
) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ProductData) {
            Log.d("Rishabh", "Inside product inside adapter")

//            Glide.with(context)
//                .load(data.image)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(binding.ivProduct)
            Log.d("Rishabh", "Inside product inside after glide adapter")

            binding.tvProductName.text = data.productName
            binding.tvQuantity.text = data.stock
            binding.tvPrice.text = data.discountedPrice.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Rishabh", "Inside product inside in on bind adapter")

        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}