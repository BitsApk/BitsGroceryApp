package com.bitspanindia.groceryapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemLocationBinding
import com.bitspanindia.groceryapp.model.PlaceModel
import com.bumptech.glide.Glide

class PlaceAdapter(
     val placeList: List<PlaceModel>
): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = placeList[position]
        holder.bind(item,holder)

    }


    inner class ViewHolder(val binding: ItemLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceModel, holder: ViewHolder) {

            binding.tvCityDetails.text = item.address
            binding.tvCity.text =  item.name

        }
    }


    override fun getItemCount(): Int = placeList.size
}