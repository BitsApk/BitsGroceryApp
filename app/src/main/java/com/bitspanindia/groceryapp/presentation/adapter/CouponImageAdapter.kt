package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.model.BannerData
import com.bitspanindia.groceryapp.data.model.response.CouponCode
import com.bitspanindia.groceryapp.data.model.response.CouponListResp
import com.bitspanindia.groceryapp.databinding.ItemBannerImageBinding
import com.bitspanindia.groceryapp.databinding.ItemCouponsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CouponImageAdapter(
    private val couponList: List<CouponCode>,
    private val context: Context,
    private val callBack: (name: String, pos: Int) -> Unit
): RecyclerView.Adapter<CouponImageAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemCouponsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(coupon: CouponCode) {
            binding.apply {
                couponCodeTxt.text = coupon.promoCode
                validTxt.text = context.getString(R.string.valid_until_n_s, "${coupon.expiryDate} - ${coupon.expiryTime}")
                descriptionTxt.text = coupon.promoCodeDiscription
                percTxt.text = context.getString(R.string.s_perc, coupon.promoCodeDiscount)

                if (coupon.canApply == false) {
                    errorTxt.visibility = View.VISIBLE
                    errorTxt.text = coupon.mess
                } else {
                    errorTxt.visibility = View.GONE
                }

                applyTxt.setOnClickListener {
                    callBack(coupon.promoCode ?: "", absoluteAdapterPosition)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCouponsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = couponList[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = couponList.size
}