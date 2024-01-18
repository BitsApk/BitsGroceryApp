package com.bitspanindia.groceryapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemAddressesBinding
import com.bitspanindia.groceryapp.databinding.ItemLocationBinding
import com.bitspanindia.groceryapp.model.PlaceModel
import com.bumptech.glide.Glide

class AddressesAdapter(
    private val addressesList: List<PlaceModel>
): RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = addressesList[position]
        holder.bind(item,holder)

    }


    inner class ViewHolder(val binding: ItemAddressesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceModel, holder: ViewHolder) {

            binding.tvCityDetails.text = item.address
            binding.tvAddressType.text =  item.name

        }
    }


    override fun getItemCount(): Int = addressesList.size
}