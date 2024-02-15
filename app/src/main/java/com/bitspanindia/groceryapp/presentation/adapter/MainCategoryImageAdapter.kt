package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.model.Category
import com.bitspanindia.groceryapp.data.model.MainCategoryData
import com.bitspanindia.groceryapp.data.model.SubCategory
import com.bitspanindia.groceryapp.databinding.ItemCategoryVerticalBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlin.properties.Delegates

class MainCategoryImageAdapter(
    private val mainCatList: List<Any>, // Change the type to Any
    private val context: Context,
    private val designType: String,
    private val callBack:(subCatId:String,catName:String)->Any
) : RecyclerView.Adapter<MainCategoryImageAdapter.ViewHolder>() {

    private var selectedItem = 0

    inner class ViewHolder(val binding: ItemCategoryVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Any) { // Change parameter type to Any
            when (item) {
                is Category -> {
                    val mainCat = item as Category

                    setData(mainCat.catName,mainCat.catImage)

                    itemView.setOnClickListener {
                        callBack(mainCat.catId?:"",mainCat.catName?:"")
                    }
                }
                is SubCategory -> {
                    val subCat = item as SubCategory

//                    setImageTextSize()

                    binding.view.visibility = View.VISIBLE
                    setData(subCat.subCatName,subCat.subCatImage)

                    // Handle SubCategory here if needed
//                    if (designType == "subCatPage") {
//                        if (adapterPosition == 0) {
//                            binding.ivProduct.background =
//                                ContextCompat.getDrawable(context, R.drawable.gradient_category)
//                        } else {
//                            binding.ivProduct.background =
//                                ContextCompat.getDrawable(context, R.drawable.white_rounded)
//                        }
//                    } else {
//                        binding.ivProduct.background = ContextCompat.getDrawable(context, R.drawable.gradient_category)
//                    }

                    ContextCompat.getDrawable(context, R.drawable.white_rounded)
                    itemView.setOnClickListener {
                        selectedItem = absoluteAdapterPosition
//                        selectedItem = if (selectedItem == absoluteAdapterPosition) -1 else absoluteAdapterPosition
                        notifyDataSetChanged()
                        callBack(subCat.id?:"",subCat.subCatName?:"")
                    }
                   binding.view.visibility = if (selectedItem == absoluteAdapterPosition) View.VISIBLE else View.GONE

                }
            }

        }

        private fun setImageTextSize() {
            val layoutParams = binding.ivProduct.layoutParams
            layoutParams.height = 65
            binding.ivProduct.layoutParams = layoutParams
            binding.tvProductName.textSize = 10f
        }

        private fun setData(catName: String?, catImage: String?) {
            Glide.with(context)
                .load(catImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProduct)

            binding.tvProductName.text = catName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mainCatList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = mainCatList.size
}
