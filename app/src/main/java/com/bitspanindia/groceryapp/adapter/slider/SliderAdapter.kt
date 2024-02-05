package com.bitspanindia.groceryapp.adapter.slider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemProductImageBinding
import com.bitspanindia.groceryapp.data.model.SliderModel
class SliderAdapter(private val data: List<SliderModel>,
                    val callBack:(pos:Int)->Any
) :
    RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SliderModel) {
            binding.ivSlider.setImageResource(data.image)
            binding.executePendingBindings()

            binding.ivSlider.setOnClickListener {
                callBack(adapterPosition)
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
