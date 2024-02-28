package com.bitspan.bitsjobkaro.presentation.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.models.recruiter.RecNotification
import com.bitspan.bitsjobkaro.databinding.ItemRecAppliedViewedBinding

class RecViewedByAdapter(
    private val empJobList: List<RecNotification>,
    val type: Int,  // 0 then applied, 1 then viewed
    val context: Context,
    val callback: (jobId: Int) -> Any
) : RecyclerView.Adapter<RecViewedByAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemRecAppliedViewedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jobItem: RecNotification, context: Context, type: Int) {
            binding.apply {
                titleTxt.text = context.getString(R.string.emp_name, jobItem.empName)
                highQualTxt.text = jobItem.tExpType
                expValueTxt.text = CommonDataFunctions.getFormattedExpYr(jobItem.tExpYr, jobItem.tExpMonth)
                empJobNameTxt.text = jobItem.jTitle?.capitalize()
                recJobTitleTxt.text = jobItem.pJobRoles?.capitalize()
                recJobAnnSalTxt.text = CommonDataFunctions.getFormattedSalary(jobItem.sMin, jobItem.sMax)
                recJobLocTxt.text = jobItem.pCities?.capitalize()
                jobStatusTxt.text = if (type == 0) context.getString(R.string.applied) else context.getString(R.string.viewed)
            }
        }

        fun layoutClick(empId: Int) {
            val bundle = Bundle()
            bundle.putInt("empId", empId)
            binding.root.findNavController().navigate(R.id.employeePageFragment, bundle)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecAppliedViewedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jobItem = empJobList[position]
        holder.bind(jobItem, context, type)
        holder.itemView.setOnClickListener {
            jobItem.empId?.let { holder.layoutClick(it.toInt()) }
        }
    }

    override fun getItemCount(): Int = empJobList.size

}