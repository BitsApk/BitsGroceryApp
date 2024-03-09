package com.bitspan.bitsjobkaro.presentation.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.enums.RecFragInfoEnum
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.models.recruiter.ShortListData
import com.bitspan.bitsjobkaro.databinding.ItemHomeJobSeekersBinding

class EmpListAdapter(
    private val empList: List<RecruiterEmpData>,
    val type: RecFragInfoEnum,
    val context: Context,
    val shortList: MutableList<ShortListData> = mutableListOf(),
    val callback: (empId: Int, type: Int, pos: Int) -> Any    // 1 to shortlist, 2 to resume
) : RecyclerView.Adapter<EmpListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHomeJobSeekersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecruiterEmpData) {
            binding.jobNameTxt.text = item.cName
            binding.jobHighQualTxt.text = CommonDataFunctions.getFormattedEmpQualif(item.highestQual)
            binding.jobExpSubTxt.text = CommonDataFunctions.getFormattedExpYr(item.tExpYr, item.tExpMonth)
            binding.jobAnnExpSalTxt.text = CommonDataFunctions.getFormattedSalary(item.pSalLow, item.pSalTop)
            binding.jobTimeTypeTxt.text = CommonDataFunctions.checkJobTimeType(item.pEType)
            binding.jobRolesTxt.text = item.pJobRoles
            binding.jobTypeTxt.text = CommonDataFunctions.checkJobType(item.eTYpe)
            binding.jobExpCompTxt.text = CommonDataFunctions.getFormattedEng(item.eng)
            binding.jobEmpDescTxt.text = item.empDescription
            if (item.isShortListed) binding.jobStarImg.setImageResource(R.drawable.icon_start_filled)
            else {
                for (emp in shortList) {
                    if (item.empId == emp.id) {
                        item.isShortListed = true
                        binding.jobStarImg.setImageResource(R.drawable.icon_start_filled)
                    }
                }
                if (!item.isShortListed)  binding.jobStarImg.setImageResource(R.drawable.icon_star)
            }
            binding.progBar.visibility = if (item.showProg) View.VISIBLE else View.INVISIBLE
        }

        fun layoutClickEvents(item: RecruiterEmpData) {
            binding.jobDownArrowImg.setOnClickListener {
                val visible = binding.jobSecLayout.visibility
                binding.jobSecLayout.visibility = if (visible == View.GONE) {
                    binding.jobDownArrowImg.setImageResource(R.drawable.icon_up_arrow)
                    binding.jobSecLayout.animation =
                        AnimationUtils.loadAnimation(context, R.anim.layout_motion_down)
                    View.VISIBLE
                } else {
                    binding.jobDownArrowImg.setImageResource(R.drawable.icon_down_arrow)
                    View.GONE
                }
            }

            // Layout click to open emp page
            binding.jobRootLayout.setOnClickListener {
                item.empId?.let { it1 -> navigateToEmpPage(it1.toInt(), item.isShortListed)}
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
                for (emp in 0 until shortList.size ) {
                    if (shortList[emp].id == item.empId) {
                        index = emp
                        break
                    }
                }
                if (index != -1) shortList.removeAt(index) else shortList.add(ShortListData(cName = "${item.cName}", id = item.empId))
                item.empId?.let { it1 -> callback(it1.toInt(), 1, -1) }
            }


            // Layout click to open emp page
            binding.jobChatBtn.setOnClickListener {
                item.empId?.let { it1 -> navigateToChat(it1.toInt(), item.cName ?: "")}
            }

            // Layout click to open emp page
            binding.jobReqResuBtn.setOnClickListener {
                item.showProg = true
                binding.progBar.visibility = View.VISIBLE
                item.empId?.let { it1 -> callback(it1.toInt(), 2, absoluteAdapterPosition)}
            }

        }

        private fun navigateToChat(empId: Int, name: String) {
            val bundle = Bundle()
            bundle.putInt("userToChatId", empId)
            bundle.putString("name", name)
            val navBuilder = NavOptions.Builder().setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out).setPopExitAnim(
                android.R.anim.fade_out).build()
            binding.root.findNavController().navigate(R.id.chatFragment, bundle, navBuilder)
        }

        private fun navigateToEmpPage(empId: Int, shortListed: Boolean) {
            val bundle = Bundle()
            bundle.putInt("empId", empId)
            bundle.putBoolean("isShortListed", shortListed)
            val navBuilder = NavOptions.Builder().setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out).setPopExitAnim(
                android.R.anim.fade_out).build()
            binding.root.findNavController().navigate(R.id.employeePageFragment, bundle, navBuilder)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomeJobSeekersBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (type) {
            RecFragInfoEnum.RecSaved -> {
                holder.binding.jobStarImg.apply {
                    setImageResource(R.drawable.icon_start_filled)
                    tag = 1
                }
            } else -> {}
        }
        val item = empList[position]
        holder.bind(item)
        holder.layoutClickEvents(item)


    }

    override fun getItemCount(): Int = empList.size
}