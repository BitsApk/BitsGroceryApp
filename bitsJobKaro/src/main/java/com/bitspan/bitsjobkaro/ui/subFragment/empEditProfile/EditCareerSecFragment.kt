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
import com.bitspan.bitsjobkaro.CommonUiFunctions.statesList
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.databinding.FragmentEditCareerSecBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpCarPrefViewModel


class EditCareerSecFragment : Fragment() {


    private lateinit var binding: FragmentEditCareerSecBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val empCarViewModel: EmpCarPrefViewModel by activityViewModels()
    private val empSecArgs: EditCareerSecFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditCareerSecBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fromProfile = empSecArgs.fromProfile
        getState()
        setDropDownLists()


        if (fromProfile) {
            setUiFields()
        }

        binding.rEditNextbtn.setOnClickListener {
            if (checkFields()) {
                updateCareerPrefContainer()
                val direction = EditCareerSecFragmentDirections.actionEditCareerSecFragmentToEditCareerThirdFragment(fromProfile)
                findNavController().navigate(direction)
            }
        }

        (binding.carEditStateEdTxt.editText as? AutoCompleteTextView)?.setOnItemClickListener { p0, p1, p2, p3 ->
            (binding.carEditStateEdTxt.editText as? AutoCompleteTextView)?.setText(statesList[p2].stateTitle, false)
            empCarViewModel.careerPrefContainer.stateId = (p2 + 1).toString()
        }



    }

    private fun setUiFields() {
        binding.apply {
            carEditSkillEdTxt.editText?.setText(empCarViewModel.careerPrefContainer.skills?.joinToString(","))
            (carEditStateEdTxt.editText as AutoCompleteTextView).setText(empCarViewModel.careerPrefContainer.stateName,false)
            carEditCityEdTxt.editText?.setText(empCarViewModel.careerPrefContainer.cities)
            (carMinSalEdTxt.editText as AutoCompleteTextView).setText(empCarViewModel.careerPrefContainer.minSalary,false)
            (carMaxSalEdTxt.editText as AutoCompleteTextView).setText(empCarViewModel.careerPrefContainer.maxSalary,false)
        }
    }

    private fun updateCareerPrefContainer() {
        empCarViewModel.careerPrefContainer.apply {
            skills = binding.carEditSkillEdTxt.editText?.text?.split(",")!!
            cities = binding.carEditCityEdTxt.editText?.text.toString()
            minSalary = binding.carMinSalEdTxt.editText?.text.toString()
            maxSalary = binding.carMinSalEdTxt.editText?.text.toString()
        }
    }

    private fun getState() {
        val list = mutableListOf<String>()
        for (item in statesList) {
            if (item.stateTitle != null) list.add(item.stateTitle)
        }
        setStateAdapter(list)
    }

    private fun checkFields(): Boolean {
        val skill = binding.carEditSkillEdTxt.editText?.text.toString()
        val state = binding.carEditStateEdTxt.editText?.text.toString()
        val city = binding.carEditCityEdTxt.editText?.text.toString()
        val minSal = binding.carMinSalEdTxt.editText?.text.toString()
        val maxSal = binding.carMaxSalEdTxt.editText?.text.toString()

        val check = if (skill.isBlank()) {
            resetErrorAndFields(skill = "Enter atleast one skill")
            false
        } else if (state.isBlank()) {
            resetErrorAndFields(state = "Select your prefer state")
            false
        } else if (city.isBlank()) {
            resetErrorAndFields(city = "Select your prefer city")
            false
        } else if (minSal.isBlank()) {
            resetErrorAndFields(minSal = "Minimum salary expected")
            false
        } else if (maxSal.isBlank()) {
            resetErrorAndFields(maxSal = "Maximum salary expected")
            false
        } else if (minSal.toInt() > maxSal.toInt()) {
            resetErrorAndFields(maxSal = "Maximum salary should be greater than min salary")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(
        skill: String? = null,
        state: String? = null,
        city: String? = null,
        minSal: String? = null,
        maxSal: String? = null
    ) {
        binding.carEditSkillEdTxt.error = skill
        binding.carEditStateEdTxt.error = state
        binding.carEditCityEdTxt.error = city
        binding.carMinSalEdTxt.error = minSal
        binding.carMaxSalEdTxt.error = maxSal
    }

    private fun setStateAdapter(list: MutableList<String>) {
        val stateAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, list)
        (binding.carEditStateEdTxt.editText as? AutoCompleteTextView)?.setAdapter(stateAdapter)
    }

    private fun setDropDownLists() {
        val minSalList = mutableListOf<Int>()
        for (i in 0..12) minSalList.add(i)
        val minSalAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, minSalList)
        (binding.carMinSalEdTxt.editText as? AutoCompleteTextView)?.setAdapter(minSalAdapter)

        val maxSalList = mutableListOf<Int>()
        for (i in 1..20) maxSalList.add(i)
        val maxSalAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, maxSalList)
        (binding.carMaxSalEdTxt.editText as? AutoCompleteTextView)?.setAdapter(maxSalAdapter)
    }


}