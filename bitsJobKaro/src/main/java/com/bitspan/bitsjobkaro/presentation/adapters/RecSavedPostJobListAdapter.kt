package com.bitspan.bitsjobkaro.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.data.models.recruiter.PostedJobData
import com.bitspan.bitsjobkaro.databinding.ItemRecSavedPostListBinding

class RecSavedPostJobListAdapter(
    val postedJobList: List<PostedJobData>,
    val callback: (jobId: Int, chipList: List<String>) -> Any
) : RecyclerView.Adapter<RecSavedPostJobListAdapter.ViewHolder>() {

    private var lastCheckedPosition: Int? = null

    class ViewHolder(val binding: ItemRecSavedPostListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jobItem: PostedJobData) {
            binding.apply {
                rPostNameTxt.text = jobItem.jTitle
                rPostSalaryTxt.text = CommonDataFunctions.getFormattedSalary(jobItem.sMin, jobItem.sMax)
                rPostJobType.text = CommonDataFunctions.checkJobType(jobItem.empType)
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecSavedPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jobItem = postedJobList[position]
        holder.bind(jobItem)

        if (lastCheckedPosition != null) {
            holder.binding.rPostRadioBtn.isChecked = lastCheckedPosition == position
        }

        holder.binding.rPostListItemLayout.setOnClickListener {
            holder.binding.rPostRadioBtn.isChecked = true
            lastCheckedPosition = holder.absoluteAdapterPosition
            callback(jobItem.jobId.toInt(), listOf(
                jobItem.jTitle,
                CommonDataFunctions.getFormattedSalary(jobItem.sMin, jobItem.sMax),
                CommonDataFunctions.checkJobType(jobItem.empType),
                CommonDataFunctions.checkJobTimeType(jobItem.jobtype)
            ))
        }

    }

    override fun getItemCount(): Int = postedJobList.size

}