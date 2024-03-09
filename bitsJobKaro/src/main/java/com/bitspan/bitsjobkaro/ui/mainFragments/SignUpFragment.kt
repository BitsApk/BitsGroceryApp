package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.data.models.RegisterBody
import com.bitspan.bitsjobkaro.data.models.SendOtpReq
import com.bitspan.bitsjobkaro.databinding.FragmentSignUpBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.LoginViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val otherVM: OtherViewModel by viewModels()
    private val registerViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpComNameEdTxt.hint = if (userType) getString(R.string.candidate_name) else getString(R.string.company_name)

        binding.signUpLogBtn.setOnClickListener {
            registerViewModel.registerReq = getUserRequest()
            binding.progBar.visibility = View.VISIBLE
            binding.backBtn.isEnabled = false
            if (checkField()) {
                sendOtp()
            } else {
                binding.progBar.visibility = View.GONE
                binding.backBtn.isEnabled = true
            }
        }


        binding.signUpForPassTxt.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun sendOtp() {
        val sendOtpReq = SendOtpReq (
            name = registerViewModel.registerReq.name,
            number = registerViewModel.registerReq.mobile.toLong(),
            server = otherVM.server,
            isLogin = null
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {

                otherVM.sendRegisterOtp(sendOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.signUpLogBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            navigateToOtp(registerViewModel.registerReq.mobile)
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to send otp at this moment") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Error in sending otp at this moment") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.signUpLogBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to send otp at this moment") {
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun getUserRequest(): RegisterBody {
        return binding.run {
            RegisterBody(
                signUpComNameEdTxt.editText?.text.toString().trim(),
                signUpEmailEdTxt.editText?.text.toString().trim(),
                signUpMobileEdTxt.editText?.text.toString().trim(),
                signUpPassEdTxt.editText?.text.toString().trim()
            )
        }
    }

    private fun checkField(): Boolean {
        val check = if (registerViewModel.registerReq.name.isBlank()) {
            resetErrorAndFields("Name can't be empty")
            false
        } else if (registerViewModel.registerReq.email.isBlank() || !CommonUiFunctions.isValidEmail(registerViewModel.registerReq.email)) {
            resetErrorAndFields(email = "Please type correct email")
            false
        } else if (registerViewModel.registerReq.mobile.isBlank()) {
            resetErrorAndFields(num = "Mobile can't empty")
            false
        } else if (!CommonUiFunctions.isValidNum(registerViewModel.registerReq.mobile)) {
            resetErrorAndFields(num = "Number should be of 10 numeric characters")
            false
        } else if (registerViewModel.registerReq.password.isBlank() || registerViewModel.registerReq.password.length <= 5) {
            resetErrorAndFields(pass = "Password should contain atleast 6 character")
            false
        } else if (registerViewModel.registerReq.password != binding.signUpConfPassEdTxt.editText?.text.toString()) {
            resetErrorAndFields(confPass = "Confirm Password should be equal to password")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(
        compName: String? = null,
        email: String? = null,
        num: String? = null,
        pass: String? = null,
        confPass: String? = null
    ) {
        binding.signUpComNameEdTxt.editText?.error = compName
        binding.signUpEmailEdTxt.editText?.error = email
        binding.signUpMobileEdTxt.editText?.error = num
        binding.signUpPassEdTxt.error = pass
        binding.signUpConfPassEdTxt.error = confPass
    }

    private fun navigateToOtp(number: String) {
        val direction = SignUpFragmentDirections.actionSignUpFragmentToOtpFragment(fromRegister = true,
            mobileNum = number
        )
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registerViewModel.userRegisterResLiveData.removeObservers(viewLifecycleOwner)
        registerViewModel.recRegisterResponseLiveData.removeObservers(viewLifecycleOwner)
    }

}