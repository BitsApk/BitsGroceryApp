package com.bitspan.bitsjobkaro.ui.subFragment.recInter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.startShimmer
import com.bitspan.bitsjobkaro.CommonUiFunctions.stopShimmer
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.models.recruiter.RecNotification
import com.bitspan.bitsjobkaro.databinding.FragmentRecruiterViewedByBinding
import com.bitspan.bitsjobkaro.presentation.adapters.RecViewedByAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecruiterViewedByFragment : Fragment() {

    private lateinit var binding: FragmentRecruiterViewedByBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val recManageViewModel: RecruiterManageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentRecruiterViewedByBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startShimmer(binding.rEmpApplyShimmer, binding.rApplyRecyView)
        getAppliedData()
        getViewedData()
    }

    private fun getViewedData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recManageViewModel.getViewedData(userId.toInt()).let {
                    stopShimmer(binding.rEmpApplyShimmer, binding.rViewedRecyView)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            val viewedList = it.body()?.jobViewed ?: listOf()
                            setViewedAdapter(viewedList)
                            binding.noDataLaySec.clNodataFound.visibility = View.GONE
                        } else {
                            binding.noDataLaySec.clNodataFound.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext, "Some error in getting viewed by data, Check your internet"
                        ) {
                        }
                        binding.noDataLaySec.clNodataFound.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                CommonUiFunctions.showErrorMsgDialog(
                    mContext, "Some technical error in getting viewed by data, Check your internet"
                ) {
                }
                binding.noDataLaySec.clNodataFound.visibility = View.VISIBLE

            }
        }
    }

    private fun getAppliedData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recManageViewModel.getViewedData(userId.toInt()).let {
                    stopShimmer(binding.rEmpApplyShimmer, binding.rApplyRecyView)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status  == 200) {
                            val appliedList = it.body()?.jobApplied ?: listOf()
                            setAppliedAdapter(appliedList)
                            binding.noDataLay.clNodataFound.visibility = View.GONE
                        } else {
                            binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext, "Some error in getting applied by data, Check your internet"
                        ) {
                        }
                        binding.noDataLay.clNodataFound.visibility = View.VISIBLE

                    }
                }
            } catch (e: Exception) {
                CommonUiFunctions.showErrorMsgDialog(
                    mContext, "Some technical error in getting applied data, Check your internet"
                ) {
                }
                binding.noDataLay.clNodataFound.visibility = View.VISIBLE

            }

        }
    }

    private fun setAppliedAdapter(appliedList: List<RecNotification>) {
        binding.rApplyRecyView.adapter = RecViewedByAdapter(appliedList, 0, mContext) {}

    }

    private fun setViewedAdapter(viewedList: List<RecNotification>) {
        binding.rViewedRecyView.adapter = RecViewedByAdapter(viewedList, 1, mContext) {}
    }

}