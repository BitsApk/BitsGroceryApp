package com.bitspanindia.groceryapp.ui.mainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.ProductSmallImageAdapter
import com.bitspanindia.groceryapp.adapter.slider.SliderAdapter
import com.bitspanindia.groceryapp.databinding.FragmentProductImagesBinding
import com.bitspanindia.groceryapp.data.model.SliderModel

class ProductImagesFragment : Fragment() {
    private lateinit var binding:FragmentProductImagesBinding
    val args: ProductImagesFragmentArgs by navArgs()
    private lateinit var adapter:ProductSmallImageAdapter
    private var imagePos = 0

    val data = listOf(
        SliderModel(
            image= R.drawable.lays1
        ),
        SliderModel(
            image= R.drawable.lays2
        ),
        SliderModel(
            image= R.drawable.lays3
        ),
        SliderModel(
            image=  R.drawable.lays4
        ),
    ) // Replace this with your data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentProductImagesBinding.inflate(inflater, container, false)

        imagePos = args.position

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewpagerSlider()
        binding.viewPager.currentItem = imagePos

       adapter = ProductSmallImageAdapter(data,requireContext()){pos ->
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
        val adapter = SliderAdapter(data){ pos -> }
        binding.viewPager.adapter = adapter
    }

}