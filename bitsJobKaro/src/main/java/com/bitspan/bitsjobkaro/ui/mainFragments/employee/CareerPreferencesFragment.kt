package com.bitspan.bitsjobkaro.ui.mainFragments.employee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.databinding.FragmentCareerPreferencesBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpCarPrefViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CareerPreferencesFragment : Fragment() {

    private lateinit var mActivity: FragmentActivity
    private lateinit var mContext: Context
    private lateinit var binding: FragmentCareerPreferencesBinding
    private val empCarViewModel: EmpCarPrefViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        // Inflate the layout for this fragment
        binding =  FragmentCareerPreferencesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCareerData()
        binding.profileBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.editCarPref.setOnClickListener{
            val direction = CareerPreferencesFragmentDirections.actionCareerPreferencesFragmentToEditCareerFirstFragment(fromProfile = true)
            findNavController().navigate(direction)
        }

    }


    private fun getCareerData() {
        viewLifecycleOwner.lifecycleScope.launch{
            try {
                empCarViewModel.getEmpCarPrefData(userId.toInt()).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.status == 200) {
                            val body = it.body()?.data?.get(0)
                            empCarViewModel.careerPrefContainer.apply {
                                formId = body?.formId ?: ""
                                jobRole = body?.empJobRoles ?: "NA"
                                cities = body?.empPrefCities ?: "NA"
                                empId = body?.empCreatedBy ?: ""
                                expLevel = body?.empTypeExp ?: "NA"
                                expYr = body?.empExpYear ?: "NA"
                                expMonth = body?.empExpMonth ?: "NA"
                                engLevel = body?.empEnglishLabel ?: "NA"
                                stateId = body?.empPrefState ?: ""
                                stateName = body?.empState ?: "NA"
                                minSalary = body?.empSalaryLakh ?: "NA"
                                maxSalary = body?.empSalaryThousand ?: "NA"
                                prefShift = body?.empPrefShift ?: "NA"
                                empType = body?.empPrefEmpType ?: "NA"
                                eType = body?.empTypeOH ?: "NA"
                                objective = body?.empObjective ?: "NA"
                                dataPresent = true
                                resume = body?.empResume ?: "NA"
                                skills = body?.empSkills?.split(",") ?: listOf()
                            }
                            setUiFields()
                        } else {

                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching career detail, please check internet connection or contact help desk") {
                        }
                    }
                }
            } catch (e: Exception) {
                CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching career detail, please check internet connection or contact help desk") {
                    findNavController().popBackStack()
                }
            }
        }

    }



    private fun setUiFields() {
        empCarViewModel.careerPrefContainer.apply {
            binding.carPrefObj.text = objective
            binding.carPrefJob.text = jobRole
            binding.carPreExpLabel.text = expLevel
            binding.carTotalExp.text = CommonDataFunctions.getFormattedExpYr(expYr, expMonth)
            binding.carPrefEngLabel.text = CommonDataFunctions.getFormattedEng(engLevel)
            binding.carPrefState.text = stateName
            binding.carPrefCity.text = cities
            binding.carPrefExpSalary.text = CommonDataFunctions.getFormattedSalary(minSalary, maxSalary)
            binding.carPrefShift.text = CommonDataFunctions.getFormattedShifts(prefShift)
            binding.carPrefJobType.text = CommonDataFunctions.checkJobTimeType(empType)
            binding.carPrefEmpType.text = CommonDataFunctions.checkJobType(eType)
            binding.carPrefSkills.text = skills?.joinToString(", ") ?: "NA"
        }
    }

}