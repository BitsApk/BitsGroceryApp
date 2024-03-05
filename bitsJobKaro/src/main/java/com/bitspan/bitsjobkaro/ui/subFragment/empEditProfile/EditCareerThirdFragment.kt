package com.bitspan.bitsjobkaro.ui.subFragment.empEditProfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonUiFunctions.getRealPathFromURI
import com.bitspan.bitsjobkaro.CommonUiFunctions.multiPartImage
import com.bitspan.bitsjobkaro.CommonUiFunctions.multiPartText
import com.bitspan.bitsjobkaro.CommonUiFunctions.showErrorMsgDialog
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.databinding.FragmentEditCareerThirdBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpCarPrefViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File

@AndroidEntryPoint
class EditCareerThirdFragment : Fragment() {

    private lateinit var binding: FragmentEditCareerThirdBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val empCarViewModel: EmpCarPrefViewModel by activityViewModels()
    private val empThirdArgs: EditCareerThirdFragmentArgs by navArgs()
    private var pdfPath: String? = null
    private var pdfName: String? = null

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {pdfUri ->
        if (pdfUri != null) {
            pdfPath = getRealPathFromURI(pdfUri, mActivity.applicationContext)
            pdfName = File(pdfPath!!).name
            binding.uploadedNameTxt.text = pdfName
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditCareerThirdBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resumeBtn.setOnClickListener {
            contract.launch("application/pdf")
        }


        if (empThirdArgs.fromProfile) {
            pdfName = empCarViewModel.careerPrefContainer.resume
            binding.uploadedNameTxt.text = pdfName
            binding.carEditObjecEdTxt.editText?.setText(empCarViewModel.careerPrefContainer.objective)
        }

        binding.rEditUpdateBtn.setOnClickListener {
            if (binding.carEditObjecEdTxt.editText?.text.toString().isBlank()) {
                binding.carEditObjecEdTxt.error = "Enter your objective and description"
            } else if (pdfName.isNullOrEmpty()) {
                Toast.makeText(mContext, "You must upload CV/Resume", Toast.LENGTH_SHORT).show()
            } else {
                updateApi()
            }
        }


//        binding.rEditUpdateBtn.setOnClickListener {
//            findNavController().navigate(R.id.a)
//        }
    }

    private fun updateApi() {
        empCarViewModel.careerPrefContainer.objective = binding.carEditObjecEdTxt.editText?.text.toString()
        viewLifecycleOwner.lifecycleScope.launch{
            try {
                empCarViewModel.saveCareerPreference(
                    resume = multiPartImage(pdfPath!!, "resume"),
                    cities = listOf(MultipartBody.Part.createFormData("cities[]", empCarViewModel.careerPrefContainer.cities ?: "")),
                    jobRole = listOf(MultipartBody.Part.createFormData("jobRole[]", empCarViewModel.careerPrefContainer.jobRole ?: "")),
                    skills = listOf(MultipartBody.Part.createFormData("skills[]", empCarViewModel.careerPrefContainer.skills?.joinToString(",") ?: "")),
                    empId = multiPartText(userId),
                    formId = multiPartText(empCarViewModel.careerPrefContainer.formId ?: ""),
                    expLevel = multiPartText(empCarViewModel.careerPrefContainer.expLevel ?: ""),
                    expYr = multiPartText(empCarViewModel.careerPrefContainer.expYr ?: ""),
                    expMonth = multiPartText(empCarViewModel.careerPrefContainer.expMonth ?: ""),
                    engLevel = multiPartText(empCarViewModel.careerPrefContainer.engLevel ?: ""),
                    state = multiPartText(empCarViewModel.careerPrefContainer.stateId ?: ""),
                    minSalary = multiPartText(empCarViewModel.careerPrefContainer.minSalary ?: ""),
                    maxSalary = multiPartText(empCarViewModel.careerPrefContainer.maxSalary ?: ""),
                    prefShift = multiPartText(empCarViewModel.careerPrefContainer.prefShift ?: ""),
                    empType = multiPartText(empCarViewModel.careerPrefContainer.empType ?: ""),
                    eType = multiPartText(empCarViewModel.careerPrefContainer.eType ?: ""),
                    objective = multiPartText(empCarViewModel.careerPrefContainer.objective ?: "")
                ).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            if (empThirdArgs.fromProfile) {
                                Toast.makeText(mContext, "Career preferences Updated", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack(R.id.editCareerFirstFragment, true)
                            } else {
                                Toast.makeText(mContext, "Career preferences added", Toast.LENGTH_SHORT).show()
                                navigateToCompleted()
                            }
                        } else {
                            showErrorMsgDialog(mContext, "Some technical error in uploading, sorry for the incovenience") {
                                findNavController().popBackStack(R.id.editCareerFirstFragment, true)
                            }
                        }
                    } else {
                        showErrorMsgDialog(mContext, "Error in saving career preference, please check your internet or try to contact help desk") {
                            findNavController().popBackStack(R.id.editCareerFirstFragment, true)
                        }
                    }
                }
            } catch (e: Exception) {
                showErrorMsgDialog(mContext, "Error in saving career preference, please check your internet or try to contact help desk") {
                    findNavController().popBackStack(R.id.editCareerFirstFragment, true)
                }
            }

        }

    }


    private fun navigateToCompleted() {
        findNavController().popBackStack(R.id.editCareerFirstFragment, true)
        val direction =
            EditCareerThirdFragmentDirections.actionGlobalCompletedLottieFragment(R.id.homeFragmentSeeker)
        findNavController().navigate(direction)
    }


}