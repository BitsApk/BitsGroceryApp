package com.bitspan.bitsjobkaro.ui.mainFragments.employee

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.showErrorMsgDialog
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.models.ProfileImageReq
import com.bitspan.bitsjobkaro.data.models.employee.AddCurrentCompanyReq
import com.bitspan.bitsjobkaro.data.models.employee.AllPreCom
import com.bitspan.bitsjobkaro.data.models.employee.CurrDetail
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.databinding.FragmentEmployeePageBinding
import com.bitspan.bitsjobkaro.presentation.adapters.EmpCompDetailAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpCarPrefViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EmployeePageFragment : Fragment() {

    private lateinit var binding: FragmentEmployeePageBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val recruiterViewModel: RecruiterViewModel by viewModels()
    private val empCarViewModel: EmpCarPrefViewModel by viewModels()
    private val otherVM: OtherViewModel by viewModels()
    private val recPostViewModel: RecruiterManageViewModel by viewModels()
    private val empArgs: EmployeePageFragmentArgs by navArgs()
    private var isShortListed = false
    private var isBasicFound = false
    private var isCareerFound = false
    private var isExpFound = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEmployeePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        getEmpBasicDetail(empArgs.empId)
        getEmpCareerPref(empArgs.empId)
        getEmpExpData(empArgs.empId)
        getEmpProfileImage(empArgs.empId)

        binding.empMenuImg.setOnClickListener {
            showMenu(it, R.menu.action_option_menu)
        }

        binding.empBackArrImg.setOnClickListener {
            findNavController().popBackStack()
        }


        if (empArgs.isShortListed) {
            isShortListed = true
            binding.empStarImg.setImageResource(R.drawable.icon_start_filled)
        }
        binding.empStarImg.setOnClickListener {
            if (isShortListed) {
                binding.empStarImg.setImageResource(R.drawable.icon_star)
                isShortListed = false

            } else {
                binding.empStarImg.setImageResource(R.drawable.icon_start_filled)
                isShortListed = true
            }
            callShortListApi(empId = empArgs.empId)
        }

    }

    private fun getEmpProfileImage(empId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.getEmpProfileImage(ProfileImageReq(employeeId = empId, recId = null)).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.status == 200) {
                        Glide.with(this@EmployeePageFragment)
                            .load(it.body()!!.profile)
                            .placeholder(R.drawable.avataar_placeholder) // Placeholder image while loading
                            .centerCrop() // Center-crop the image
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image in both memory and disk
                            .into(binding.homeProfImg)
                    }
                }
            } catch (_: Exception) {

            }
        }
    }

    private fun callShortListApi(empId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recPostViewModel.callShortListApi(empId, Constant.userId.toInt())
            } catch (_: Exception) {
            }
        }
    }

    private fun getEmpBasicDetail(empId: Int) {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                recruiterViewModel.getEmpBasicData(empId).let {
                    isBasicFound = true
                    stopShimmer()
                    if (it.isSuccessful && it.body() != null && it.body()?.status == 200) {
                        val details = it.body()?.data?.get(0)
                        val profImageUrl = "" // ""${it.body()!!.imgPath}${details?.uploadPic}"
                        Glide.with(this@EmployeePageFragment)
                            .load(profImageUrl)
                            .placeholder(R.drawable.avataar_placeholder) // Placeholder image while loading
                            .centerCrop() // Center-crop the image
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image in both memory and disk
                            .into(binding.homeProfImg)
                        setEmpBasicData(details)
                    } else {
                        showErrorMsgDialog(mContext, "Employee basic detail not found, please try again later") {
//                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            isBasicFound = true
            stopShimmer()
            showErrorMsgDialog(mContext, "Employee basic detail not properly filled, please try again later") {
//                findNavController().popBackStack()
            }
        }
    }

    private fun stopShimmer() {
        if (isBasicFound && isCareerFound && isExpFound) {
            binding.apply {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                empParentLay.visibility = View.VISIBLE
            }
        }
    }

    private fun setEmpBasicData(details: RecruiterEmpData?) {
        binding.apply {
            empNameTxt.text = details?.cName
            empHighQualTxt.text = CommonDataFunctions.getFormattedQualif(details?.highestQual)
            empExpSubTxt.text = CommonDataFunctions.getFormattedExpYr(details?.tExpYr, details?.tExpMonth)
            empAgeTxt.text = getString(R.string.format_age, details?.dateDiff)
            empEngTxt.text = CommonDataFunctions.getFormattedEng(details?.eng)
        }
    }

    private fun setChipData(chipList: List<String>) {
        for (text in chipList) {
            binding.empChipGroup.addView(CommonUiFunctions.createChip(text, R.color.chip_bg_greyish, mContext))
        }
    }

    private fun getEmpCareerPref(empId: Int) {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                empCarViewModel.getEmpCarPrefData(empId).let {
                    isCareerFound = true
                    stopShimmer()
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            val response = it.body()?.data?.get(0)
                            val skillList: List<String> = response?.empSkills?.split(",") ?: listOf()
                            setChipData(skillList)
                            binding.apply {
                                empJobTitleTxt.text = response?.empJobRoles
                                empJobExpTxt.text = response?.empTypeExp
                                empAppAnnSalTxt.text = CommonDataFunctions.getFormattedSalary(response?.empSalaryLakh, response?.empSalaryThousand)
                                empJobTypeTxt.text = CommonDataFunctions.checkJobType(response?.empTypeOH)
                                empJobTimeTypeTxt.text = CommonDataFunctions.checkJobTimeType(response?.empPrefEmpType)
                                empJobCityTxt.text = response?.empPrefCities
                                empJobStateTxt.text = response?.empState
                                empJobDescTxt.text = response?.empObjective
                                empJobRoleTxt.text = response?.empJobRoles
                                jobExpSubTxt.text = CommonDataFunctions.getFormattedExpYr(response?.empExpYear, response?.empExpMonth)
                            }
                        } else {
                            showErrorMsgDialog(mContext, "Employee career preference not found") {
//                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            isCareerFound = true
            stopShimmer()
                showErrorMsgDialog(mContext, "Error in fetching career preference, please try again later") {
//                    findNavController().popBackStack()
            }
        }
    }

    private fun getEmpExpData(empId: Int) {
        val req = AddCurrentCompanyReq()
        req.empId = empId

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.getAllProfessDetails(req).let {
                    isExpFound = true
                    stopShimmer()
                    if (it.isSuccessful && it.body() != null && it.body()?.status == 200 ) {

                        val currComDetail = it.body()?.currDetails ?: listOf() // it.body()?.data?.newPDetails ?: listOf()
                        setCurrCompDetail(currComDetail)
                        val prevCompList = it.body()?.allPreCom ?: listOf()
                        setPrevComDetail(prevCompList)
                    }  else {
                        showErrorMsgDialog(mContext, "Employee experience detail not found, please try again later") {
//                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                isExpFound = true
                stopShimmer()
                showErrorMsgDialog(mContext, "Some technical error in getting employee experience detail, please try again later") {
//                            findNavController().popBackStack()
                }
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(mContext, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.reqResume -> {
                    Log.d(LOG_TAG, "Emp page req resume ")
                    true
                }
                else -> {
                    Log.d(LOG_TAG, "Emp page req number ")
                    true
                }
            }
        }

        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

    private fun setPrevComDetail(prevCompList: List<AllPreCom>?) {
        if (prevCompList.isNullOrEmpty()) {
            binding.empPrevComRecView.visibility = View.GONE
            binding.empCurrPrevTxt.visibility = View.GONE
            binding.empExpField.visibility = View.GONE
        } else {
            binding.empPrevComRecView.adapter = EmpCompDetailAdapter(prevCompList, mContext)
        }
    }

    private fun setCurrCompDetail(currCompPDetail: List<CurrDetail>?) {
        if (currCompPDetail.isNullOrEmpty()) {
            binding.empCompExpLayout.visibility = View.GONE
        } else {
            binding.currComItem.apply {
                empCurrCompNameTxt.text = currCompPDetail[0].currComName
                empCurrJobDateTxt.text = getString(R.string.date_format, currCompPDetail[0].currDFrom, currCompPDetail[0].currDTo)
                empCurrDesignTxt.text = currCompPDetail[0].currComDesig
                empCurrNoticePeriod.text = getString(R.string.notice_period, currCompPDetail[0].currNotice)
            }
        }

    }
}