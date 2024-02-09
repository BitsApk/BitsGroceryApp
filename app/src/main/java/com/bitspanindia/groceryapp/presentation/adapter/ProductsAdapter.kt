package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bitspanindia.groceryapp.data.model.SliderModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductsAdapter(
    private val data: List<ProductData>,
    private val context: Context,
    private val countMap: MutableMap<String, Int>,
    private val callback: (prod: ProductData, action: CartAction) -> Any
) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductData) {


            Glide.with(context)
                .load(product.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProduct)
            binding.apply {
                tvProductName.text = product.productName
                tvQuantity.text = product.stock
                tvPrice.text = product.discountedPrice.toString()

                if (countMap[product.id] == null) {
                    handleAddBtnVisiblity(View.VISIBLE, View.GONE)
                } else {
                    count.text = countMap[product.id].toString()
                    handleAddBtnVisiblity(View.GONE, View.VISIBLE)
                }


                btnAdd.setOnClickListener {
                    handleAddBtnVisiblity(View.GONE, View.VISIBLE)
                    countMap[product.id] = 1
                    count.text = countMap[product.id].toString()
                    callback (product, CartAction.Add)
                }

                add.setOnClickListener {
                    countMap[product.id] = countMap[product.id]!! + 1
                    count.text = countMap[product.id].toString()
                    callback (product, CartAction.Add)
                }

                minus.setOnClickListener {
                    if(countMap[product.id] == 1) {
                        countMap.remove(product.id)
                        handleAddBtnVisiblity(View.VISIBLE, View.GONE)
                    } else {
                        countMap[product.id] = countMap[product.id]!! - 1
                        count.text = countMap[product.id].toString()
                    }
                    callback (product, CartAction.Minus)
                }
            }

        }

        private fun handleAddBtnVisiblity(add: Int, countLay: Int) {
            binding.btnAdd.visibility = add
            binding.countLay.visibility = countLay
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}