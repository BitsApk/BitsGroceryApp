package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.ItemProductImageSmallBinding
import com.bitspanindia.groceryapp.data.model.SliderModel
import com.bitspanindia.groceryapp.data.model.response.MultiImg
import com.bumptech.glide.Glide

class ProductSmallImageAdapter(
    private val data: List<MultiImg>,
    private val context: Context,
    val callBack: (pos: Int) -> Any
) :
    RecyclerView.Adapter<ProductSmallImageAdapter.ViewHolder>() {

    var selectedItemPos=0

    inner class ViewHolder(private val binding: ItemProductImageSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MultiImg) {
            Glide.with(context).load(data.image).into(binding.ivSlider)
            binding.executePendingBindings()

            setSelectedItem(absoluteAdapterPosition)

            binding.ivSlider.setOnClickListener {
                selectedItemPos = absoluteAdapterPosition
                callBack(absoluteAdapterPosition)
                notifyDataSetChanged()
            }



        }

        private fun setSelectedItem(adapterPosition: Int) {
            if (selectedItemPos==adapterPosition){
                binding.clCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_50))
            }else{
                binding.clCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductImageSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}