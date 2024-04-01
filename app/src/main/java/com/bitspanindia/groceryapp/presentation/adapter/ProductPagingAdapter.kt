package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.AppUtils.adjustItemWidth
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bumptech.glide.Glide

class ProductPagingAdapter(
    private val context: Context,
    private val designType: Int,
    private val countMap: MutableMap<String, MutableMap<String, Int>>,
    private val callback: (prod: ProductData, action: CartAction) -> Any
) : PagingDataAdapter<ProductData, ProductPagingAdapter.ViewHolder>(Diff) {

    inner class ViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductData) {
            binding.apply {
                tvProductName.text = product.productName
                tvWeight.text = product.weight
                tvPrice.text = context.getString(R.string.rupee, product.discountedPrice.toString())
                offeredField.visibility =
                    if (product.price.toString().isNullOrEmpty()) View.GONE else View.VISIBLE
                offeredField.text = context.getString(R.string.rupee, product.price.toString())
                offeredField.paintFlags =
                    binding.offeredField.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                Glide.with(context).load(product.image).placeholder(R.drawable.product_placeholder)
                    .into(binding.ivProduct)

                adjustItemWidth(designType, clItem)

                if (countMap[product.id] == null) {
                    handleAddBtnVisiblity(View.VISIBLE, View.GONE)
                } else {
                    count.text = countMap[product.id]!!["-1"].toString()
                    handleAddBtnVisiblity(View.GONE, View.VISIBLE)
                }


                if (product.stock == "0") {
                    btnAdd.isEnabled = false
                    btnAdd.text = context.getString(R.string.no_stock)
                } else {
                    btnAdd.isEnabled = true
                    btnAdd.text = context.getString(R.string.add)
                    btnAdd.setOnClickListener {
                        handleAddBtnVisiblity(View.GONE, View.VISIBLE)
                        countMap[product.id] = mutableMapOf(Pair(product.sizeId, 1), Pair("-1", 1))
                        count.text = "1"
                        callback(product, CartAction.Add)
                    }
                }


                add.setOnClickListener {
                    countMap[product.id]!![product.sizeId] =
                        countMap[product.id]!![product.sizeId]!! + 1
                    countMap[product.id]!!["-1"] = countMap[product.id]!!["-1"]!! + 1
                    count.text = countMap[product.id]!!["-1"].toString()
                    callback(product, CartAction.Add)

                    if (countMap[product.id]!![product.sizeId] == (product.stock ?: "0").toInt()) {
                        binding.add.isEnabled = false
                        binding.add.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.grey_700
                            )
                        )
                    }
                }
                minus.setOnClickListener {
                    if (countMap[product.id]!!["-1"] == 1) {
                        countMap.remove(product.id)
                        handleAddBtnVisiblity(View.VISIBLE, View.GONE)
                    } else {
                        if (countMap[product.id]!![product.sizeId] == 1) {
                            countMap[product.id]!!.remove(product.sizeId)
                        } else {
                            countMap[product.id]!![product.sizeId] =
                                countMap[product.id]!![product.sizeId]!! - 1
                        }
                        countMap[product.id]!!["-1"] = countMap[product.id]!!["-1"]!! - 1
                        count.text = countMap[product.id]!!["-1"].toString()
                    }
                    callback(product, CartAction.Minus)
                    if (!binding.add.isEnabled) {
                        binding.add.isEnabled = true
                        binding.add.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    }
                }
                binding.ivProduct.setOnClickListener {
                    callback(product, CartAction.ItemClick)
                }

            }

        }

        private fun handleAddBtnVisiblity(add: Int, countLay: Int) {
            binding.btnAdd.visibility = add
            binding.countLay.visibility = countLay
        }

    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object Diff : DiffUtil.ItemCallback<ProductData>() {
        override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem == newItem
        }

    }

}