package com.bitspanindia.groceryapp.ui.mainFragments.signin

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.LoginMobReq
import com.bitspanindia.groceryapp.data.model.request.OtpVerifyReq
import com.bitspanindia.groceryapp.data.model.request.RegisterApiReq
import com.bitspanindia.groceryapp.databinding.FragmentVerifyOtpBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.LoginViewModel
import com.bitspanindia.groceryapp.storage.SharedPreferenceUtil
import `in`.aabhasjindal.otptextview.OTPListener
import kotlinx.coroutines.launch
import javax.inject.Inject


class VerifyOtpFragment : Fragment() {
    private lateinit var binding:FragmentVerifyOtpBinding
    private lateinit var mContext: Context
    private val loginVM: LoginViewModel by activityViewModels()
    private val otpArgs: VerifyOtpFragmentArgs by navArgs()
    private lateinit var countDownTimer: CountDownTimer

    private val countdownTime: Long = 60000 // 60 seconds


    @Inject
    lateinit var pref: SharedPreferenceUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fromLogin = otpArgs.fromLogin

        countDownTimer = object : CountDownTimer(countdownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerText(millisUntilFinished)
            }

            override fun onFinish() {
                binding.timerTextView.text = "00:00"
                binding.resendVerificationCode.isEnabled = true
                // Enable your resend button or trigger resend logic here
            }
        }

        if (!fromLogin) {
            binding.apply {
                textView3.visibility = View.GONE
                numCodeTxt.visibility = View.GONE
                signUpMobileEdTxt.visibility = View.GONE
                btnContinue.visibility = View.GONE
                startCountdownTimer()
                otpVisibility(View.VISIBLE)
            }
        }


        binding.resendVerificationCode.isEnabled = false



        binding.resendVerificationCode.setOnClickListener {
            binding.progBar.visibility = View.VISIBLE
            if (otpArgs.fromLogin) {
                sendMobileOtp()
            } else {
                sendRegisterOtp()
            }
            restartCountdownTimer()
            binding.resendVerificationCode.isEnabled = false
        }

        binding.btnContinue.setOnClickListener {
            binding.progBar.visibility = View.VISIBLE
            binding.btnContinue.visibility = View.GONE
            val mobile = if (fromLogin) binding.signUpMobileEdTxt.editText?.text.toString() else null
            if (checkField(mobile)) {
                if (fromLogin) {
                    sendMobileOtp()
                }
            } else {
                binding.progBar.visibility = View.GONE
                binding.btnContinue.visibility = View.VISIBLE
            }
        }


        binding.otpView.otpListener = object : OTPListener{
            override fun onInteractionListener() {
            }

            override fun onOTPComplete(otp: String) {
                binding.progBar.visibility = View.VISIBLE
                if (fromLogin) {
                    doLoginVerifyOtp(otp)
                } else {
                    doRegistration(otp)
                }

            }

        }

    }

    private fun doLoginVerifyOtp(otp: String) {
        val otpVerifyReq = OtpVerifyReq(
            phone = binding.signUpMobileEdTxt.editText?.text.toString(),
            otp = otp.toInt(),
            fcmToken = loginVM.fcmToken
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                loginVM.doOtpVerify(otpVerifyReq).let {
                    binding.progBar.visibility = View.GONE
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            pref.putString(Constant.USER_ID, it.body()!!.userId ?: "")
                            Constant.userId = it.body()!!.userId ?: ""
                            navigateToHome()
                        } else {
                            Toast.makeText(mContext, it.body()?.message ?: "Unable to login, please try again later", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // TODO error dialog
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                // TODO error dialog
            }
        }
    }

    private fun doRegistration(otp: String?) {
        val registerApiReq = RegisterApiReq(
            addedByWeb = loginVM.registerApiReq.addedByWeb,
            email = loginVM.registerApiReq.email,
            name = loginVM.registerApiReq.name,
            phone = loginVM.registerApiReq.phone,
            password = loginVM.registerApiReq.password,
            confpasswords = loginVM.registerApiReq.confpasswords,
            otp = otp
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                loginVM.doRegistrationOtp(registerApiReq).let {
                    binding.progBar.visibility = View.GONE
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            Toast.makeText(mContext, "SuccessFully Registered", Toast.LENGTH_SHORT).show()
                            pref.putString(Constant.USER_ID, it.body()!!.userId.toString())
                            Constant.userId = it.body()!!.userId.toString()
                            navigateToHome()
                        } else {
                            Toast.makeText(mContext, it.body()?.message ?: "Unable to register user, please try again later", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // TODO error dialog
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                // TODO error dialog
            }
        }
    }

    private fun sendMobileOtp() {
        val loginMobReq = LoginMobReq(phone = binding.signUpMobileEdTxt.editText?.text.toString())
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                loginVM.doMobLogin(loginMobReq).let {
                    binding.progBar.visibility = View.GONE
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            Toast.makeText(mContext, "Otp Sent", Toast.LENGTH_SHORT).show()
                            startCountdownTimer()
                            otpVisibility(View.VISIBLE)
                        } else {
                            Toast.makeText(mContext, it.body()?.message ?: "Unable to send otp, please try again later", Toast.LENGTH_SHORT).show()
                            binding.btnContinue.visibility = View.VISIBLE
                            stopCountdownTimer()
                            otpVisibility(View.GONE)
                        }
                    } else {
                        binding.btnContinue.visibility = View.VISIBLE
                        stopCountdownTimer()
                        otpVisibility(View.GONE)
                        // TODO error dialog
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.btnContinue.visibility = View.VISIBLE
                stopCountdownTimer()
                otpVisibility(View.GONE)
                // TODO error dialog
            }
        }
    }

    private fun otpVisibility(visible: Int) {
        binding.timerTextView.visibility = visible
        binding.resendLay.visibility = visible
        binding.otpView.visibility = visible
    }

    private fun sendRegisterOtp() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                loginVM.doRegistration(loginVM.registerApiReq).let {
                    binding.progBar.visibility = View.GONE
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            Toast.makeText(mContext, "Otp Sent", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(mContext, it.body()?.message ?: "Unable to send otp, please try again later", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // TODO error dialog
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.btnContinue.isEnabled = true
                // TODO error dialog
            }
        }
    }


    private fun checkField(mobile: String?): Boolean {
        val check = if (mobile != null && !AppUtils.isValidNum(mobile)) {
            binding.signUpMobileEdTxt.error = "Please type correct mobile number"
            false
        } else true
        return check
    }


    private fun navigateToHome() {
        val action = VerifyOtpFragmentDirections.actionVerifyOtpFragmentToHomeFragment()
        findNavController().navigate(action)
    }


    private fun stopCountdownTimer() {
        countDownTimer.cancel()
    }

    private fun restartCountdownTimer() {
        stopCountdownTimer()
        startCountdownTimer()
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



    override fun onDestroy() {
        super.onDestroy()
        stopCountdownTimer() // Ensure the timer is stopped to prevent memory leaks
    }




}