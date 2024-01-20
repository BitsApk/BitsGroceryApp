package com.bitspanindia.groceryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemCartProductBinding
import com.bitspanindia.groceryapp.databinding.ItemHorizontalProductBinding
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bitspanindia.groceryapp.model.SliderModel

class CartProductAdapter(private val data: List<SliderModel>) :
    RecyclerView.Adapter<CartProductAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHorizontalProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SliderModel) {
            binding.ivProduct.setImageResource(data.image)
            binding.tvProductName.text = data.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHorizontalProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return 2 //TODO data.size return hoga
    }

}