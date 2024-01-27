package com.bitspanindia.groceryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemBannerImageBinding

class BannerImageAdapter(
    private val bannerList: List<*>
): RecyclerView.Adapter<BannerImageAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemBannerImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: Int) {

            binding.imageView.setImageResource(banner)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bannerList[position]
        holder.bind(item as Int)

    }


    override fun getItemCount(): Int = bannerList.size
}