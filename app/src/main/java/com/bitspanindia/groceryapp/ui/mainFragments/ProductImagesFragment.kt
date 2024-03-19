package com.bitspanindia.groceryapp.ui.mainFragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.presentation.adapter.ProductSmallImageAdapter
import com.bitspanindia.groceryapp.presentation.adapter.slider.SliderAdapter
import com.bitspanindia.groceryapp.databinding.FragmentProductImagesBinding
import com.bitspanindia.groceryapp.data.model.SliderModel
import com.bitspanindia.groceryapp.presentation.viewmodel.ProductViewModel

class ProductImagesFragment : Fragment() {
    private lateinit var binding:FragmentProductImagesBinding
    val args: ProductImagesFragmentArgs by navArgs()
    private lateinit var adapter: ProductSmallImageAdapter
    private val pvm: ProductViewModel by activityViewModels()
    private var imagePos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentProductImagesBinding.inflate(inflater, container, false)

        AppUtils.cartLayoutVisibility(requireActivity(),View.GONE)

        imagePos = args.position

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewpagerSlider()

       adapter = ProductSmallImageAdapter(pvm.prodImageList,requireContext()){pos ->
            imagePos = pos
            if (pos>=0){
                binding.viewPager.currentItem =imagePos
            }
        }
        binding.rvProductImage.adapter=adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                adapter.selectedItemPos= position
                adapter.notifyDataSetChanged()
            }
        })

        binding.clClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setViewpagerSlider() {
        val adapter = SliderAdapter(requireContext(),pvm.prodImageList){ pos -> }
        binding.viewPager.adapter = adapter
        Handler(Looper.getMainLooper()).postDelayed({
            binding.viewPager.setCurrentItem(imagePos, true)
        }, 80)
    }

}