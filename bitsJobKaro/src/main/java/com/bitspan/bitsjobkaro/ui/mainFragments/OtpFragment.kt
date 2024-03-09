package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.data.constants.NetworkResult
import com.bitspan.bitsjobkaro.data.models.SendOtpReq
import com.bitspan.bitsjobkaro.data.models.VerifyOtpReq
import com.bitspan.bitsjobkaro.databinding.FragmentOtpBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpCarPrefViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.LoginViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpFragment : Fragment() {


    private lateinit var binding: FragmentOtpBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var countDownTimer: CountDownTimer


    private val otpArgs: OtpFragmentArgs by navArgs()
    private val commonViewModel: CommonViewModel by viewModels()
    private val otherVM: OtherViewModel by viewModels()
    private val loginVM: LoginViewModel by activityViewModels()
    private val empProffViewModel: EmpCarPrefViewModel by viewModels()
    private val recViewModel: RecruiterViewModel by viewModels()
    private val rManageVM: RecruiterManageViewModel by viewModels()



    private val countdownTime: Long = 60000 // 60 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)

        binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendingOTPOn.text = getString(R.string._91_s, otpArgs.mobileNum)

        binding.resendVerificationCode.isEnabled = false

        countDownTimer = object : CountDownTimer(countdownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerText(millisUntilFinished)
            }

            override fun onFinish() {
                binding.timerTextView.text = "00:00"
                binding.resendVerificationCode.isEnabled = true
                binding.resendVerificationCode.setTextColor(ContextCompat.getColor(mContext, R.color.blue_500))
                // Enable your resend button or trigger resend logic here
            }
        }
        countDownTimer.start()
        binding.resendVerificationCode.setOnClickListener {
            binding.progBar.visibility = View.VISIBLE
            binding.btnOTPVerification.isEnabled = false
            if (otpArgs.fromRegister) {
                sendRegisterOtp()
            } else if (otpArgs.fromForgot) {
                sendForgotOtp()
            } else {
                sendLoginOtp()
            }
            restartCountdownTimer()
            binding.resendVerificationCode.isEnabled = false
            binding.resendVerificationCode.setTextColor(ContextCompat.getColor(mContext, R.color.dark_violet_grey))
        }


        binding.btnOTPVerification.setOnClickListener {
            binding.progBar.visibility = View.VISIBLE
            binding.btnOTPVerification.isEnabled = false
            if (checkValid()) {
                val otp = binding.otpView.otp ?: ""
                val verifyOtpReq = VerifyOtpReq(
                    otp = otp.toInt(),
                    number = otpArgs.mobileNum.toLong(),
                    server = otherVM.server
                )
                if (otpArgs.fromRegister) {
                    verifyRegisterOtp(verifyOtpReq)

                } else if (otpArgs.fromForgot) {
                    verifyForgotOtp(verifyOtpReq)
                } else {
                    verifyAndLogin(verifyOtpReq)
                }
            } else {
                binding.btnOTPVerification.isEnabled = true
                binding.progBar.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun sendForgotOtp() {
        val sendOtpReq = SendOtpReq(
            isLogin = null,
            number = otpArgs.mobileNum.toLong(),
            server = otherVM.server,
            name = null
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.sendForgotOtp(sendOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.btnOTPVerification.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Otp Sent", Toast.LENGTH_SHORT).show()
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
                binding.btnOTPVerification.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(mContext, "Their is some problem in sending otp at this moment") {
                    findNavController().popBackStack()
                }
            }

        }
    }


    private fun sendLoginOtp() {
        val sendOtpReq = SendOtpReq(
            isLogin = otherVM.yes,
            number = otpArgs.mobileNum.toLong(),
            server = otherVM.server,
            name = null
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.sendLoginOtp(sendOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.btnOTPVerification.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Otp Sent", Toast.LENGTH_SHORT).show()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to send otp at this moment") {
                                findNavController().popBackStack()
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
                binding.btnOTPVerification.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(mContext, "Their is some problem in sending otp at this moment") {
                    findNavController().popBackStack()
                }
            }

        }
    }

    private fun sendRegisterOtp() {
        val sendOtpReq = SendOtpReq (
            name = loginVM.registerReq.name,
            number = otpArgs.mobileNum.toLong(),
            server = otherVM.server,
            isLogin = null
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.sendRegisterOtp(sendOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.btnOTPVerification.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Otp Sent", Toast.LENGTH_SHORT).show()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to send otp at this moment") {
                                findNavController().popBackStack()

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
                binding.btnOTPVerification.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to send otp at this moment") {
                    findNavController().popBackStack()
                }
            }
        }
    }



    private fun startCountdownTimer() {
        countDownTimer.start()
    }

    private fun updateTimerText(millisUntilFinished: Long) {
        val seconds = millisUntilFinished / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val formattedTime = String.format("%02d:%02d", minutes, remainingSeconds)
        binding.timerTextView.text = formattedTime
    }

    private fun stopCountdownTimer() {
        countDownTimer.cancel()
    }

    private fun restartCountdownTimer() {
        stopCountdownTimer()
        startCountdownTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopCountdownTimer() // Ensure the timer is stopped to prevent memory leaks
    }



    private fun verifyForgotOtp(verifyOtpReq: VerifyOtpReq) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.verifyForgotOtp(verifyOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.btnOTPVerification.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                           navigateToForgotPass(otpArgs.mobileNum)
                        } else {
                            Toast.makeText(
                                mContext,
                                "Entered otp isn't correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            "Error in verifying otp, please try again later"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.btnOTPVerification.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Technical error in verifying otp, please try again later"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun navigateToForgotPass(mobileNum: String) {
        val direction = OtpFragmentDirections.actionOtpFragmentToForgotPassFragment(mobileNum)
        findNavController().navigate(direction)
    }


    private fun verifyRegisterOtp(verifyOtpReq: VerifyOtpReq) {

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.verifyRegisterOtp(verifyOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.btnOTPVerification.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            if (userType) {
                                bindEmpObservers()
                                loginVM.registerUser(loginVM.registerReq)
                            } else {
                                bindRecruitObservers()
                                loginVM.doRecruitRegister(loginVM.registerReq)
                            }
                        } else {
                            Toast.makeText(
                                mContext,
                                "Entered otp isn't correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            "Error in verifying otp, please try again later"
                        ) {

                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.btnOTPVerification.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Technical error in verifying otp, please try again later"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun verifyAndLogin(verifyOtpReq: VerifyOtpReq) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.verifyLoginOtp(verifyOtpReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.btnOTPVerification.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            val userId = it.body()?.data?.get(0)?.id ?: ""
                            commonViewModel.saveUserId(userId)
                            Constant.userId = userId

                            if (userType) {
                                empProffViewModel.getEmpCarPrefData(userId.toInt()).let { carPref ->
                                    binding.progBar.visibility = View.GONE
                                    if (carPref.body()!!.status == 200) {


                                        val direction =
                                            OtpFragmentDirections.actionOtpFragmentToHomeFragmentSeeker()
                                        findNavController().navigate(direction)
                                    } else {
                                        val direction =
                                            OtpFragmentDirections.actionOtpFragmentToEditCareerFirstFragment()
                                        findNavController().navigate(direction)
                                    }
                                }
                            } else {
                                recViewModel.getRecProfileData(userId.toInt()).let { profileRes ->
                                    if (profileRes.body()!!.status == 200) {
                                        rManageVM.getPostedJobList(userId.toInt()).let {postJob ->
                                            if (postJob.body()!!.status == 200) {
                                                if ((postJob.body()!!.data?.size ?: 0) > 0) {
                                                    val direction = OtpFragmentDirections.actionOtpFragmentToRecruiterHomeFragment()
                                                    findNavController().navigate(direction)
                                                }
                                            } else {
                                                val direction = OtpFragmentDirections.actionOtpFragmentToPostJobFragment(false)
                                                findNavController().navigate(direction)
                                            }
                                        }

                                    } else {
                                        val direction =
                                            OtpFragmentDirections.actionOtpFragmentToEditCompLocationFragment(false)
                                        findNavController().navigate(direction)
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(
                                mContext,
                                "Entered otp isn't correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            "Error in verifying otp, please try again later"
                        ) {

                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.btnOTPVerification.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Error in verifying otp, please try again later"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun bindRecruitObservers() {
        loginVM.recRegisterResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val userId = it.data?.id.toString()
                    Constant.userId = userId
                    commonViewModel.saveUserId(userId).let {
                        val direction = OtpFragmentDirections.actionOtpFragmentToEditCompLocationFragment(fromProfile = false)
                        findNavController().navigate(direction)
                    }

                    binding.progBar.visibility = View.GONE
                }

                is NetworkResult.Error -> {
                    binding.progBar.visibility = View.GONE
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    binding.progBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showValidationErrors(error: String) {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
    }

    private fun bindEmpObservers() {
        loginVM.userRegisterResLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val userId = it.data?.id.toString()
                    Constant.userId = userId
                    commonViewModel.saveUserId(userId).let {
                        val direction = OtpFragmentDirections.actionOtpFragmentToEditCareerFirstFragment()
                        findNavController().navigate(direction)
                    }
                    binding.progBar.visibility = View.GONE
                }

                is NetworkResult.Error -> {
                    binding.progBar.visibility = View.GONE
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    binding.progBar.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun checkValid(): Boolean {
        return if (binding.otpView.otp?.length != 5) {
            Toast.makeText(mContext, "Please fill otp", Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}