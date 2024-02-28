package com.bitspan.bitsjobkaro.ui.subFragment.recEditProfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList
import com.bitspan.bitsjobkaro.databinding.FragmentEditCompanyInfoBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterNewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditCompanyInfoFragment : Fragment() {

    private lateinit var binding: FragmentEditCompanyInfoBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val postJobViewModel: RecruiterNewViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditCompanyInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFilledData()


        CommonUiFunctions.apply {
            changeStatusBarColor(mActivity, R.color.edit_profile)
            bottomNavBarVisibility(mActivity, View.GONE)
        }

        val indAdapter =
            ArrayAdapter(mContext, R.layout.item_drop_down, PreDefinedList.industryType)
        (binding.rIndTypeEdTxt.editText as? AutoCompleteTextView)?.setAdapter(indAdapter)

        binding.rEditUpdateBtn.setOnClickListener {
            if (checkField()) { // checkField()
                setData()
                updateApi()

            }
        }
    }

    private fun setFilledData() {
        (binding.rIndTypeEdTxt.editText as AutoCompleteTextView).setText(CommonDataFunctions.getIndusType(postJobViewModel.postAboutCompanyData.comType), false)
        binding.rEditComGstEdTxt.editText?.setText(postJobViewModel.postAboutCompanyData.cGst)
        binding.rEditNumEmpEdTxt.editText?.setText(postJobViewModel.postAboutCompanyData.nEmp)
        binding.rEditWebEdTxt.editText?.setText(postJobViewModel.postAboutCompanyData.link)
    }

    private fun setData() {
        postJobViewModel.postAboutCompanyData.comType = CommonDataFunctions.postIndusType(binding.rIndTypeEdTxt.editText?.text.toString().trim())
        postJobViewModel.postAboutCompanyData.cGst =
            binding.rEditComGstEdTxt.editText?.text.toString().trim()
        postJobViewModel.postAboutCompanyData.nEmp =
            binding.rEditNumEmpEdTxt.editText?.text.toString().trim()
        postJobViewModel.postAboutCompanyData.link =
            binding.rEditWebEdTxt.editText?.text.toString().trim()
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
                        Toast.makeText(mContext, "Company Info Updated", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                } catch (e: Exception) {
                    CommonUiFunctions.showErrorMsgDialog(mContext, "Some technical error in updating company info, please try again later") {
                        findNavController().popBackStack()
                    }
                }

            }
        }
    }

    fun checkField(): Boolean {
        val check = if (binding.rIndTypeEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields("Choose industry type")
            false
        } else if (binding.rEditNumEmpEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields(noOfEmp = "Enter total employees")
            false
        }  else if (binding.rEditComGstEdTxt.editText?.text?.isBlank() != true && binding.rEditComGstEdTxt.editText?.text?.length!! < 15) {
            resetErrorAndFields(gstNum = "Please enter correct gst or leave it empty")
            false
        }  else true
        return check
    }


    private fun resetErrorAndFields(induType: String? = null, noOfEmp: String? = null, gstNum: String? = null) {
        binding.rIndTypeEdTxt.error = induType
        binding.rEditNumEmpEdTxt.error = noOfEmp
        binding.rEditComGstEdTxt.error = gstNum
    }

}