package com.bitspan.bitsjobkaro.ui.subFragment.postJob

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.databinding.FragmentPostJobSecBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterPostJobViewModel

class PostJobSecFragment : Fragment() {

    private lateinit var binding: FragmentPostJobSecBinding
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
        binding = FragmentPostJobSecBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDropDownLists()
        Log.d(LOG_TAG, viewModel.postJobBody.toString())
    }

    private fun setDropDownLists() {
        val expReqAdapter =
            ArrayAdapter.createFromResource(mContext, R.array.expReqList, R.layout.item_drop_down)
        (binding.postExpReqTxt.editText as? AutoCompleteTextView)?.setAdapter(expReqAdapter)

        val qualAdapter =
            ArrayAdapter.createFromResource(mContext, R.array.qualReqList, R.layout.item_drop_down)
        (binding.postQualReqEdTxt.editText as? AutoCompleteTextView)?.setAdapter(qualAdapter)

        val engAdapter =
            ArrayAdapter.createFromResource(mContext, R.array.engLevList, R.layout.item_drop_down)
        (binding.postCommSkillEdTxt.editText as? AutoCompleteTextView)?.setAdapter(engAdapter)

        val minSalList = mutableListOf<Int>()
        for (i in 0..20) minSalList.add(i)
        val minSalAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, minSalList)
        (binding.postMinSalEdTxt.editText as? AutoCompleteTextView)?.setAdapter(minSalAdapter)

        val maxSalList = mutableListOf<Int>()
        for (i in 1..25) maxSalList.add(i)
        val maxSalAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, maxSalList)
        (binding.postMaxSalEdTxt.editText as? AutoCompleteTextView)?.setAdapter(maxSalAdapter)

        val workFromAdapter =
            ArrayAdapter.createFromResource(mContext, R.array.daysList, R.layout.item_drop_down)
        (binding.postWorkFromEdTxt.editText as? AutoCompleteTextView)?.setAdapter(workFromAdapter)

        val workToAdapter =
            ArrayAdapter.createFromResource(mContext, R.array.daysList, R.layout.item_drop_down)
        (binding.postWorkToEdTxt.editText as? AutoCompleteTextView)?.setAdapter(workToAdapter)
    }

    fun checkField(): Boolean {
        val check = if (binding.postExpReqTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields("Choose experience")
            false
        } else if (binding.postQualReqEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(qualReq = "Choose qualification")
            false
        } else if (binding.postCommSkillEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(commSkill = "Choose eng level")
            false
        } else if (binding.postMinSalEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(minSal = "Choose min salary")
            false
        } else if (binding.postMaxSalEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(maxSal = "Choose max salary")
            false
        } else if ((binding.postMinSalEdTxt.editText?.text?.toString()
                ?.toInt() ?: 0) >= (binding.postMaxSalEdTxt.editText?.text?.toString()?.toInt()
                ?: 1)
        ) {
            resetErrorAndFields(maxSal = "Max salary should greater than min salary")
            false
        } else if (binding.postWorkFromEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(workFrom = "Choose start day")
            false
        } else if (binding.postWorkToEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(workTo = "Choose end day")
            false
        } else true
        setFieldToPostJob()
        return check
    }

    private fun setFieldToPostJob() {
        viewModel.postJobBody.experience = CommonDataFunctions.postRecExperience(binding.postExpReqTxt.editText?.text.toString())
        viewModel.postJobBody.quali = CommonDataFunctions.postRecQualif(binding.postQualReqEdTxt.editText?.text.toString())
        viewModel.postJobBody.cEng = CommonDataFunctions.postRecEng(binding.postCommSkillEdTxt.editText?.text.toString())
        viewModel.postJobBody.sMin = binding.postMinSalEdTxt.editText?.text.toString()
        viewModel.postJobBody.sMax = binding.postMaxSalEdTxt.editText?.text.toString()
        viewModel.postJobBody.dayFrom = binding.postWorkFromEdTxt.editText?.text.toString()
        viewModel.postJobBody.dayTo = binding.postWorkToEdTxt.editText?.text.toString()
    }

    private fun resetErrorAndFields(
        expReq: String? = null,
        qualReq: String? = null,
        commSkill: String? = null,
        minSal: String? = null,
        maxSal: String? = null,
        workFrom: String? = null,
        workTo: String? = null
    ) {
        binding.postExpReqTxt.error = expReq
        binding.postQualReqEdTxt.error = qualReq
        binding.postCommSkillEdTxt.error = commSkill
        binding.postMinSalEdTxt.error = minSal
        binding.postMaxSalEdTxt.error = maxSal
        binding.postWorkFromEdTxt.error = workFrom
        binding.postWorkToEdTxt.error = workTo
    }


}