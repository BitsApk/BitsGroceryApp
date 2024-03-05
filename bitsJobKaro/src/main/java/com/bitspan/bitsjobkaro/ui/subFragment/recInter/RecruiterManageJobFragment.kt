package com.bitspan.bitsjobkaro.ui.subFragment.recInter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.startShimmer
import com.bitspan.bitsjobkaro.CommonUiFunctions.stopShimmer
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.databinding.FragmentRecruiterManageJobBinding
import com.bitspan.bitsjobkaro.presentation.adapters.ManageJobListAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecruiterManageJobFragment : Fragment() {

    private lateinit var binding: FragmentRecruiterManageJobBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var manageJobListAdapter: ManageJobListAdapter
    private val recruiterManageViewModel: RecruiterManageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentRecruiterManageJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startShimmer(binding.rPostShimmer, binding.jobManageRecyView)

        val isFromProfile = arguments?.getBoolean("profile") ?: false
        binding.jobManageTitleTxt.visibility = if (isFromProfile) {
            CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
            View.VISIBLE
        } else View.GONE

        viewLifecycleOwner.lifecycleScope.launch{
            try {
                recruiterManageViewModel.getPostedJobList(userId.toInt()).let {
                    stopShimmer(binding.rPostShimmer, binding.jobManageRecyView)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            val postedList = it.body()!!.data ?: listOf()
                            manageJobListAdapter = ManageJobListAdapter(postedList, mContext) {jobId ->
                                callPostJobStatusApi(jobId)
                            }
                            setManageListAdapter()
                            binding.noDataLay.clNodataFound.visibility = View.GONE

                        } else {
                            binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext, "Some error in getting posted jobs, Check your internet"
                        ) {
                        }
                        binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                CommonUiFunctions.showErrorMsgDialog(
                    mContext, "Some technical error in getting posted jobs, Check your internet"
                ) {
                }
                binding.noDataLay.clNodataFound.visibility = View.VISIBLE
            }

        }
    }


    private fun setManageListAdapter() {
        binding.jobManageRecyView.apply {
            adapter = manageJobListAdapter
            layoutManager = LinearLayoutManager(mContext)
        }
    }

    private fun callPostJobStatusApi(jobId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            recruiterManageViewModel.callPostJobStatusApi(jobId).let {
                Log.d(LOG_TAG, "Posted Job: $jobId: ${it.body()}")
            }
        }
    }

}