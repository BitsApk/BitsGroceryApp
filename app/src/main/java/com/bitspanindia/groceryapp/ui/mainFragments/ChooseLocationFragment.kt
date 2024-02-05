package com.bitspanindia.groceryapp.ui.mainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.PlaceAdapter
import com.bitspanindia.groceryapp.databinding.FragmentChooseLocationBinding
import com.bitspanindia.groceryapp.data.model.PlaceModel

class ChooseLocationFragment : Fragment() {
    private lateinit var binding:FragmentChooseLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentChooseLocationBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLocationData()

    }

    private fun setLocationData(){
        val list = listOf<PlaceModel>(
            PlaceModel("1","Sambhal","Lodhi Sarai Near Railway Station"),
            PlaceModel("1","Moradabad","Pital Nagri Moradabad"),
            PlaceModel("1","Delhi","Najafgarh , Near Dhansa Bus Stand"),
        )

        binding.rvLocation.adapter = PlaceAdapter(list)
    }

}