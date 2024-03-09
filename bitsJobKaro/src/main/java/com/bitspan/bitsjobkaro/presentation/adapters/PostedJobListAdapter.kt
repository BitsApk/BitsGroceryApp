package com.bitspan.bitsjobkaro.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.data.models.recruiter.PostedJobData
import com.bitspan.bitsjobkaro.databinding.ItemRecHomePostedJobBinding

class PostedJobListAdapter(
    val postedJobList: List<PostedJobData>,
    val callback: (jobId: Int, chipList: List<String>) -> Any
) : RecyclerView.Adapter<PostedJobListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecHomePostedJobBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jobItem: PostedJobData) {
            binding.apply {
                jobTitleTxt.text = jobItem.jTitle
                jobHighQualTxt.text = CommonDataFunctions.getFormattedQualif(jobItem.quali)
                jobExpSubTxt.text = CommonDataFunctions.getFormattedExp(jobItem.experience)
                jobAppAnnSalTxt.text = CommonDataFunctions.getFormattedSalary(jobItem.sMin, jobItem.sMax)
                jobTypeTxt.text = CommonDataFunctions.checkJobType(jobItem.empType)
                jobTimeTypeTxt.text = CommonDataFunctions.checkJobTimeType(jobItem.jobtype)
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecHomePostedJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jobItem = postedJobList[position]
        holder.bind(jobItem)

        holder.binding.rootLayout.setOnClickListener {
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