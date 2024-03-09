package com.bitspan.bitsjobkaro.ui.subFragment.recEditProfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.databinding.FragmentEditRecDetailBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterNewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditRecDetailFragment : Fragment() {

    private lateinit var binding: FragmentEditRecDetailBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val recNewVM: RecruiterNewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditRecDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUiFunctions.apply {
            changeStatusBarColor(mActivity, R.color.edit_profile)
            bottomNavBarVisibility(mActivity, View.GONE)
        }


        setFilledData()


        binding.rEditNextbtn.setOnClickListener {
            if (checkField()) {
                setData()
                updateApi()

            }
        }
    }

    private fun setFilledData() {
        binding.rEditRecNameEdTxt.editText?.setText(recNewVM.postAboutCompanyData.rName)
        binding.rEditYourDesignationEdTxt.editText?.setText(recNewVM.postAboutCompanyData.rDesig)
        binding.rEditYourNumEdTxt.editText?.setText(recNewVM.postAboutCompanyData.rMobile)
    }

    private fun setData() {
        recNewVM.postAboutCompanyData.rName = binding.rEditRecNameEdTxt.editText?.text.toString().trim()
        recNewVM.postAboutCompanyData.rDesig = binding.rEditYourDesignationEdTxt.editText?.text.toString().trim()
        recNewVM.postAboutCompanyData.rMobile = binding.rEditYourNumEdTxt.editText?.text.toString().trim()
    }

    private fun updateApi() {
        recNewVM.postAboutCompanyData.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    recNewVM.postAboutCompanyData(
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
                        Toast.makeText(mContext, "Your Detail Updated", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                } catch (e: Exception) {
                    CommonUiFunctions.showErrorMsgDialog(mContext, "Some technical error in updating your detail, please try again later") {
                        findNavController().popBackStack()
                    }
                }

            }
        }
    }

    fun checkField(): Boolean {
        val check = if (binding.rEditRecNameEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields("Recruiter name can't be empty")
            false
        } else if (binding.rEditYourDesignationEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(designation = "Type your designation")
            false
        } else if (!CommonUiFunctions.isValidNum(binding.rEditYourNumEdTxt.editText?.text.toString())) {
            resetErrorAndFields(recNum = "please enter your contact number")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(name: String? = null, designation: String? = null, recNum: String? = null) {
        binding.rEditRecNameEdTxt.error = name
        binding.rEditYourDesignationEdTxt.error = designation
        binding.rEditYourNumEdTxt.error = recNum
    }


}