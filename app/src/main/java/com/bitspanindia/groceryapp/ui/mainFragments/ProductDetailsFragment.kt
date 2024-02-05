package com.bitspanindia.groceryapp.ui.mainFragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.presentation.adapter.slider.SliderAdapter
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.databinding.FragmentProductDetailsBinding
import com.bitspanindia.groceryapp.data.model.SliderModel


class ProductDetailsFragment : Fragment() {
    private lateinit var binding:FragmentProductDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewpagerSlider()
        setProducts()


        binding.tvOfferPrice.paintFlags = binding.tvOfferPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


    }

    private fun setViewpagerSlider() {

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
        val adapter = SliderAdapter(data){ pos ->
            val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToProductImagesFragment(pos)
            findNavController().navigate(action)
        }
        binding.viewPager.adapter = adapter

        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }

    private fun setProducts() {

        val data = listOf(
            SliderModel(
                name = "Kurkure",
                quantity = "82 g",
                offPrice = "र60",
                price = "र54",
                image= R.drawable.kukure
            ),
            SliderModel(
                name = "Uncle Chips",
                quantity = "2 Pieces",
                offPrice = "र0",
                price = "र40",
                image= R.drawable.uncle_chips
            ),
            SliderModel(
                name = "Bingo Tedhe Medhe",
                quantity = "90 g",
                offPrice = "र60",
                price = "र42",
                image= R.drawable.tedhe_medhe
            ),
           SliderModel(
                name = "Lay's American",
                quantity = "50 g",
                offPrice = "र0",
                price = "र20",
                image= R.drawable.lays_american
            ),
        ) // Replace this with your data
//        binding.rvProducts.adapter= ProductsAdapter(data)
    }

}