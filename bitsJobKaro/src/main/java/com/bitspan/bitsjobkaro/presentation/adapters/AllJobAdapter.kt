package com.bitspan.bitsjobkaro.presentation.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.models.AllJobData
import com.bitspan.bitsjobkaro.data.models.employee.EmpSavedBookmarkData
import com.bitspan.bitsjobkaro.databinding.ItemJobAppliedBinding


class AllJobAdapter(
    val jobList: List<AllJobData>,
    val shortList: MutableList<EmpSavedBookmarkData> = mutableListOf(),
    val callback: (jobId: Int) -> Any
) : RecyclerView.Adapter<AllJobAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemJobAppliedBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: AllJobData) {
            binding.jobTitleTxt.text = item.jobTitle
            binding.jobHighQualTxt.text = CommonDataFunctions.getFormattedQualif(item.jobQualification)
            binding.jobExpSubTxt.text = CommonDataFunctions.getFormattedExp(item.jobExperience)
            binding.jobAppAnnSalTxt.text = CommonDataFunctions.getFormattedSalary(item.jobExMinSalary,item.jobExMaxSalary)
            binding.jobTypeTxt.text = CommonDataFunctions.checkJobType(item.jobEmpType)
            binding.jobTimeTypeTxt.text = CommonDataFunctions.checkJobTimeType(item.jobJobType)
            binding.jobExpCompTxt.text = "${item.jobDayFrom} - ${item.jobDayTo}"
            binding.jobEmpDescTxt.text = item.jobDescription
            binding.jobExpDesignTxt.text = CommonDataFunctions.getFormattedEng(item.jobEnglishLabel)
            binding.jobAppNameTxt.text = CommonDataFunctions.getFormattedShifts(item.jobShift)
            if (item.isShortListed) binding.jobStarImg.setImageResource(R.drawable.icon_start_filled)
            else {
                for (job in shortList) {
                    if (item.jobId == job.id) {
                        item.isShortListed = true
                        binding.jobStarImg.setImageResource(R.drawable.icon_start_filled)
                    }
                }
                if (!item.isShortListed)  binding.jobStarImg.setImageResource(R.drawable.icon_star)

            }
        }


        fun layoutClickEvents(item: AllJobData) {
            binding.rootLayout.setOnClickListener {
                navigateToRecPage(item)
            }

            // Shortlisting
            binding.jobStarImg.setOnClickListener {
                if (item.isShortListed) {
                    binding.jobStarImg.apply {
                        setImageResource(R.drawable.icon_star)
                        item.isShortListed = false
                    }
                } else {
                    binding.jobStarImg.apply {
                        setImageResource(R.drawable.icon_start_filled)
                        item.isShortListed = true
                    }
                }
                var index = -1
                for (recJobIndex in 0 until shortList.size) {
                    if (shortList[recJobIndex].id == item.jobId) {
                        index = recJobIndex
                        break
                    }
                }
                if (index != -1) shortList.removeAt(index) else shortList.add(
                    EmpSavedBookmarkData(
                        title = "${item.jobTitle}",
                        id = item.jobId
                    )
                )
                item.jobId?.let { it1 -> callback(it1.toInt()) }
            }

        }


        private fun navigateToRecPage(item: AllJobData) {
            val bundle = Bundle()
            bundle.putString("recId", item.jobCreatedBy)
            bundle.putString("jobId", item.jobId)
            val navBuilder = NavOptions.Builder().setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out).setPopExitAnim(
                android.R.anim.fade_out).build()
            binding.root.findNavController().navigate(R.id.recruiterPageFragment, bundle, navBuilder)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJobAppliedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = jobList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemJobModel = jobList[position]
        holder.bind(itemJobModel)
        holder.layoutClickEvents(itemJobModel)

    }

}