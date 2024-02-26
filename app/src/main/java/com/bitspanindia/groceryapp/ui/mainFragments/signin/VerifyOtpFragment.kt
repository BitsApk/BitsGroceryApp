package com.bitspanindia.groceryapp.ui.mainFragments.signin

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentVerifyOtpBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.LoginViewModel
import `in`.aabhasjindal.otptextview.OTPListener
import kotlinx.coroutines.launch


class VerifyOtpFragment : Fragment() {
    private lateinit var binding:FragmentVerifyOtpBinding
    private lateinit var mContext: Context
    private val loginVM: LoginViewModel by activityViewModels()
    private val otpArgs: VerifyOtpFragmentArgs by navArgs()
    private lateinit var countDownTimer: CountDownTimer

    private val countdownTime: Long = 60000 // 60 seconds

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

        if (!fromLogin) {
            binding.apply {
                textView3.visibility = View.GONE
                numCodeTxt.visibility = View.GONE
                signUpMobileEdTxt.visibility = View.GONE
                otpView.visibility = View.VISIBLE
            }
        }


        binding.resendVerificationCode.isEnabled = false

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
        countDownTimer.start()
        binding.resendVerificationCode.setOnClickListener {
            binding.progBar.visibility = View.VISIBLE
            binding.btnContinue.isEnabled = false
            if (otpArgs.fromLogin) {
                sendLoginOtp()
            } else {
//                sendRegisterOtp()
            }
            restartCountdownTimer()
            binding.resendVerificationCode.isEnabled = false
        }

        binding.otpView.otpListener = object : OTPListener{
            override fun onInteractionListener() {
            }

            override fun onOTPComplete(otp: String) {
                val action = VerifyOtpFragmentDirections.actionVerifyOtpFragmentToHomeFragment()
                findNavController().navigate(action)
            }

        }

    }

    private fun sendLoginOtp() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {

            } catch (e: Exception) {

            }
        }
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