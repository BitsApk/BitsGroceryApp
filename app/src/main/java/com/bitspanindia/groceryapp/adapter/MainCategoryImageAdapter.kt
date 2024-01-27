package com.bitspanindia.groceryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.model.MainCategoryData
import com.bitspanindia.groceryapp.databinding.ItemCategoryVerticalBinding

class MainCategoryImageAdapter(
    private val mainCatList: List<*>
): RecyclerView.Adapter<MainCategoryImageAdapter.ViewHolder>() {




    inner class ViewHolder(val binding: ItemCategoryVerticalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(mainCat: MainCategoryData) {

            binding.ivProduct.setImageResource(mainCat.catImg)
            binding.tvProductName.text = mainCat.catName
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mainCatList[position]
        holder.bind(item as MainCategoryData)

    }



    override fun getItemCount(): Int = mainCatList.size
}