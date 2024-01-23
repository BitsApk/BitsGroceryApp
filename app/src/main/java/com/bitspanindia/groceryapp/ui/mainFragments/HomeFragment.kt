package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.databinding.FragmentHomeBinding
import com.bitspanindia.groceryapp.databinding.LocationEnableBottomSheetBinding
import com.bitspanindia.groceryapp.model.SliderModel
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
        setProducts()
        binding.profImage.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(action)
        }

//        binding.tvProductDetails.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment()
//            findNavController().navigate(action)
//        }

    }
    private fun setProducts() {

        val data = listOf(
            SliderModel(
                name = "Uncle Chips",
                quantity = "2 Pieces",
                offPrice = "₹0",
                price = "₹40",
                image= R.drawable.uncle_chips
            ),
            SliderModel(
                name = "Bingo Tedhe Medhe",
                quantity = "90 g",
                offPrice = "₹60",
                price = "₹42",
                image= R.drawable.tedhe_medhe
            ),
            SliderModel(
                name = "Kurkure",
                quantity = "82 g",
                offPrice = "₹60",
                price = "₹54",
                image= R.drawable.kukure
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.lays1
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.kaccha_mango_bite
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.lays_american
            ),
            SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.kaccha_mango_bite1
            ),
            SliderModel(
                name = "Lay's  sdsd sds sd American",
                quantity = "50 g",
                offPrice = "₹0",
                price = "₹20",
                image= R.drawable.lays1
            ),
        ) // Replace this with your data
        binding.selectedProdRecView.layoutManager = GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false)
        binding.selectedProdRecView.adapter= ProductsAdapter(data)
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