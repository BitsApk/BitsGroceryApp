package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.data.constants.NetworkResult
import com.bitspan.bitsjobkaro.data.models.LoginBody
import com.bitspan.bitsjobkaro.databinding.FragmentSignInBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpCarPrefViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.LoginViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val viewModel: CommonViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val recViewModel: RecruiterViewModel by viewModels()
    private val empProffViewModel: EmpCarPrefViewModel by viewModels()
    private val rManageVM: RecruiterManageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInRegisterTxt.setOnClickListener {
            navigateToSignUp()
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signInForPassTxt.setOnClickListener {
            navigateToForgotPass()
        }

        binding.signInLogBtn.setOnClickListener {
            val userRequest: LoginBody = getUserRequest()
            if (checkField(userRequest.email, userRequest.password)) {
                if (userType) {
                    loginViewModel.empLoginUser(userRequest)
                } else {
                    loginViewModel.doRecruitLogin(userRequest)
                }
            }
        }

        if (userType) {
            bindEmployeeObservers()
        } else bindRecruiterObservers()

        binding.signInMobileTxt.setOnClickListener { navigateToMobileLogin() }

    }

    private fun navigateToForgotPass() {
        val direction = SignInFragmentDirections.actionSignInFragmentToLoginFragment(fromForgotPass = true)
        findNavController().navigate(direction)
    }

    private fun bindRecruiterObservers() {
        loginViewModel.recLoginResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val userId = it.data?.data?.get(0)?.id ?: ""
                    viewModel.saveUserId(userId)
                    Constant.userId = userId
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            recViewModel.getRecProfileData(userId.toInt()).let { profileRes ->
                                if (profileRes.body()!!.status == 200) {
                                    rManageVM.getPostedJobList(userId.toInt()).let {postJob ->
                                        if (postJob.body()!!.status == 200) {
                                            if ((postJob.body()!!.data?.size ?: 0) > 0) {
                                                navigateToRecHomePage()
                                            }
                                        } else {
                                            val direction = SignInFragmentDirections.actionSignInFragmentToPostJobFragment(false)
                                            findNavController().navigate(direction)
                                        }
                                    }
                                } else {
                                    findNavController().popBackStack(R.id.userSelectFragment, true)
                                    findNavController().navigate(R.id.editCompLocationFragment)
                                }
                                binding.signInProgBar.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching recruiter profile details, please try again later") {
                                mActivity.finish()
                            }
                        }

                    }
                }
                is NetworkResult.Error -> {
                    binding.signInProgBar.visibility = View.GONE
                    showValidationErrors(it.message.toString())
                }
                is NetworkResult.Loading -> {
                    binding.signInProgBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun bindEmployeeObservers() {
        loginViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val userId = it.data?.data?.get(0)?.id ?: ""
                    viewModel.saveUserId(userId)
                    Constant.userId = userId
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            empProffViewModel.getEmpCarPrefData(userId.toInt()).let { carPref ->
                                binding.signInProgBar.visibility = View.GONE
                                if (carPref.body()!!.status == 200) {
                                    navigateToHomePage()
                                } else {
                                    findNavController().popBackStack(R.id.userSelectFragment, true)
                                    findNavController().navigate(R.id.editCareerFirstFragment)
                                }
                            }
                        } catch (e: Exception) {
                            CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching employee career details, please try again later") {
                                mActivity.finish()
                            }
                        }

                    }
                }

                is NetworkResult.Error -> {
                    binding.signInProgBar.visibility = View.GONE
                    showValidationErrors(it.message.toString())
                }
                is NetworkResult.Loading -> {
                    binding.signInProgBar.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun checkField(email: String, pass: String): Boolean {
        val check = if (email.isBlank() || !CommonUiFunctions.isValidEmail(email)) {
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
        binding.signInEmailEdTxt.editText?.error = email
        binding.signInPassEdTxt.error = pass
    }

    private fun getUserRequest(): LoginBody {
        return binding.run {
            LoginBody(
                signInEmailEdTxt.editText?.text.toString(),
                signInPassEdTxt.editText?.text.toString()
            )
        }
    }

    private fun showValidationErrors(error: String) {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMobileLogin() {
        val direction = SignInFragmentDirections.actionSignInFragmentToLoginFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToRecHomePage() {
        val direction = SignInFragmentDirections.actionSignInFragmentToRecruiterHomeFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToHomePage() {
        val direction = SignInFragmentDirections.actionSignInFragmentToHomeFragmentSeeker()
        findNavController().navigate(direction)
    }

    private fun navigateToSignUp() {
        val direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginViewModel.userResponseLiveData.removeObservers(viewLifecycleOwner)
        loginViewModel.recLoginResponseLiveData.removeObservers(viewLifecycleOwner)
    }
}