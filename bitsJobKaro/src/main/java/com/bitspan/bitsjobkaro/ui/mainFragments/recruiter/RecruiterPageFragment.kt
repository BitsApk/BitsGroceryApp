package com.bitspan.bitsjobkaro.ui.mainFragments.recruiter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.chip.Chip
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList.recChipGroupList
import com.bitspan.bitsjobkaro.data.models.employee.EmpApplyJobReq
import com.bitspan.bitsjobkaro.data.models.recruiter.RecProfileData
import com.bitspan.bitsjobkaro.databinding.FragmentRecruiterPageBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.AllJobViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.JobDetailsViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecruiterPageFragment : Fragment() {

    private lateinit var binding: FragmentRecruiterPageBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val recPageArgs: RecruiterPageFragmentArgs by navArgs()

    private val jobDetailsViewModel: JobDetailsViewModel by viewModels()
    private val allJobViewModel: AllJobViewModel by viewModels()
    private val recViewModel: RecruiterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentRecruiterPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)

        binding.recBackArrImg.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.recApplyBtn.setOnClickListener {
            applyJob()
        }

        binding.recChatBtn.setOnClickListener {
            val action = RecruiterPageFragmentDirections.actionRecruiterPageFragmentToChatFragment(recPageArgs.recId.toInt(),binding.recExpDesignTxt.text.toString())
            findNavController().navigate(action)
        }

//        for (head in recChipGroupList) {
//            val chip = Chip(mContext)
//            chip.text = head
//            binding.recChipGroup.addView(chip)
//        }

        getProfData()
        getJobDetails()

    }

    private fun getProfData() {
        startShimmer()
        viewLifecycleOwner.lifecycleScope.launch {
            recViewModel.getRecProfileData(recPageArgs.recId.toInt()).let {

                if (it.isSuccessful && it.body() != null) {
                    stopShimmer()
                    if (it.body()!!.status == 200) {
                        val recProfileData = it.body()!!.data?.get(0) ?: RecProfileData()
                        val  profImageUrl = "${it.body()!!.imgPath}${recProfileData.cLogo}"
                        binding.recExpDesignTxt.text = recProfileData.rName
                        binding.recAppNameTxt.text = recProfileData.rDesig
                        binding.recExpCompTxt.text = recProfileData.name

                        Glide.with(mContext)
                            .load(profImageUrl)
                            .placeholder(R.drawable.avataar_placeholder) // Placeholder image while loading
                            .centerCrop() // Center-crop the image
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image in both memory and disk
                            .into(binding.recProfImg)

                    } else {
                        Toast.makeText(mContext, it.body()!!.mess, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(Constant.LOG_TAG, "${it.errorBody()}")
                }
            }
        }
    }

    private fun getJobDetails() {
        startShimmer()
        viewLifecycleOwner.lifecycleScope.launch {
            jobDetailsViewModel.getJobDetails(recPageArgs.jobId.toInt()).let {
                if (it.isSuccessful && it.body() != null) {
                    stopShimmer()
                    if (it.body()?.status == 200) {
                        val data = it.body()?.data?.get(0)
                        binding.recTitleTxt.text = data?.jobTitle
                        binding.recHighQualTxt.text = CommonDataFunctions.getFormattedQualif(data?.jobQualification)
                        binding.jobExpSubTxt.text = CommonDataFunctions.getFormattedExp(data?.jobExperience)
                        binding.recAppAnnSalTxt.text = getString(R.string.salary_n_rs_2_6_lpa, CommonDataFunctions.getFormattedSalary(
                            data?.jobExMinSalary,
                            data?.jobExMaxSalary
                        ))
                        binding.recJobTypeTxt.text = CommonDataFunctions.checkJobType(data?.jobEmpType)
                        binding.recTimeTypeTxt.text = getString(R.string.job_type_nfull_time,CommonDataFunctions.checkJobTimeType(data?.jobJobType))
                        binding.recNumPosTxt.text = getString(R.string.positions_n4_pos,data?.noOfOpenings)
                        binding.recJobDesTxt.text = data?.jobDescription
                        binding.recLocationTxt.text = getString(R.string.four_str,data?.jobCity," | ${data?.jobState}"," | ${data?.jobZip}","\n${data?.jobAddress}")
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            it.body()?.mess?:"Something went wrong"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                } else {
                    CommonUiFunctions.showErrorMsgDialog(
                        mContext,
                        "Something went wrong"
                    ) {
                        findNavController().popBackStack()
                    }
                }
            }

        }
    }

    private fun applyJob() {
        startShimmer()
        val req = EmpApplyJobReq()
        req.empId = Constant.userId.toInt()
        req.jobId = recPageArgs.jobId.toInt()
        req.recId = recPageArgs.recId.toInt()
        viewLifecycleOwner.lifecycleScope.launch {
            allJobViewModel.empApplyJob(req).let {
                if (it.isSuccessful && it.body() != null) {
                    stopShimmer()
                    if (it.body()?.status == 200) {
                        Toast.makeText(mContext,it.body()?.message?:"Something went wrong",Toast.LENGTH_LONG).show()
                        binding.recApplyBtn.text = "Applied"
                        binding.recApplyBtn.isFocusable = false
                        binding.recApplyBtn
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            it.body()?.message?:"Something went wrong"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                } else {
                    CommonUiFunctions.showErrorMsgDialog(
                        mContext,
                        "Something went wrong"
                    ) {
                        findNavController().popBackStack()
                    }
                }
            }

        }
    }

    private fun startShimmer(){
        binding.scrollView.visibility = View.GONE
        binding.recChatBtn.visibility = View.GONE
        binding.recApplyBtn.visibility = View.GONE
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmer()
    }

    private fun stopShimmer(){
        binding.scrollView.visibility = View.VISIBLE
        binding.recChatBtn.visibility = View.VISIBLE
        binding.recApplyBtn.visibility = View.VISIBLE
        binding.shimmer.visibility = View.GONE
        binding.shimmer.stopShimmer()
    }

}