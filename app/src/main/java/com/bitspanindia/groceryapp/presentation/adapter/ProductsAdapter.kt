package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.AppUtils.adjustItemWidth
import com.bitspanindia.groceryapp.AppUtils.toDp
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bitspanindia.groceryapp.data.model.SliderModel
import com.bitspanindia.groceryapp.databinding.ItemProductHorizontalBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductsAdapter(
    private val data: MutableList<ProductData>,
    private val context: Context,
    private val countMap: MutableMap<String, MutableMap<String, Int>>,
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
                tvPrice.text = product.discountedPrice.toString()

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
                        binding.add.setColorFilter(ContextCompat.getColor(context, R.color.grey_700))
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

            adjustItemWidth(orientation, binding.clItem)

        }

        private fun handleAddBtnVisiblity(add: Int, countLay: Int) {
            binding.btnAdd.visibility = add
            binding.countLay.visibility = countLay
        }

    }


    inner class HorizontalViewHolder(private val binding: ItemProductHorizontalBinding) :     // For cart views, here we show count with separate size ids
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductData) {

            Glide.with(context)
                .load(product.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProduct)
            binding.apply {
                tvProductName.text = product.productName
                tvQuantity.text = product.weight
                tvPrice.text = product.discountedPrice.toString()

                priceInfo.visibility = if (product.priceChange == null) View.GONE
                else {
                    priceInfo.text = context.getString(R.string.change_price_from_2f_to_2f, product.priceChange!!.first, product.priceChange!!.second)
                    View.VISIBLE
                }


                if (countMap[product.id] == null) {
                    handleAddBtnVisiblity(View.VISIBLE, View.GONE)
                } else {
                    count.text = countMap[product.id]!![product.sizeId].toString()
                    handleAddBtnVisiblity(View.GONE, View.VISIBLE)
                }

                add.setOnClickListener {
                    countMap[product.id]!![product.sizeId] =
                        countMap[product.id]!![product.sizeId]!! + 1
                    countMap[product.id]!!["-1"] = countMap[product.id]!!["-1"]!! + 1
                    count.text = countMap[product.id]!![product.sizeId].toString()
                    callback(product, CartAction.Add)
                    if (countMap[product.id]!![product.sizeId] == (product.stock ?: "0").toInt()) {
                        binding.add.isEnabled = false
                        binding.add.setColorFilter(ContextCompat.getColor(context, R.color.grey_700))
                    }
                }

                minus.setOnClickListener {
                    countMap[product.id]!!["-1"] = countMap[product.id]!!["-1"]!! - 1
                    if (countMap[product.id]!!["-1"] == 0) {
                        countMap.remove(product.id)
                        handleAddBtnVisiblity(View.VISIBLE, View.GONE)
                        data.removeAt(absoluteAdapterPosition)
                        notifyItemRemoved(absoluteAdapterPosition)
                    } else if (countMap[product.id]!![product.sizeId] == 1) {
                        countMap[product.id]!!.remove(product.sizeId)
                        handleAddBtnVisiblity(View.VISIBLE, View.GONE)
                        data.removeAt(absoluteAdapterPosition)
                        notifyItemRemoved(absoluteAdapterPosition)
                    } else {
                        countMap[product.id]!![product.sizeId] =
                            countMap[product.id]!![product.sizeId]!! - 1
                        count.text = countMap[product.id]!![product.sizeId].toString()
                    }
                    callback(product, CartAction.Minus)
                    if (!binding.add.isEnabled) {
                        binding.add.isEnabled = true
                        binding.add.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    }
                }
            }

        }

        private fun handleAddBtnVisiblity(add: Int, countLay: Int) {
            binding.countLay.visibility = countLay
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (orientation) {
            1 -> {
                HorizontalViewHolder(
                    ItemProductHorizontalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> VerticalViewHolder(
                ItemProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (orientation) {
            1 -> (holder as HorizontalViewHolder).bind(data[position])
            else -> (holder as VerticalViewHolder).bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}