package com.bitspanindia.groceryapp.ui.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentCartBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CartBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCartBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


}