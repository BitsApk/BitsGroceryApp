package com.bitspan.bitsjobkaro.ui.subFragment.recInter

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
import com.google.android.material.sidesheet.SideSheetDialog
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.startShimmer
import com.bitspan.bitsjobkaro.CommonUiFunctions.stopShimmer
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.enums.RecFragInfoEnum
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.models.recruiter.ShortListData
import com.bitspan.bitsjobkaro.databinding.FragmentRecruiterSavedBinding
import com.bitspan.bitsjobkaro.databinding.SideSheetRecSavedBinding
import com.bitspan.bitsjobkaro.presentation.adapters.ChatTypes
import com.bitspan.bitsjobkaro.presentation.adapters.EmpListAdapter
import com.bitspan.bitsjobkaro.presentation.adapters.RecSavedPostJobListAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.ChatViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecruiterSavedFragment : Fragment() {

    private lateinit var binding: FragmentRecruiterSavedBinding
    private lateinit var sideSheetBinding: SideSheetRecSavedBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var empListAdapter: EmpListAdapter
    private lateinit var postJobListAdapter: RecSavedPostJobListAdapter
    private lateinit var sideSheet: SideSheetDialog
    private val recManageViewModel: RecruiterManageViewModel by viewModels()
    private val recVM: RecruiterViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var shortList: MutableList<ShortListData>
//    private var stackFlag: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireContext()
        mActivity = requireActivity()
        sideSheet = SideSheetDialog(mContext)
        binding = FragmentRecruiterSavedBinding.inflate(inflater, container, false)
        sideSheetBinding = SideSheetRecSavedBinding.inflate(LayoutInflater.from(mContext))
        sideSheet.setContentView(sideSheetBinding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.VISIBLE)
        getAllShortlisted()
        getPostedJobs()


        binding.rSavedFilterCandTxt.setOnClickListener {
            sideSheet.show()
        }

    }

    private fun getPostedJobs() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recManageViewModel.getPostedJobList(userId.toInt()).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            val list = it.body()!!.data ?: listOf()
                            postJobListAdapter = RecSavedPostJobListAdapter(list) { jobId, chipList ->
                                getAllSavedCan(jobId)
                                setChipData(chipList)
                                sideSheet.hide()
                            }
                            setSideSheetAdapter()
                        }  else {}
                    } else {
                        // Toast is ok because this part is in side sheet
                        Toast.makeText(mContext, "Error in getting posted job list", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(mContext, "Error in getting posted job list", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setChipData(chipList: List<String>) {
        binding.rSavedChipGroup.visibility = View.VISIBLE
        binding.rSavedChipGroup.removeAllViews()
        for (text in chipList) {
            binding.rSavedChipGroup.addView(
                CommonUiFunctions.createChip(
                    text,
                    R.color.white,
                    mContext
                )
            )
        }
    }

    private fun getAllShortlisted() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recVM.getAllShortlisted(userId.toInt()).let {
                    if (it.isSuccessful && it.body() != null) {
                        shortList = mutableListOf()
                        if (it.body()!!.status == 200) {
                            shortList = it.body()!!.shortList!!
                        }
                        getAllSavedCan()
                        binding.noDataLay.clNodataFound.visibility = View.GONE
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            "Error in fetching employess detail, please check internet"
                        ) {
                        }
                        binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Error in fetching employess detail, please check internet"
                ) {
                }
                binding.noDataLay.clNodataFound.visibility = View.VISIBLE
            }

        }
    }

    private fun getAllSavedCan(jobId: Int? = null) {
        startShimmer(binding.rSavedShimmer, binding.jobSavedRecyView)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recManageViewModel.getSavedCandidates(userId.toInt(), jobId).let {
                    stopShimmer(binding.rSavedShimmer, binding.jobSavedRecyView)
                    val responseBody = it.body()
                    if (it.isSuccessful && responseBody != null) {
                        if (responseBody.status == 200) {
                            val list = responseBody.data
                            empListAdapter =
                                EmpListAdapter(list, RecFragInfoEnum.RecSaved, mContext, shortList) { empId, type, pos->
                                    when (type) {
                                        1 -> callShortListApi(empId)
                                        2 -> reqResume(empId, list, pos)
                                        else -> {}
                                    }
                                }
                            setEmpListAdapter()
                            binding.noDataLay.clNodataFound.visibility = View.GONE
                        } else {
                            binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            "Error in fetching employess detail, please check internet"
                        ) {
                        }
                        binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                stopShimmer(binding.rSavedShimmer, binding.jobSavedRecyView)
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Technical Error in fetching employess detail, please check internet"
                ) {
                }
                binding.noDataLay.clNodataFound.visibility = View.VISIBLE
            }
        }
    }

    private fun reqResume(empId: Int, empList: List<RecruiterEmpData>, pos: Int) {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                chatViewModel.sendMess(
                    empId = empId,
                    mess = ChatTypes.recReqRes,
                    recId = Constant.userId.toInt(),
                    senderType = "recruiter"
                ).let {
                    empList[pos].showProg = false
                    empListAdapter.notifyItemChanged(pos)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == "200") {
                            Toast.makeText(
                                mContext,
                                "Resume requested",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext, "Some technical error in requesting resume"
                        ) {

                        }
                    }
                }
            }
        } catch (e: Exception) {
            CommonUiFunctions.showErrorMsgDialog(
                mContext,
                "Some error in requesting resume, Check your internet"
            ) {

            }
        }

    }


    private fun callShortListApi(empId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            recManageViewModel.callShortListApi(empId, userId.toInt()).let {
                Log.d(LOG_TAG, "Shortlist: ${it.body()}")
            }
        }
    }

    private fun setEmpListAdapter() {
        binding.jobSavedRecyView.apply {
            adapter = empListAdapter
        }
    }

    private fun setSideSheetAdapter() {
        sideSheetBinding.postSheetRecview.apply {
            adapter = postJobListAdapter
        }
    }

}