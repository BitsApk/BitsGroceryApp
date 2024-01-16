package com.bitspanindia.groceryapp.ui.mainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentVerifyOtpBinding
import `in`.aabhasjindal.otptextview.OTPListener


class VerifyOtpFragment : Fragment() {
    private lateinit var binding:FragmentVerifyOtpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.otpView.otpListener = object : OTPListener{
            override fun onInteractionListener() {
            }

            override fun onOTPComplete(otp: String) {
                val action = VerifyOtpFragmentDirections.actionVerifyOtpFragmentToHomeFragment()
                findNavController().navigate(action)
            }

        }

    }

}