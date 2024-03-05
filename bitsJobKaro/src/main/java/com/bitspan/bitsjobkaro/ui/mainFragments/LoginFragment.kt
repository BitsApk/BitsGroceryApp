package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.data.models.SendOtpReq
import com.bitspan.bitsjobkaro.databinding.FragmentLoginBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val otherVM: OtherViewModel by viewModels()
    private val loginArgs: LoginFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireActivity()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity,View.GONE)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (loginArgs.fromForgotPass) {
            binding.textView2.visibility = View.INVISIBLE
            binding.loginSubHeadTxt.visibility = View.INVISIBLE
        }

        //signUp button click---------------------
        binding.loginLogBtn.setOnClickListener {
            binding.loginLogBtn.isEnabled = false
            binding.progBar.visibility = View.VISIBLE
            if (checkValid()) {
                if (loginArgs.fromForgotPass) sendForgotOtp() else sendOtp()
            } else {
                binding.progBar.visibility = View.GONE
                binding.loginLogBtn.isEnabled = true
            }
        }
        //signUp button click---------------------
        binding.loginBackImg.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun sendForgotOtp() {
        val sendOtpReq = SendOtpReq(
            isLogin = null,
            number = binding.loginMobileEdTxt.editText?.text.toString().toLong(),
            server = otherVM.server,
            name = null
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.sendForgotOtp(sendOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.loginLogBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            navigateToOtp(it.body()!!.number)
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to send otp at this moment") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to send otp at this moment") {
                            findNavController().popBackStack()
                        }

                    }
                }
            } catch (e: Exception) {

                binding.progBar.visibility = View.GONE
                binding.loginLogBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(mContext, "Their is some problem in sending otp at this moment") {
                    findNavController().popBackStack()
                }
            }

        }
    }

    private fun sendOtp() {
        val sendOtpReq = SendOtpReq(
            isLogin = otherVM.yes,
            number = binding.loginMobileEdTxt.editText?.text.toString().toLong(),
            server = otherVM.server,
            name = null
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.sendLoginOtp(sendOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.loginLogBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            navigateToOtp(it.body()!!.number)
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to send otp at this moment") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to send otp at this moment") {
                            findNavController().popBackStack()
                        }

                    }
                }
            } catch (e: Exception) {

                binding.progBar.visibility = View.GONE
                binding.loginLogBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(mContext, "Their is some problem in sending otp at this moment") {
                    findNavController().popBackStack()
                }
            }

        }
    }

    private fun checkValid(): Boolean {
        if (CommonUiFunctions.isValidNum(binding.loginMobileEdTxt.editText?.text.toString())) {
            binding.loginMobileEdTxt.error = null
            return true
        }
        binding.loginMobileEdTxt.error = "Please enter valid mobile num"
        return false
    }

    private fun navigateToOtp(number: String?) {
        val direction = LoginFragmentDirections.actionLoginFragmentToOtpFragment(mobileNum = number ?: "", fromForgot = loginArgs.fromForgotPass)
        findNavController().navigate(direction)
    }
}