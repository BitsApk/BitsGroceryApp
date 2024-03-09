package com.bitspan.bitsjobkaro.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.enums.JobFragInfoEnum
import com.bitspan.bitsjobkaro.data.constants.enums.JobFragInfoEnum.*
import com.bitspan.bitsjobkaro.databinding.ItemJobAppliedBinding

class RecJobListAdapter(val type: JobFragInfoEnum): RecyclerView.Adapter<RecJobListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemJobAppliedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
//            binding.jobExpSubTxt.text = context.getString(R.string.exp_years, 1.02f)
//            binding.jobAnnExpSalTxt.text = context.getString(R.string.ann_income, 1, 3)
        }

        fun navigateToRecPage() {
            binding.root.findNavController().navigate(R.id.recruiterPageFragment)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJobAppliedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(type) {
            JobApplied -> {

            }
            JobSaved -> {
                holder.binding.jobStarImg.setImageResource(R.drawable.icon_start_filled)
            }
            JobViewed -> {
                holder.binding.jobStarImg.setImageResource(R.drawable.icon_viewed)
            }
        }
        holder.binding.rootLayout.setOnClickListener {
            holder.navigateToRecPage()
        }
    }

    override fun getItemCount(): Int = 4
}