package com.bitspanindia.groceryapp.ui.mainFragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {
private lateinit var binding:FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        Handler(Looper.getMainLooper()).postDelayed({
            val direction = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
            findNavController().navigate(directions = direction)
        }, 3500)

        return binding.root
    }

}