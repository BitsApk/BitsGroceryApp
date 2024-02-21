package com.bitspanindia.groceryapp.presentation.adapter.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemProductImageBinding
import com.bitspanindia.groceryapp.data.model.response.MultiImg
import com.bumptech.glide.Glide

class SliderAdapter(
    private val mContext: Context,
    private val data: List<MultiImg>,
    val callBack:(pos:Int)->Any
) :
    RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MultiImg) {
            Glide.with(mContext).load(data.image).into(binding.ivSlider)
            binding.executePendingBindings()

            binding.ivSlider.setOnClickListener {
                callBack(absoluteAdapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
