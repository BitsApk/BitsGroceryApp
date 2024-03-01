package com.bitspanindia.groceryapp.ui.mainFragments.signin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.RegisterApiReq
import com.bitspanindia.groceryapp.databinding.FragmentLoginBinding
import com.bitspanindia.groceryapp.databinding.FragmentRegisterBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.LoginViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val loginVM: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signUpBtn.setOnClickListener {
            loginVM.registerApiReq = getUserRequest()
//            binding.progBar.visibility = View.VISIBLE
            binding.signUpBtn.isEnabled = false
            if (checkField()) {
                navigateToOtp()
            } else {
//                binding.progBar.visibility = View.GONE
                binding.signUpBtn.isEnabled = true
            }
        }


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun navigateToOtp() {
        val direction = RegisterFragmentDirections.actionRegisterFragmentToVerifyOtpFragment(fromLogin = false)
        findNavController().navigate(direction)
    }

    private fun checkField(): Boolean {
        val check = if (loginVM.registerApiReq.name.isBlank()) {
            resetErrorAndFields("Name can't be empty")
            false
        } else if (loginVM.registerApiReq.email.isBlank() || !AppUtils.isValidEmail(loginVM.registerApiReq.email)) {
            resetErrorAndFields(email = "Please type correct email")
            false
        } else if (loginVM.registerApiReq.phone.isBlank()) {
            resetErrorAndFields(num = "Mobile can't empty")
            false
        } else if (!AppUtils.isValidNum(loginVM.registerApiReq.phone)) {
            resetErrorAndFields(num = "Number should be of 10 numeric characters")
            false
        } else if (loginVM.registerApiReq.password.isBlank() || loginVM.registerApiReq.password.length <= 5) {
            resetErrorAndFields(pass = "Password should contain atleast 6 character")
            false
        } else if (loginVM.registerApiReq.password != binding.signUpConfPassEdTxt.editText?.text.toString()) {
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



    private fun getUserRequest(): RegisterApiReq {
        return binding.run {
            RegisterApiReq(
                addedByWeb = Constant.addedByWeb,
                email = signUpEmailEdTxt.editText?.text.toString().trim(),
                name = signUpComNameEdTxt.editText?.text.toString().trim(),
                phone = signUpMobileEdTxt.editText?.text.toString().trim(),
                password = signUpPassEdTxt.editText?.text.toString().trim(),
                confpasswords = signUpPassEdTxt.editText?.text.toString().trim(),
                fcmToken = ""
            )
        }
    }

}