package com.bitspan.bitsjobkaro.ui.mainFragments.recruiter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.showErrorMsgDialog
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.enums.RecFragInfoEnum
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.models.recruiter.ShortListData
import com.bitspan.bitsjobkaro.databinding.FragmentEmpAppliedJobBinding
import com.bitspan.bitsjobkaro.databinding.FragmentRecruiterManageJobBinding
import com.bitspan.bitsjobkaro.presentation.adapters.ChatTypes
import com.bitspan.bitsjobkaro.presentation.adapters.EmpListAdapter
import com.bitspan.bitsjobkaro.presentation.adapters.ManageJobListAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.ChatViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmpAppliedJobFragment : Fragment() {


    private lateinit var binding: FragmentEmpAppliedJobBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var empListAdapter: EmpListAdapter
    private val applyVM: RecruiterManageViewModel by viewModels()
    private val applyRecVM: RecruiterViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val empApplyArgs: EmpAppliedJobFragmentArgs by navArgs()
    private lateinit var shortList: MutableList<ShortListData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mActivity = requireActivity()
        mContext = requireContext()
        binding = FragmentEmpAppliedJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconBackImg.setOnClickListener {
            findNavController().popBackStack()
        }

        val jobDetails = empApplyArgs.jobDetails
        setChipData(jobDetails)

        getAllShortlisted()
    }

    private fun getAllShortlisted() {
        viewLifecycleOwner.lifecycleScope.launch {
            applyRecVM.getAllShortlisted(Constant.userId.toInt()).let {
                if (it.isSuccessful && it.body() != null && it.body()!!.status == 200) {
                    shortList = it.body()!!.shortList!!
                    getAppliedEmpData()
                } else {
                    showErrorMsgDialog(
                        mContext,
                        "Error in fetching employess detail, please check internet"
                    ) {

                    }
                }
            }
        }
    }

    private fun getAppliedEmpData() {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                applyVM.getAppliedEmpData(empApplyArgs.jobId).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            val empList = it.body()!!.data ?: listOf()
                            val empAdapter = EmpListAdapter(empList, RecFragInfoEnum.RecEmpApplied, mContext, shortList) { empId, type, pos ->
                                when (type) {
                                    1 -> callShortListApi(empId)
                                    2 -> reqResume(empId, empList, pos)
                                    else -> {}
                                }
                            }
                            binding.empAppliedRecView.adapter = empAdapter

                        } else {
                            showErrorMsgDialog(mContext, "Data not found") {
                                findNavController().popBackStack()
                            }
                        }
                    } else {
                        showErrorMsgDialog(mContext, "Some technical error in fetching detail") {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            showErrorMsgDialog(mContext, "Error in fetching employees detail, please check your internet") {
                findNavController().popBackStack()
            }
        }
    }

    private fun callShortListApi(empId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            applyVM.callShortListApi(empId, Constant.userId.toInt()).let {
                if (it.body() == null || it.body()!!.status == 204) {
                    Toast.makeText(mContext, "Error in shortlisting employee, employee not shortlisted", Toast.LENGTH_SHORT).show()
                }
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
                        showErrorMsgDialog(
                            mContext, "Some technical error in requesting resume"
                        ) {

                        }
                    }
                }
            }
        } catch (e: Exception) {
            showErrorMsgDialog(
                mContext,
                "Some error in requesting resume, Check your internet"
            ) {

            }
        }

    }



    private fun setChipData(chipList: Array<String>) {
        binding.empAppliedChipGroup.removeAllViews()
        for (text in chipList) {
            binding.empAppliedChipGroup.addView(
                CommonUiFunctions.createChip(
                    text,
                    R.color.white,
                    mContext
                )
            )
        }
    }

}