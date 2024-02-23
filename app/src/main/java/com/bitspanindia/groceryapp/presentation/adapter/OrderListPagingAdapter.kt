package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.AppUtils.adjustItemWidth
import com.bitspanindia.groceryapp.AppUtils.toDp
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.response.Order
import com.bitspanindia.groceryapp.databinding.ItemOrdersBinding
import com.bitspanindia.groceryapp.databinding.ItemProductBinding
import com.bumptech.glide.Glide

class OrderListPagingAdapter(
    private val context: Context,
    val callback:(data:Order)->Any
) : PagingDataAdapter<Order, OrderListPagingAdapter.ViewHolder>(Diff) {

    inner class ViewHolder(val binding: ItemOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Order) {
            binding.apply {
                tvOrdId.text = data.orderId
                tvOrdDate.text = data.createdate
                tvOrdStatus.text = data.status
                tvPrice.text = context.getString(R.string.rupee,data.amount)
                tvPayType.text = context.getString(R.string.pay_type,data.payMode)

                when(data.orderStatus){
                    "P"->{
                        ivItem.setImageResource(R.drawable.icon_order_placed)
                        backgroundTint(R.color.green_100)
                    }
                    "PR"->{
                        ivItem.setImageResource(R.drawable.icon_order_packed)
                        backgroundTint(R.color.yellow_100)
                    }
                    "S"->{
                        ivItem.setImageResource(R.drawable.icon_shipped)
                        backgroundTint(R.color.blue_100)
                    }
                    "D"->{
                        ivItem.setImageResource(R.drawable.icon_order_delivered)
                        backgroundTint(R.color.orange_100)
                    }
                    "C","DC"->{
                        ivItem.setImageResource(R.drawable.icon_order_cancelled)
                        backgroundTint(R.color.red_100)
                    }
                }
            }

            itemView.setOnClickListener {
                callback(data)
            }

        }

        private fun backgroundTint(backgroundColor: Int) {
            binding.tvOrdStatus.background.setTint(ContextCompat.getColor(context,backgroundColor))
            binding.iconOrder.background.setTint(ContextCompat.getColor(context,backgroundColor))
        }
    }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            if (item != null) {
                holder.bind(item)
            }
        }

        object Diff : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }

        }

}