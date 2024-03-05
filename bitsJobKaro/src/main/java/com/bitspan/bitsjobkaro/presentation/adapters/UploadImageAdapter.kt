package com.bitspan.bitsjobkaro.presentation.adapters

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.databinding.ItemImageUploadBinding
import java.util.*

class UploadImageAdapter(val imageList: TypedArray, val callback: (imgPos: Int) -> Any) :
    RecyclerView.Adapter<UploadImageAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemImageUploadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Drawable?){
            binding.rProfDefImage.setImageDrawable(item)

            binding.rProfDefImage.setOnClickListener {
                callback (absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemImageUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = imageList.length()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = imageList.getDrawable(position)
        holder.bind(item)

    }
}