package com.bitspan.bitsjobkaro.ui.subFragment.recEditProfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.bitspan.bitsjobkaro.CommonUiFunctions.isValidEmail
import com.bitspan.bitsjobkaro.CommonUiFunctions.isValidNum
import com.bitspan.bitsjobkaro.databinding.FragmentEditCompBasicBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterNewViewModel

class EditCompBasicFragment : Fragment() {

    private lateinit var binding: FragmentEditCompBasicBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val postJobViewModel: RecruiterNewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditCompBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rEditNextbtn.setOnClickListener {
            if (checkField()) {
                setData()
//                navigateToCompInfo()
            }
        }
    }

    private fun setData() {
        postJobViewModel.postAboutCompanyData.name = binding.rEditComNameEdTxt.editText?.text.toString().trim()
        postJobViewModel.postAboutCompanyData.email = binding.rEmailEdTxt.editText?.text.toString().trim()
        postJobViewModel.postAboutCompanyData.number = binding.rEditConNumEdTxt.editText?.text.toString().trim()
    }

    fun checkField(): Boolean {
        val check = if (binding.rEditComNameEdTxt.editText?.text?.isBlank() == true) {
            resetErrorAndFields("Comp name can't be empty")
            false
        } else if (binding.rEmailEdTxt.editText?.text?.isBlank() == true && !isValidEmail(binding.rEmailEdTxt.editText!!.text.toString())) {
            resetErrorAndFields(email = "enter your email correctly")
            false
        } else if (!isValidNum(binding.rEditConNumEdTxt.editText?.text.toString())) {
            resetErrorAndFields(comNum = "please enter your company contact number correctly")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(
        name: String? = null,
        email: String? = null,
        comNum: String? = null
    ) {
        binding.rEditComNameEdTxt.error = name
        binding.rEmailEdTxt.error = email
        binding.rEditConNumEdTxt.error = comNum
    }

//    private fun navigateToCompInfo() {
//        val direction =
//            EditCompBasicFragmentDirections.actionEditCompBasicFragmentToEditCompanyInfoFragment()
//        findNavController().navigate(direction)
//    }

}