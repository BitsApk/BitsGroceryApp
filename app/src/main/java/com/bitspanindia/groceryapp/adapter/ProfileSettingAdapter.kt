package com.bitspanindia.groceryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.databinding.ItemProfileSettingBinding
import com.bitspanindia.groceryapp.model.ProfileSettingItemModel

class ProfileSettingAdapter(
    private val pSettingList: List<ProfileSettingItemModel>,
    private val callBack:(pos:Int)->Any
): RecyclerView.Adapter<ProfileSettingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = pSettingList[position]
        holder.bind(item,holder)

    }


    inner class ViewHolder(val binding: ItemProfileSettingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProfileSettingItemModel, holder: ViewHolder) {

            binding.tvTitle.text = item.title
            binding.ivItem.setImageResource(item.icon)

            holder.itemView.setOnClickListener {
                callBack(adapterPosition)
            }

        }
    }


    override fun getItemCount(): Int = pSettingList.size
}