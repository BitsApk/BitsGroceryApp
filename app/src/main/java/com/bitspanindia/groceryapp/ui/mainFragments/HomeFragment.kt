package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentHomeBinding
import com.bitspanindia.groceryapp.databinding.LocationEnableBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : Fragment() {
private lateinit var binding:FragmentHomeBinding
private lateinit var mContext:Context
private lateinit var mActivity:FragmentActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding =  FragmentHomeBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        showLocationDialog()

        binding.tvProfile.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(action)
        }

    }

    private fun showLocationDialog() {
        val dialog = BottomSheetDialog(mContext)
//        val view = layoutInflater.inflate(R.layout.location_enable_bottom_sheet, null)
        val bindingDialog = LocationEnableBottomSheetBinding.inflate(layoutInflater)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)

        bindingDialog.clSearchLocation.setOnClickListener {
            dialog.dismiss()
            val action = HomeFragmentDirections.actionHomeFragmentToChooseLocationFragment()
            findNavController().navigate(action)
        }

        dialog.show()
    }

}