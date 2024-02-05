package com.bitspanindia.groceryapp.ui.mainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentOrderSuccessBinding

class OrderSuccessFragment : Fragment() {
    private lateinit var binding:FragmentOrderSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)


        return binding.root
    }

}