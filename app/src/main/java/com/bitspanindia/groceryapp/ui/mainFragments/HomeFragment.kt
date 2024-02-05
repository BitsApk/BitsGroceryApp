package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.BannerImageAdapter
import com.bitspanindia.groceryapp.adapter.HomeRecyclerAdapter
import com.bitspanindia.groceryapp.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.data.DummyData
import com.bitspanindia.groceryapp.data.model.HomeData
import com.bitspanindia.groceryapp.databinding.FragmentHomeBinding
import com.bitspanindia.groceryapp.databinding.LocationEnableBottomSheetBinding
import com.bitspanindia.groceryapp.data.model.SliderModel
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


//        val images = listOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4,  R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_1)
//
//        binding.banner.bannerRecView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
//        binding.banner.bannerRecView.adapter = BannerImageAdapter(images)
//
//        val snapHelper = PagerSnapHelper()
//        snapHelper.attachToRecyclerView(binding.banner.bannerRecView)

        binding.tvCartDetails.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment()
            findNavController().navigate(action)
        }

//        binding.tvCartDetails.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToCartFragment()
//            findNavController().navigate(action)
//        }

    }
    private fun setProducts() {

        binding.homeRecView.adapter = HomeRecyclerAdapter(listOf(
            HomeData("twoRowProduct", "Recommended Products", DummyData.data),
            HomeData("mainCategoryGrid", "Famous Category", DummyData.mainCategory),
            HomeData("oneRowProduct", "How about Snacks", DummyData.data),
            HomeData("bannerRecView", "Banner", DummyData.bannerData),
            HomeData("oneRowProduct", "How about Snacks", DummyData.data),
            HomeData("bannerRecView", "Banner", DummyData.bannerData),
            HomeData("mainCategoryGrid", "Famous Category", DummyData.mainCategory),
        ),
            mContext){
            Toast.makeText(context,"click2", Toast.LENGTH_SHORT).show()
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment()
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