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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.getRealPathFromURI
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList
import com.bitspan.bitsjobkaro.databinding.FragmentEditCompDescBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterNewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class EditCompDescFragment: Fragment() {

    private lateinit var binding: FragmentEditCompDescBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val editArgs: EditCompDescFragmentArgs by navArgs()
    private val postJobViewModel: RecruiterNewViewModel by activityViewModels()
    private var docPath: String? = null
    private var logoPath: String? = null
    private var docName: String? = null
    private var logoName: String? = null

    private val logoContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            logoPath = getRealPathFromURI(it, mContext)
            logoName = File(logoPath!!).name
            binding.uploadedImgTxt.text = logoName
        }
    }

    private val pdfContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        Log.d(LOG_TAG, "Pdf Uri: ${it.toString()} Pdf path: ${it?.path}")
        if (it != null) {
            docPath = getRealPathFromURI(it, mContext)
            docName = File(docPath!!).name
            binding.uploadedPdfTxt.text = docName
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditCompDescBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editComDescTopTxt.visibility = if (editArgs.fromProfile) {
            CommonUiFunctions.setMargin(mContext, binding.editLayout, 0, 60, 0, 0)
            View.GONE
        } else {
            binding.rEditIndTypeField.visibility = View.VISIBLE
            binding.rIndTypeEdTxt.visibility = View.VISIBLE
            val indAdapter =
                ArrayAdapter(mContext, R.layout.item_drop_down, PreDefinedList.industryType)
            (binding.rIndTypeEdTxt.editText as? AutoCompleteTextView)?.setAdapter(indAdapter)
            View.VISIBLE
        }


        val docAdapter =
            ArrayAdapter(mContext, R.layout.item_drop_down, PreDefinedList.docType)
        (binding.rEditDocTypeEdTxt.editText as? AutoCompleteTextView)?.setAdapter(docAdapter)

        if (editArgs.fromProfile) {
            docName = postJobViewModel.postAboutCompanyData.documents11
            logoName = postJobViewModel.postAboutCompanyData.cLogo11
            if (logoName != null) binding.uploadedImgTxt.text = logoName
            if (docName != null) binding.uploadedPdfTxt.text = docName
            binding.rEditComDescEdTxt.editText?.setText(postJobViewModel.postAboutCompanyData.cDesc)
            (binding.rEditDocTypeEdTxt.editText as AutoCompleteTextView).setText(CommonDataFunctions.getDocType(postJobViewModel.postAboutCompanyData.docType), false)
        }


        CommonUiFunctions.apply {
            changeStatusBarColor(mActivity, R.color.edit_profile)
            bottomNavBarVisibility(mActivity, View.GONE)
        }

        binding.logoBtn.setOnClickListener {
            logoContract.launch("image/*")
        }

        binding.docsBtn.setOnClickListener {
            pdfContract.launch("application/pdf")  // application/pdf
        }

        binding.rEditPostbtn.setOnClickListener {
            if (checkField()) {   // checkField()
                postJobViewModel.postAboutCompanyData.cDesc = binding.rEditComDescEdTxt.editText?.text.toString()
                if (!editArgs.fromProfile) {
                    postJobViewModel.postAboutCompanyData.comType = CommonDataFunctions.postIndusType(binding.rIndTypeEdTxt.editText?.text.toString().trim())
                }

                updateApi()
            }
        }

    }


    private fun updateApi() {
        postJobViewModel.postAboutCompanyData.recId = userId
        val docMultiPart = if (docPath != null) CommonUiFunctions.multiPartDoc(docPath!!, "documents") else null
        val logoMultiPart = if (logoPath != null) CommonUiFunctions.multiPartImage(logoPath!!, "c_logo") else null
        val logoName = if (postJobViewModel.postAboutCompanyData.cLogo11 != null)  CommonUiFunctions.multiPartText(postJobViewModel.postAboutCompanyData.cLogo11!!) else null
        val docName = if (postJobViewModel.postAboutCompanyData.documents11 != null)  CommonUiFunctions.multiPartText(postJobViewModel.postAboutCompanyData.documents11!!) else null
        postJobViewModel.postAboutCompanyData.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    postJobViewModel.postAboutCompanyData(
                        documents = docMultiPart,
                        c_logo = logoMultiPart,
                        c_logo11 = logoName,
                        documents11 = docName,
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
                        docType = CommonUiFunctions.multiPartText(CommonDataFunctions.postDocType(binding.rEditDocTypeEdTxt.editText?.text.toString()))
                    ).let {
                        if (it.isSuccessful && it.body() != null && it.body()!!.status == 200) {
                            if (editArgs.fromProfile) {
                                Toast.makeText(mContext, "Company description Updated", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                            else {
                                navigateToCompleted()
                            }
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, "Some error in updating company description, please try again later") {
                                findNavController().popBackStack()
                            }
                        }

                    }
                } catch (e: Exception) {
                    CommonUiFunctions.showErrorMsgDialog(mContext, "Some technical error in updating company description, please try again later") {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun navigateToCompleted() {
        findNavController().popBackStack(R.id.editRecDetailFragment, true)
        val direction = EditCompDescFragmentDirections.actionGlobalCompletedLottieFragment(R.id.postJobFragment)
        findNavController().navigate(direction)
    }

    fun checkField(): Boolean {
        val check = if (!editArgs.fromProfile && binding.rIndTypeEdTxt.editText?.text?.isBlank() == true) {
            binding.rIndTypeEdTxt.error = "Please select industry type"
            binding.rEditComDescEdTxt.error = null
            binding.rEditDocTypeEdTxt.error = null
            false
        } else if (binding.rEditComDescEdTxt.editText?.text?.isBlank() == true) {
            binding.rEditComDescEdTxt.error = "Comp description is important and can't be empty"
            binding.rEditDocTypeEdTxt.error = null
            false
        } else if (binding.rEditDocTypeEdTxt.editText?.text?.isBlank() == true) {
            binding.rEditDocTypeEdTxt.error = "Choose doc type"
            binding.rEditComDescEdTxt.error = null
            false
        } else if (logoName == null) {
            Toast.makeText(mContext, "Please select company logo", Toast.LENGTH_SHORT).show()
            false
        } else if (docName == null) {
            Toast.makeText(mContext, "Please upload company document for verification", Toast.LENGTH_SHORT).show()
            false
        } else true
        return check
    }


}