package com.bitspanindia.groceryapp.ui.mainFragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {
    private lateinit var binding:FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentEditProfileBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.white)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green_500)
    }

}