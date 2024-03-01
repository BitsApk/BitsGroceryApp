package com.bitspanindia.groceryapp.ui.mainFragments.signin

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.LoginBody
import com.bitspanindia.groceryapp.databinding.FragmentLoginBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.LoginViewModel
import com.bitspanindia.groceryapp.storage.SharedPreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private lateinit var mContext: Context
    private val loginViewModel: LoginViewModel by activityViewModels()

    @Inject
    lateinit var pref: SharedPreferenceUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInLogBtn.setOnClickListener {
            binding.signInLogBtn.isEnabled = false
            binding.signInProgBar.visibility = View.VISIBLE
            loginViewModel.loginBody = getUserRequest()

            if (checkField(loginViewModel.loginBody.email, loginViewModel.loginBody.password)) {
                doPassLogin(loginViewModel.loginBody)
            } else {
                binding.signInLogBtn.isEnabled = true
                binding.signInProgBar.visibility = View.GONE
            }
        }

        binding.signInMobileTxt.setOnClickListener {
            val direction = LoginFragmentDirections.actionLoginFragmentToVerifyOtpFragment(fromLogin = true)
            findNavController().navigate(direction)
        }

        binding.signInRegisterTxt.setOnClickListener {
            val direction = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(direction)
        }

        binding.skipLogin.setOnClickListener {
            navigateToHomePage()
        }

    }

    private fun doPassLogin(userRequest: LoginBody) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                loginViewModel.doPassLogin(userRequest).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.statusCode == 200) {
                        pref.putString(Constant.USER_ID, it.body()!!.userId ?: "")
                        pref.putString(Constant.PHONE_NUMBER, it.body()!!.phone ?: "")
                        pref.putString(Constant.USER_NAME, it.body()!!.name ?: "")
                        Constant.userId = it.body()!!.userId ?: ""
                        navigateToHomePage()
                    } else {
                        binding.signInLogBtn.isEnabled = true
                        binding.signInProgBar.visibility = View.GONE
                        Toast.makeText(mContext, it.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }  catch (e: Exception) {
                binding.signInLogBtn.isEnabled = true
                binding.signInProgBar.visibility = View.GONE
                // TODO add error dialog
            }
        }
    }

    private fun navigateToHomePage() {
        val direction = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(direction)
    }

    private fun checkField(email: String, pass: String): Boolean {
        val check = if (email.isBlank() || !AppUtils.isValidEmail(email)) {
            resetErrorAndFields(email = "Please type correct email")
            false
        } else if (pass.isBlank() || pass.length <= 2) {
            resetErrorAndFields(pass = "Password can't be empty")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(
        email: String? = null,
        pass: String? = null
    ) {
        binding.signInEmailEdTxt.error = email
        binding.signInPassEdTxt.error = pass
    }
    private fun getUserRequest(): LoginBody {
        return binding.run {
            LoginBody(
                email = signInEmailEdTxt.editText?.text.toString(),
                password = signInPassEdTxt.editText?.text.toString()
            )
        }
    }
}