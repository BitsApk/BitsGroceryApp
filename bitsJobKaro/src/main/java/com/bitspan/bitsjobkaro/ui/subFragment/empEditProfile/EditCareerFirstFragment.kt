package com.bitspan.bitsjobkaro.ui.subFragment.empEditProfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonDataFunctions.checkJobTimeType
import com.bitspan.bitsjobkaro.CommonDataFunctions.checkJobType
import com.bitspan.bitsjobkaro.CommonDataFunctions.getFormattedEng
import com.bitspan.bitsjobkaro.CommonDataFunctions.getFormattedShifts
import com.bitspan.bitsjobkaro.CommonDataFunctions.postEmpEng
import com.bitspan.bitsjobkaro.CommonDataFunctions.postEmpJobTimeType
import com.bitspan.bitsjobkaro.CommonDataFunctions.postEmpShifts
import com.bitspan.bitsjobkaro.CommonDataFunctions.postEmployeeEmpType
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.databinding.FragmentEditCareerFirstBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpCarPrefViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditCareerFirstFragment : Fragment() {

    private lateinit var binding: FragmentEditCareerFirstBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val empCarViewModel: EmpCarPrefViewModel by activityViewModels()
    private val carFirstArgs: EditCareerFirstFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditCareerFirstBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fromProfile = carFirstArgs.fromProfile
        CommonUiFunctions.apply {
            changeStatusBarColor(mActivity, R.color.edit_profile)
            bottomNavBarVisibility(mActivity, View.GONE)
        }

        setDropDownLists()
        if (fromProfile) setUiFields()

        binding.rEditNextbtn.setOnClickListener {
            if (checkFields()) {
                updateCareerPrefContainer()
                val direction = EditCareerFirstFragmentDirections.actionEditCareerFirstFragmentToEditCareerSecFragment(fromProfile)
                findNavController().navigate(direction)
            }
        }


    }

    private fun updateCareerPrefContainer() {
        empCarViewModel.careerPrefContainer.apply {
            jobRole = binding.carPreJobEdTxt.editText?.text.toString()
            expLevel = binding.carExpLevelEdTxt.editText?.text.toString()
            expYr = binding.carYearEdTxt.editText?.text.toString()
            expMonth = binding.carMonthEdTxt.editText?.text.toString()
            eType = postEmployeeEmpType(binding.carPreEmpTypeEdTxt.editText?.text.toString())
            empType = postEmpJobTimeType(binding.carPreJobTypeEdTxt.editText?.text.toString())
            prefShift = postEmpShifts(binding.carPreJobShiftEdTxt.editText?.text.toString())
            engLevel = postEmpEng(binding.carPrefEngEdTxt.editText?.text.toString())
        }
    }

    private fun setUiFields() {
        binding.apply {
            carPreJobEdTxt.editText?.setText(empCarViewModel.careerPrefContainer.jobRole)
            (carExpLevelEdTxt.editText as AutoCompleteTextView).setText(empCarViewModel.careerPrefContainer.expLevel,false)
            (carYearEdTxt.editText as AutoCompleteTextView).setText(empCarViewModel.careerPrefContainer.expYr,false)
            (carMonthEdTxt.editText as AutoCompleteTextView).setText(empCarViewModel.careerPrefContainer.expMonth,false)
            (carPreEmpTypeEdTxt.editText as AutoCompleteTextView).setText(checkJobType(empCarViewModel.careerPrefContainer.eType),false)
            (carPreJobTypeEdTxt.editText as AutoCompleteTextView).setText(checkJobTimeType(empCarViewModel.careerPrefContainer.empType),false)
            (carPreJobShiftEdTxt.editText as AutoCompleteTextView).setText(getFormattedShifts(empCarViewModel.careerPrefContainer.prefShift),false)
            (carPrefEngEdTxt.editText as AutoCompleteTextView).setText(getFormattedEng(empCarViewModel.careerPrefContainer.engLevel),false)
        }
    }

    private fun checkFields(): Boolean {
        val jobRole = binding.carPreJobEdTxt.editText?.text.toString()
        val exp = binding.carExpLevelEdTxt.editText?.text.toString()
        val year = binding.carYearEdTxt.editText?.text.toString()
        val month = binding.carMonthEdTxt.editText?.text.toString()
        val empType = binding.carPreEmpTypeEdTxt.editText?.text.toString()
        val jobType = binding.carPreJobTypeEdTxt.editText?.text.toString()
        val jobShift = binding.carPreJobShiftEdTxt.editText?.text.toString()
        val engLevel = binding.carPrefEngEdTxt.editText?.text.toString()

        val check = if (jobRole.isBlank()) {
            resetErrorAndFields(jobRole = "Role can't be empty")
            false
        } else if (exp.isBlank()) {
            resetErrorAndFields(exp = "Choose your experience level")
            false
        } else if (year.isBlank()) {
            resetErrorAndFields(year = "Select your experience year")
            false
        } else if (month.isBlank()) {
            resetErrorAndFields(month = "Select extra's months")
            false
        } else if (empType.isBlank()) {
            resetErrorAndFields(empType = "Select your preference")
            false
        } else if (jobType.isBlank()) {
            resetErrorAndFields(jobType = "Select your time type for job")
            false
        } else if (jobShift.isBlank()) {
            resetErrorAndFields(jobShift = "Choose preferable job shift")
            false
        } else if (engLevel.isBlank()) {
            resetErrorAndFields(engLevel = "Select your english level")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(
        jobRole: String? = null,
        exp: String? = null,
        year: String? = null,
        month: String? = null,
        empType: String? = null,
        jobType: String? = null,
        jobShift: String? = null,
        engLevel: String? = null
    ) {
        binding.carPreJobEdTxt.error = jobRole
        binding.carExpLevelEdTxt.error = exp
        binding.carYearEdTxt.error = year
        binding.carMonthEdTxt.error = month
        binding.carPreEmpTypeEdTxt.error = empType
        binding.carPreJobTypeEdTxt.error = jobType
        binding.carPreJobShiftEdTxt.error = jobShift
        binding.carPrefEngEdTxt.error = engLevel
    }

    private fun setDropDownLists() {
        val expReqAdapter =
            ArrayAdapter.createFromResource(
                mContext,
                R.array.empExpReqList,
                R.layout.item_drop_down
            )
        (binding.carExpLevelEdTxt.editText as? AutoCompleteTextView)?.setAdapter(expReqAdapter)

        val yearExpList = mutableListOf<Int>()
        for (i in 0..10) yearExpList.add(i)
        val yearExpAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, yearExpList)
        (binding.carYearEdTxt.editText as? AutoCompleteTextView)?.setAdapter(yearExpAdapter)

        val monthList = mutableListOf<Int>()
        for (i in 0..11) monthList.add(i)
        val maxSalAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, monthList)
        (binding.carMonthEdTxt.editText as? AutoCompleteTextView)?.setAdapter(maxSalAdapter)

        val empTypeAdapter =
            ArrayAdapter.createFromResource(mContext, R.array.emplTypeList, R.layout.item_drop_down)
        (binding.carPreEmpTypeEdTxt.editText as? AutoCompleteTextView)?.setAdapter(empTypeAdapter)

        val jobTypeAdapter =
            ArrayAdapter.createFromResource(mContext, R.array.jobTypeList, R.layout.item_drop_down)
        (binding.carPreJobTypeEdTxt.editText as? AutoCompleteTextView)?.setAdapter(jobTypeAdapter)

        val jobShiftAdapter = ArrayAdapter.createFromResource(
            mContext,
            R.array.empDayShiftList,
            R.layout.item_drop_down
        )
        (binding.carPreJobShiftEdTxt.editText as? AutoCompleteTextView)?.setAdapter(jobShiftAdapter)

        val engAdapter =
            ArrayAdapter.createFromResource(
                mContext,
                R.array.empEngLevList,
                R.layout.item_drop_down
            )
        (binding.carPrefEngEdTxt.editText as? AutoCompleteTextView)?.setAdapter(engAdapter)
    }

}