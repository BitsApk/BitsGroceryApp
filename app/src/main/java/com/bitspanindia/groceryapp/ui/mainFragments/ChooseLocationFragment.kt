package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.presentation.adapter.PlaceAdapter
import com.bitspanindia.groceryapp.databinding.FragmentChooseLocationBinding
import com.bitspanindia.groceryapp.data.model.PlaceModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

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
        // Fetching API_KEY which we wrapped
//        val ai: ApplicationInfo = requireContext().packageManager
//            .getApplicationInfo(requireContext().packageName, PackageManager.GET_META_DATA)
//        val value = ai.metaData["api_key"]
//        val apiKey = value.toString()
//
//        // Initializing the Places API
//        // with the help of our API_KEY
//        if (!Places.isInitialized()) {
//            Places.initialize(requireContext(), apiKey)
//        }
//
//        // Initialize Autocomplete Fragments
//        // from the main activity layout file
//        val autocompleteSupportFragment1 = requireActivity().supportFragmentManager.findFragmentById(R.id.autocomplete_fragment1) as AutocompleteSupportFragment?
//
//        // Information that we wish to fetch after typing
//        // the location and clicking on one of the options
//        autocompleteSupportFragment1!!.setPlaceFields(
//            listOf(
//
//                Place.Field.NAME,
//                Place.Field.ADDRESS,
//                Place.Field.PHONE_NUMBER,
//                Place.Field.LAT_LNG,
//                Place.Field.OPENING_HOURS,
//                Place.Field.RATING,
//                Place.Field.USER_RATINGS_TOTAL
//
//            )
//        )
//
//        // Display the fetched information after clicking on one of the options
//        autocompleteSupportFragment1.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//
//                // Text view where we will
//                // append the information that we fetch
//                val textView = binding.root.findViewById<TextView>(R.id.tv1)
//
//                // Information about the place
//                val name = place.name
//                val address = place.address
//                val phone = place.phoneNumber.toString()
//                val latlng = place.latLng
//                val latitude = latlng?.latitude
//                val longitude = latlng?.longitude
//
//                val isOpenStatus : String = if(place.isOpen == true){
//                    "Open"
//                } else {
//                    "Closed"
//                }
//
//                val rating = place.rating
//                val userRatings = place.userRatingsTotal
//
//                textView.text = "Name: $name \nAddress: $address \nPhone Number: $phone \n" +
//                        "Latitude, Longitude: $latitude , $longitude \nIs open: $isOpenStatus \n" +
//                        "Rating: $rating \nUser ratings: $userRatings"
//            }
//
//            override fun onError(status: Status) {
//                Toast.makeText(requireContext(),"Some error occurred", Toast.LENGTH_SHORT).show()
//            }
//        })

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