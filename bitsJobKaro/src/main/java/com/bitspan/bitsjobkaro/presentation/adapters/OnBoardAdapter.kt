package com.bitspan.bitsjobkaro.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.data.models.OnBoardData
import com.bitspan.bitsjobkaro.databinding.ItemOnboardingScreenBinding

class OnBoardAdapter (val imageList: List<OnBoardData>) : RecyclerView.Adapter<OnBoardAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemOnboardingScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item:OnBoardData){
                binding.imageView.setImageResource(item.image)
                binding.title.text = item.title
                binding.desc.text = item.desc
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOnboardingScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val item  = imageList[position]
            holder.bind(item)
        }

    override fun getItemCount(): Int = imageList.size
}



