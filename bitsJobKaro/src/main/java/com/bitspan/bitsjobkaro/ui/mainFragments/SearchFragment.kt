package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.gson.GsonBuilder
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList
import com.bitspan.bitsjobkaro.data.models.AllJobData
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.employee.EmpSavedBookmarkData
import com.bitspan.bitsjobkaro.data.models.employee.EmpSearchJobReq
import com.bitspan.bitsjobkaro.data.models.employee.SaveUnsaveJobReq
import com.bitspan.bitsjobkaro.databinding.FragmentSearchBinding
import com.bitspan.bitsjobkaro.databinding.SideSheetEmpSearchBinding
import com.bitspan.bitsjobkaro.presentation.adapters.AllJobAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.AllJobViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {


    private lateinit var mActivity: FragmentActivity
    private lateinit var mContext: Context
    private val allJobViewModel: AllJobViewModel by viewModels()
    private val otherVM: OtherViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var sideSheet: SideSheetDialog
    private lateinit var sideSheetBinding: SideSheetEmpSearchBinding
    private lateinit var empSearchJobReq: EmpSearchJobReq
    private lateinit var jobList: List<AllJobData>
    private lateinit var adpater: AllJobAdapter
    private var savedBookMarkList: MutableList<EmpSavedBookmarkData> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireContext()
        mActivity = requireActivity()
        sideSheet = SideSheetDialog(mContext)
        sideSheetBinding = SideSheetEmpSearchBinding.inflate(LayoutInflater.from(mContext))
        sideSheet.setContentView(sideSheetBinding.root)
        binding =  FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        CommonUiFunctions.handleKeyBoardState(mContext, binding.searchEdTxt)

        empSearchJobReq = EmpSearchJobReq(
            empId = userId.toInt()
        )

        getAllSavedJobs()


        // Setting radio group
        setRadioData(PreDefinedList.salaryList, sideSheetBinding.filterSalRadioG)
        setRadioData(PreDefinedList.jobEmpTypeList, sideSheetBinding.filterJobTypeRadioG)
        setRadioData(PreDefinedList.prefShift, sideSheetBinding.filterShiftRadioG)
        setRadioData(PreDefinedList.empTimeType, sideSheetBinding.filterEmpTypeRadioG)


        binding.searchFilter.setOnClickListener {
            sideSheet.show()
        }


        sideSheetBinding.filterSalField.setOnClickListener {
            setSalaryRadio()
        }

        sideSheetBinding.filterJobTypeField.setOnClickListener {
            setJobTypeRadio()
        }

        sideSheetBinding.filterShiftField.setOnClickListener {
            setPrefShiftRadio()
        }

        sideSheetBinding.filterEmpTypeField.setOnClickListener {
            setEmpRadio()
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchBtn.setOnClickListener {
            CommonUiFunctions.hideKeyBoard(mContext, binding.searchEdTxt)
            binding.progBar.visibility = View.VISIBLE
            binding.searchBtn.isEnabled = false
            empSearchJobReq.multipleSea = if (binding.searchEdTxt.text.toString().isBlank()) "" else binding.searchEdTxt.text.toString().trim()
            getJobSearch()
        }




        sideSheetBinding.filterSalRadioG.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            empSearchJobReq.salary = CommonDataFunctions.postJobSalary(radioButton.text.toString())
        }

        sideSheetBinding.filterJobTypeRadioG.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            empSearchJobReq.jobType = CommonDataFunctions.postJobTimeType(radioButton.text.toString())
        }

        sideSheetBinding.filterShiftRadioG.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            empSearchJobReq.shift = CommonDataFunctions.postRecShifts(radioButton.text.toString())
        }


        sideSheetBinding.filterEmpTypeRadioG.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            empSearchJobReq.empType = CommonDataFunctions.postEmpEmpType(radioButton.text.toString())
        }


    }

    private fun getAllSavedJobs() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.getEmpSavedBookmarkList(RecEmpIdRequest(empId = userId.toInt())).let {
                    if (it.isSuccessful && it.body() != null){
                        savedBookMarkList = mutableListOf()
                        if (it.body()!!.status == 200) {
                            savedBookMarkList = it.body()!!.data ?: mutableListOf()
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
                binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Some technical error in fetching employess detail, please check internet"
                ) {

                }
            }
        }
    }

    private fun getJobSearch() {
        val gson = GsonBuilder().create()
        val req = gson.toJson(empSearchJobReq)
        Log.d("Rishabh", "job search $req")
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.empSearchJob(empSearchJobReq).let { it ->
                    binding.progBar.visibility = View.GONE
                    binding.searchBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            binding.noDataLay.clNodataFound.visibility = View.GONE
                            jobList = it.body()!!.list ?: listOf()
                            adpater = AllJobAdapter(jobList, savedBookMarkList) {jobId ->
                                callSaveBook(jobId)
                            }
                            binding.searchRecyclerView.layoutManager = LinearLayoutManager(mContext)
                            binding.searchRecyclerView.adapter = adpater
                            binding.noDataLay.clNodataFound.visibility = View.GONE
                        } else {
                            binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            "Some error in searching with filters, Check your internet"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.searchBtn.isEnabled = true
                binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Some error in searching with filters, Check your internet"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }




    private fun callSaveBook(jobId: Int) {
        val saveUnsaveJobReq = SaveUnsaveJobReq(
            empId = userId.toInt(),
            jobId = jobId.toString(),
            server = otherVM.server
        )
        viewLifecycleOwner.lifecycleScope.launch {
            otherVM.saveUnsaveJob(saveUnsaveJobReq).let {
            }
        }
    }



    private fun setRadioData(radioList: List<String>, radioGroup: RadioGroup) {
        for (text in radioList) {
            val rad = layoutInflater.inflate(R.layout.item_radio_button_rec_search, radioGroup, false) as RadioButton
            rad.text = text
            radioGroup.addView(rad)
        }
        val first: RadioButton = radioGroup.getChildAt(0) as RadioButton
        first.isChecked = true
    }



    private fun setJobTypeRadio() {
        sideSheetBinding.apply {
            filterJobTypeRadioG.visibility = if (filterJobTypeRadioG.isVisible) {
                filterJobTypeField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_up_arrow,0)
                View.GONE
            } else {
                filterJobTypeField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_down_arrow,0)
                View.VISIBLE
            }
        }
    }


    private fun setPrefShiftRadio() {
        sideSheetBinding.apply {
            filterShiftRadioG.visibility = if (filterShiftRadioG.isVisible) {
                filterShiftField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_up_arrow,0)
                View.GONE
            } else {
                filterShiftField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_down_arrow,0)
                View.VISIBLE
            }
        }
    }

    private fun setSalaryRadio() {
        sideSheetBinding.apply {
            filterSalRadioG.visibility = if (filterSalRadioG.isVisible) {
                filterSalField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_up_arrow,0)
                View.GONE
            } else {
                filterSalField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_down_arrow,0)
                View.VISIBLE
            }
        }
    }


    private fun setEmpRadio() {
        sideSheetBinding.apply {
            filterEmpTypeRadioG.visibility = if (filterEmpTypeRadioG.isVisible) {
                filterEmpTypeField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_up_arrow,0)
                View.GONE
            } else {
                filterEmpTypeField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_down_arrow,0)
                View.VISIBLE
            }
        }
    }


}