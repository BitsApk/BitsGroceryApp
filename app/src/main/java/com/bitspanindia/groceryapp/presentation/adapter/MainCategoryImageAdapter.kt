package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.model.Category
import com.bitspanindia.groceryapp.data.model.MainCategoryData
import com.bitspanindia.groceryapp.databinding.ItemCategoryVerticalBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MainCategoryImageAdapter(
    private val mainCatList: List<Category>,
    private val context: Context
): RecyclerView.Adapter<MainCategoryImageAdapter.ViewHolder>() {




    inner class ViewHolder(val binding: ItemCategoryVerticalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(mainCat: Category) {
            Glide.with(context)
                .load(mainCat.catImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProduct)

            binding.tvProductName.text = mainCat.catName
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mainCatList[position]
        holder.bind(item)

    }



    override fun getItemCount(): Int = mainCatList.size
}