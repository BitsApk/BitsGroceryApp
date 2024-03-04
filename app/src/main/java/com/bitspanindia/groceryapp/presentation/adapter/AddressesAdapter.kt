package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.ItemAddressesBinding
import com.bitspanindia.groceryapp.databinding.ItemLocationBinding
import com.bitspanindia.groceryapp.data.model.PlaceModel
import com.bitspanindia.groceryapp.data.model.response.MyAddress
import com.bumptech.glide.Glide
class AddressesAdapter(
    private val context: Context,
    private val addressesList: List<MyAddress>,
    private val callBack:(address:MyAddress,clickType:String)->Any
): RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = addressesList[position]
        holder.bind(item)

    }


    inner class ViewHolder(val binding: ItemAddressesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyAddress) {

            binding.tvCityDetails.text = context.getString(R.string.my_address,item.permanentAdd,item.locality,item.landMark,item.city,item.zipcode)
            binding.tvAddressType.text =  item.addressName

            binding.ivDelete.setOnClickListener {
                callBack(item,"del")
            }

            itemView.setOnClickListener {
                callBack(item,"itemClick")
            }

        }
    }

    override fun getItemCount(): Int = addressesList.size
}