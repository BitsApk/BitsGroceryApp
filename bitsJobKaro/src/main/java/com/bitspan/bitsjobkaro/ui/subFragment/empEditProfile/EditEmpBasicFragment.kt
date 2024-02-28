package com.bitspan.bitsjobkaro.ui.subFragment.empEditProfile

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.enums.DateFormatType
import com.bitspan.bitsjobkaro.data.models.employee.EmpBasicDataReq
import com.bitspan.bitsjobkaro.databinding.FragmentEditEmpBasicBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpProffessionalViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import com.bitspan.bitsjobkaro.ui.DialogHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditEmpBasicFragment : Fragment() {

    private lateinit var binding: FragmentEditEmpBasicBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val basicArgs: EditEmpBasicFragmentArgs by navArgs()
    private val empProfViewModel: EmpProffessionalViewModel by activityViewModels()
    private val otherVM: OtherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditEmpBasicBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDropDownLists()

        CommonUiFunctions.apply {
            changeStatusBarColor(mActivity, R.color.edit_profile)
            bottomNavBarVisibility(mActivity, View.GONE)
        }

        binding.eEditDobEdTxt.editText?.setOnClickListener {
            DialogHelper(mContext).showCalendar(DateFormatType.YearMonthDay) { date ->
                binding.eEditDobEdTxt.editText?.setText(date)
            }
        }

        setPreDetails()

        binding.eEditUpdateBtn.setOnClickListener {
            binding.progBar.visibility = View.VISIBLE
            binding.eEditUpdateBtn.isEnabled = false
            val empBasicDataReq = getEmpBasicReq()
            if (checkFields(empBasicDataReq)) {
                updateBasicDetails(empBasicDataReq)
            } else {
                binding.progBar.visibility = View.GONE
                binding.eEditUpdateBtn.isEnabled = true
            }
        }
    }

    private fun setPreDetails() {
        empProfViewModel.basicDetails.let {
            binding.apply {
                eEditNameEdTxt.editText?.setText(it.empName)
                eEmailEdTxt.editText?.setText(it.empEmail)
                eEditNumEdTxt.editText?.setText(it.empContact)
                (eEditGenderEdTxt.editText as AutoCompleteTextView).setText(it.empGender, false)
                eEditDobEdTxt.editText?.setText(it.empDob)
                (eEditQualEdTxt.editText as AutoCompleteTextView).setText(CommonDataFunctions.getFormattedEmpQualif(it.empHigQualification), false)
            }
        }
    }

    private fun updateBasicDetails(empBasicDataReq: EmpBasicDataReq) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.empUpdateBasicData(empBasicDataReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.eEditUpdateBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Basic details updated", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to update basic details now, please try again later") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to update basic details now, please try again later") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.eEditUpdateBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Unable to update basic details now, please try again later"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun getEmpBasicReq(): EmpBasicDataReq {
        return EmpBasicDataReq(
            name = binding.eEditNameEdTxt.editText?.text.toString(),
            email = binding.eEmailEdTxt.editText?.text.toString(),
            contactno = binding.eEditNumEdTxt.editText?.text.toString(),
            gender = binding.eEditGenderEdTxt.editText?.text.toString(),
            dob = binding.eEditDobEdTxt.editText?.text.toString(),
            qualif = CommonDataFunctions.postEmpQualif(binding.eEditQualEdTxt.editText?.text.toString()),
            formid = if (basicArgs.formId != -1) basicArgs.formId else null,
            empId = userId.toInt()
        )
    }

    private fun checkFields(empBasicDataReq: EmpBasicDataReq): Boolean {

        val check = if (empBasicDataReq.name.isBlank()) {
            resetErrorAndFields(name = "Please enter your name")
            false
        } else if (empBasicDataReq.email.isBlank() || !CommonUiFunctions.isValidEmail(empBasicDataReq.email)) {
            resetErrorAndFields(email = "Please type correct email")
            false
        } else if (empBasicDataReq.contactno.isBlank()) {
            resetErrorAndFields(num = "Mobile can't empty")
            false
        } else if (!CommonUiFunctions.isValidNum(empBasicDataReq.contactno)) {
            resetErrorAndFields(num = "Number should be of 10 numeric characters")
            false
        } else if (empBasicDataReq.gender.isBlank()) {
            resetErrorAndFields(gender = "Select your gender")
            false
        } else if (empBasicDataReq.dob.isBlank()) {
            resetErrorAndFields(dob = "Choose your date of birth")
            false
        } else if (empBasicDataReq.qualif.isBlank()) {
            resetErrorAndFields(qual = "Select your highest qualification")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(
        name: String? = null,
        email: String? = null,
        num: String? = null,
        gender: String? = null,
        dob: String? = null,
        qual: String? = null
    ) {
        binding.eEditNameEdTxt.error = name
        binding.eEmailEdTxt.error = email
        binding.eEditNumEdTxt.error = num
        binding.eEditGenderEdTxt.error = gender
        binding.eEditDobEdTxt.error = dob
        binding.eEditQualEdTxt.error = qual
    }

    private fun setDropDownLists() {

        val genderAdapter = ArrayAdapter.createFromResource(mContext, R.array.genderList, R.layout.item_drop_down)
        (binding.eEditGenderEdTxt.editText as? AutoCompleteTextView)?.setAdapter(genderAdapter)

        val qualAdapter = ArrayAdapter.createFromResource(mContext, R.array.empQualReqList, R.layout.item_drop_down)
        (binding.eEditQualEdTxt.editText as? AutoCompleteTextView)?.setAdapter(qualAdapter)

    }



}