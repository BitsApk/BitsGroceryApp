package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.model.BannerData
import com.bitspanindia.groceryapp.databinding.ItemBannerImageBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class BannerImageAdapter(
    private val bannerList: List<BannerData>,
    private val context: Context
): RecyclerView.Adapter<BannerImageAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemBannerImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: BannerData) {
            Glide.with(context)
                .load(banner.appbanner_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bannerList[position]
        holder.bind(item)

    }


    override fun getItemCount(): Int = bannerList.size
}