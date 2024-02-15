package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class ProductsAdapter(
    private val data: List<ProductData>,
    private val context: Context,
    private val designType:Int
) :
    RecyclerView.Adapter<ProductsAdapter.NormalProductViewHolder>() {

    inner class NormalProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ProductData) {
            Log.d("Rishabh", "Inside product inside adapter")

//            Glide.with(context)
//                .load(data.productImage)
////                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.placeholder_user)
//                .into(binding.ivProduct)
//            Log.d("Rishabh", "Inside product inside after glide adapter")

            Glide.with(context).load(data.productImage).placeholder(R.drawable.product_placeholder).into(binding.ivProduct)

            binding.tvProductName.text = data.productName
            binding.tvWeight.text = data.weight
            binding.tvPrice.text = context.getString(R.string.rupee,data.discountedPrice.toString())
            binding.offeredField.visibility = if (data.price.toString().isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.offeredField.text = context.getString(R.string.rupee,data.price.toString())
            binding.offeredField.paintFlags = binding.offeredField.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (designType==ElementType.Grid.type){
                // Adjusting ImageView layout parameters
                val layoutParams = binding.ivProduct.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT // Adjust height as needed
                binding.ivProduct.layoutParams = layoutParams
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  NormalProductViewHolder {

       return  NormalProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    }

    override fun onBindViewHolder(holder: NormalProductViewHolder, position: Int) {
        Log.d("Rishabh", "Inside product inside in on bind adapter")

           holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}