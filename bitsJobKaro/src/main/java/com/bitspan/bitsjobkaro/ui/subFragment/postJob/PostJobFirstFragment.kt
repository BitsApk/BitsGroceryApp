package com.bitspan.bitsjobkaro.ui.subFragment.postJob

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.databinding.FragmentPostJobFirstBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterPostJobViewModel

class PostJobFirstFragment : Fragment() {


    private lateinit var binding: FragmentPostJobFirstBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val viewModel: RecruiterPostJobViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        // Inflate the layout for this fragment
        binding = FragmentPostJobFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDropDownLists()
//        viewModel.checkField(binding.postJobTitleEdTxt)

    }

    private fun setDropDownLists() {
        val empTypeAdapter = ArrayAdapter.createFromResource(mContext, R.array.emplTypeList, R.layout.item_drop_down)
        (binding.postEmpTypeEdTxt.editText as? AutoCompleteTextView)?.setAdapter(empTypeAdapter)

        val jobTypeAdapter = ArrayAdapter.createFromResource(mContext, R.array.jobTypeList, R.layout.item_drop_down)
        (binding.postJobTypeEdTxt.editText as? AutoCompleteTextView)?.setAdapter(jobTypeAdapter)

        val jobShiftAdapter = ArrayAdapter.createFromResource(mContext, R.array.dayShiftList, R.layout.item_drop_down)
        (binding.postJobShiftEdTxt.editText as? AutoCompleteTextView)?.setAdapter(jobShiftAdapter)

    }

    fun checkField(): Boolean {
        val check = if (binding.postJobTitleEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields("Job title can't be empty")
            false
        } else if (binding.postNumOpenEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(numOpen = "Enter vacancy count")
            false
        } else if (binding.postSkillEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(skills = "At least one skill")
            false
        } else if (binding.postEmpTypeEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(empType = "Choose first")
            false
        } else if (binding.postJobTypeEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(jobType = "Choose first")
            false
        } else if (binding.postJobShiftEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields()
            binding.postJobShiftEdTxt.error = "Choose shift"
            false
        } else true
        setFieldToPostJob()
        return check
    }

    private fun setFieldToPostJob() {
        viewModel.postJobBody.jTitle = binding.postJobTitleEdTxt.editText?.text.toString()
        viewModel.postJobBody.nOpen = binding.postNumOpenEdTxt.editText?.text.toString()
        viewModel.postJobBody.skills = binding.postSkillEdTxt.editText?.text.toString()
        viewModel.postJobBody.empType = CommonDataFunctions.postRecEmpType(binding.postEmpTypeEdTxt.editText?.text.toString())
        viewModel.postJobBody.jobtype = CommonDataFunctions.postRecJobTimeType(binding.postJobTypeEdTxt.editText?.text.toString())
        viewModel.postJobBody.shift = CommonDataFunctions.postRecShifts(binding.postJobShiftEdTxt.editText?.text.toString())
    }

    private fun resetErrorAndFields(jobTitle: String? = null, numOpen: String? = null, skills: String? = null,
                                    empType: String? = null, jobType: String? = null) {
        binding.postJobTitleEdTxt.error = jobTitle
        binding.postNumOpenEdTxt.error = numOpen
        binding.postSkillEdTxt.error = skills
        binding.postEmpTypeEdTxt.error = empType
        binding.postJobTypeEdTxt.error = jobType
    }

}