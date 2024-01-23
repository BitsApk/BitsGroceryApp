package com.bitspanindia.groceryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bitspanindia.groceryapp.model.SliderModel

class ProductsAdapter(private val data: List<SliderModel>) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SliderModel) {
            binding.ivProduct.setImageResource(data.image)
            binding.tvProductName.text = data.name
            binding.tvQuantity.text = data.quantity
            binding.tvPrice.text = data.price
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}