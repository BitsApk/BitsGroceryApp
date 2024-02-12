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
import com.bitspanindia.groceryapp.databinding.ItemProductHorizontalBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductsAdapter(
    private val data: MutableList<ProductData>,
    private val context: Context,
    private val countMap: MutableMap<String, Int>,
    private val orientation: Int,    // 0 is for vertical, 1 is for horizontal
    private val callback: (prod: ProductData, action: CartAction) -> Any
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class VerticalViewHolder(private val binding: ItemProductBinding) :
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


    inner class HorizontalViewHolder(private val binding: ItemProductHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductData, position: Int) {

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

                add.setOnClickListener {
                    countMap[product.id] = countMap[product.id]!! + 1
                    count.text = countMap[product.id].toString()
                    callback (product, CartAction.Add)
                }

                minus.setOnClickListener {
                    if(countMap[product.id] == 1) {
                        countMap.remove(product.id)
                        val pos = position
                        Log.d("Rishabh", "Adap pos: $pos $adapterPosition data size: ${data.size}")
                        data.removeAt(position)
                        Log.d("Rishabh", "Adap pos: $pos $adapterPosition data size: ${data.size}")

                        notifyItemRemoved(position)
                    } else {
                        countMap[product.id] = countMap[product.id]!! - 1
                        count.text = countMap[product.id].toString()
                    }
                    callback (product, CartAction.Minus)
                }
            }

        }

        private fun handleAddBtnVisiblity(add: Int, countLay: Int) {
            binding.countLay.visibility = countLay
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(orientation) {
            1 -> { HorizontalViewHolder(ItemProductHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
            else -> VerticalViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (orientation) {
            1 -> (holder as HorizontalViewHolder).bind(data[position], position)
            else -> (holder as VerticalViewHolder).bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}