package com.bitspan.bitsjobkaro.presentation.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.models.recruiter.PostedJobData
import com.bitspan.bitsjobkaro.databinding.ItemManageJobBinding
import com.bitspan.bitsjobkaro.ui.subFragment.recInter.RecruiterManageJobFragmentDirections

class ManageJobListAdapter(
    private val postedJobList: List<PostedJobData>,
    val context: Context,
    val callback: (jobId: Int) -> Any  // 1 for switching post job, 2 for see all response
) : RecyclerView.Adapter<ManageJobListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemManageJobBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostedJobData) {
            val date = item.createdAt?.split(" ")?.get(0)
            binding.apply {
                jobPostedTxt.text = context.getString(R.string.posted_at_s, date)
                jobTitleTxt.text = item.jTitle
                jobHighQualTxt.text = CommonDataFunctions.getFormattedQualif(item.quali)
                jobTypeTxt.text = CommonDataFunctions.checkJobType(item.empType)
                jobTimeTypeTxt.text = CommonDataFunctions.checkJobTimeType(item.jobtype)
                jobExpCompTxt.text = CommonDataFunctions.getFormattedShifts(item.shift)
                jobCompLocatTxt.text = item.city
                jobAppAnnSalTxt.text = CommonDataFunctions.getFormattedSalary(item.sMin, item.sMax)
                jobResponsesTxt.text =
                    context.getString(R.string.num_responses, item.numberOfResponse?.toInt())
                jobExpSubTxt.text = CommonDataFunctions.getFormattedExp(item.experience)
            }

            binding.jobSeeAllTxt.setOnClickListener {
                    val jobDetails = arrayOf(item.jTitle, item.sMin, item.sMax, item.experience)
//                    jobDetails.add(item.jTitle)
//                    jobDetails.add(item.sMin ?: "0")
//                    jobDetails.add(item.sMax ?: "0")
//                    jobDetails.add(item.experience ?: "0")
                val bundle = Bundle()
                bundle.putInt("jobId", item.jobId.toInt())
                bundle.putStringArray("jobDetails", jobDetails)
//                    val directions = RecruiterManageJobFragmentDirections.actionRecruiterManageJobFragmentToEmpAppliedJobFragment(
//                        jobId = item.jobId.toInt(),
//                        jobDetails = jobDetails.toTypedArray()
//                    )
                    binding.root.findNavController().navigate(R.id.empAppliedJobFragment, bundle)
                }


        }


        fun setStatusText(status: Boolean) {
            binding.jobOpenClosedTxt.text = if (status) {
                binding.jobOpenClosedSwitch.isChecked = true
                context.getString(R.string.opened)
            } else {
                binding.jobOpenClosedSwitch.isChecked = false
                context.getString(R.string.closed)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemManageJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = postedJobList[position]
        holder.bind(item)
        holder.setStatusText(item.status == "0")
        holder.binding.jobOpenClosedSwitch.setOnClickListener {
            val isChecked = item.status != "0"  // setting isChecked after click
            postedJobList[position].status = if (isChecked) "0" else "1"
            notifyItemChanged(position)
            holder.setStatusText(isChecked)
            callback(item.jobId.toInt())
        }
    }

    override fun getItemCount(): Int = postedJobList.size
}