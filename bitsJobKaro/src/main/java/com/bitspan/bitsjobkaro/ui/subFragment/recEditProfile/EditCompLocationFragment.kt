package com.bitspan.bitsjobkaro.ui.subFragment.recEditProfile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.models.DistrictData
import com.bitspan.bitsjobkaro.databinding.FragmentEditCompLocationBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterNewViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditCompLocationFragment : Fragment() {

    private lateinit var binding: FragmentEditCompLocationBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val recViewModel: RecruiterViewModel by viewModels()
    private val editArgs: EditCompLocationFragmentArgs by navArgs()
    private val postJobViewModel: RecruiterNewViewModel by activityViewModels()
    private var stateError: Boolean = false
    private var disError: Boolean = false
    private lateinit var stateIdList: List<String>
    private lateinit var districtIdList: List<String>
    private lateinit var districtList: List<DistrictData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditCompLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStateData()
        getDistrictData()

        CommonUiFunctions.apply {
            changeStatusBarColor(mActivity, R.color.edit_profile)
            bottomNavBarVisibility(mActivity, View.GONE)
        }

        binding.rEditLocbtn.text = if (editArgs.fromProfile){
            setFilterData()
            getString(R.string.update)
        }
        else getString(R.string.next)

        binding.rEditLocbtn.setOnClickListener {
            if (checkField()) {   // checkField()
                setData()
                if (editArgs.fromProfile) {
                    updateApi()
                } else {
                    navigateToCompDes()
                }
            }
        }

        (binding.rEditStateEdTxt.editText as? AutoCompleteTextView)?.setOnItemClickListener { p0, p1, p2, p3 ->
            getDistrictListWithId(stateIdList[p2])
            binding.rEditDistEdTxt.editText?.setText("")
            postJobViewModel.postAboutCompanyData.state = stateIdList[p2]
        }

        (binding.rEditDistEdTxt.editText as? AutoCompleteTextView)?.setOnItemClickListener { p0, p1, p2, p3 ->
            postJobViewModel.postAboutCompanyData.district = districtIdList[p2]
        }

    }

    private fun setFilterData() {
        binding.rEditAddressEdTxt.editText?.setText(postJobViewModel.postAboutCompanyData.adress)
        binding.rEditCityEdTxt.editText?.setText(postJobViewModel.postAboutCompanyData.city)
        binding.rEditZipPostalEdTxt.editText?.setText(postJobViewModel.postAboutCompanyData.zip)

    }

    private fun getDistrictListWithId(stateId: String) {
        val distList = recViewModel.getDistrictWithId(stateId, districtList)
        setDistrictAdapter(distList.first)
        districtIdList = distList.second
    }

    private fun getDistrictData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val list = recViewModel.getDistrict()
            if (list.isEmpty()) {
                disError = true
            } else {
                districtList = list
            }
        }
    }


    private fun setDistrictAdapter(list: List<String>) {
        val districtAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, list)
        (binding.rEditDistEdTxt.editText as? AutoCompleteTextView)?.setAdapter(districtAdapter)
    }


    private fun getStateData() {
        val list = recViewModel.getStatesPair()
        if (list.first.isEmpty()) {
            stateError = true
        } else {
            stateIdList = list.second
            setStateAdapter(list.first)
            Log.d(LOG_TAG, "Error in fetching state list")
        }
        if (editArgs.fromProfile) {
            (binding.rEditStateEdTxt.editText as? AutoCompleteTextView)?.setText(list.first[(postJobViewModel.postAboutCompanyData.state?.toInt() ?: 1) - 1], false)
        }


    }

    private fun setStateAdapter(list: List<String>) {
        val stateAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, list)
        (binding.rEditStateEdTxt.editText as? AutoCompleteTextView)?.setAdapter(stateAdapter)
    }


    private fun setData() {
        postJobViewModel.postAboutCompanyData.adress = binding.rEditAddressEdTxt.editText?.text.toString().trim()
        postJobViewModel.postAboutCompanyData.city = binding.rEditCityEdTxt.editText?.text.toString().trim()
        postJobViewModel.postAboutCompanyData.zip = binding.rEditZipPostalEdTxt.editText?.text.toString().trim()

    }

    private fun updateApi() {
        postJobViewModel.postAboutCompanyData.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    postJobViewModel.postAboutCompanyData(
                        documents = null,
                        c_logo = null,
                        c_logo11 = CommonUiFunctions.multiPartText(cLogo11 ?: ""),
                        documents11 = CommonUiFunctions.multiPartText(documents11 ?: ""),
                        name = CommonUiFunctions.multiPartText(name ?: ""),
                        cType = CommonUiFunctions.multiPartText(comType ?: ""),
                        cNumber = CommonUiFunctions.multiPartText(number ?: ""),
                        cEmail = CommonUiFunctions.multiPartText(email ?: ""),
                        rName = CommonUiFunctions.multiPartText(rName ?: ""),
                        rMobile = CommonUiFunctions.multiPartText(rMobile ?: ""),
                        rDesign = CommonUiFunctions.multiPartText(rDesig ?: ""),
                        cDesc = CommonUiFunctions.multiPartText(cDesc ?: ""),
                        cGst = CommonUiFunctions.multiPartText(cGst ?: ""),
                        nEmp = CommonUiFunctions.multiPartText(nEmp ?: ""),
                        adress = CommonUiFunctions.multiPartText(adress ?: ""),
                        link = CommonUiFunctions.multiPartText(link ?: ""),
                        city = CommonUiFunctions.multiPartText(city ?: ""),
                        state = CommonUiFunctions.multiPartText(state ?: ""),
                        zip = CommonUiFunctions.multiPartText(zip ?: ""),
                        district = CommonUiFunctions.multiPartText(district ?: ""),
                        createdBy = CommonUiFunctions.multiPartText(createdBy ?: ""),
                        formId = CommonUiFunctions.multiPartText(formId ?: ""),
                        recId = CommonUiFunctions.multiPartText(recId ?: ""),
                        docType = CommonUiFunctions.multiPartText(docType ?: "")
                    ).let {
                        Toast.makeText(mContext, "Company Address Updated", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                } catch (e: Exception) {
                    CommonUiFunctions.showErrorMsgDialog(mContext, "Some technical error in updating company address, please try again later") {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun navigateToCompDes() {
        val direction =
            EditCompLocationFragmentDirections.actionEditCompLocationFragmentToEditCompDescFragment(fromProfile = editArgs.fromProfile)
        findNavController().navigate(direction)
    }


    fun checkField(): Boolean {
        val check = if (binding.rEditAddressEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields("Address can't be empty")
            false
        } else if (binding.rEditDistEdTxt.editText?.text?.isBlank() == true) {
            if (disError) true else {
                resetErrorAndFields(district = "please select company district")
                false
            }
        } else if (binding.rEditCityEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(city = "please enter company city")
            false
        } else if (binding.rEditZipPostalEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(zip = "please provide area zip code")
            false
        } else if (binding.rEditStateEdTxt.editText?.text?.isBlank() == true) {
            if (stateError) true else {
                resetErrorAndFields(state = "Choose your state")
                false
            }
        } else true
        return check
    }


    private fun resetErrorAndFields(
        address: String? = null,
        district: String? = null,
        city: String? = null,
        zip: String? = null,
        state: String? = null
    ) {
        binding.rEditAddressEdTxt.error = address
        binding.rEditDistEdTxt.error = district
        binding.rEditCityEdTxt.error = city
        binding.rEditZipPostalEdTxt.error = zip
        binding.rEditStateEdTxt.error = state
    }


}