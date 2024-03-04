package com.bitspanindia.groceryapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemLocationBinding
import com.bitspanindia.groceryapp.data.model.PlaceModel
import com.bitspanindia.groceryapp.databinding.ItemAddressesBinding
import com.bumptech.glide.Glide

class PlaceAdapter(
    val places: ArrayList<PlaceModel>,
    private val callback:(latitude:String,longitude:String)->Any
): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = places[position]
        holder.bind(item,holder)

    }


    inner class ViewHolder(val binding: ItemAddressesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceModel, holder: ViewHolder) {

            binding.tvCityDetails.text = item.fullAddress
            binding.tvAddressType.text =  item.city
            binding.ivDelete.visibility = View.GONE

            itemView.setOnClickListener {
                callback(item.latitude.toString(),item.longitude.toString())
            }

        }
    }


    override fun getItemCount(): Int = places.size
}