package com.bitspanindia.groceryapp.ui.mainFragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentAddAddressBinding
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions

//class AddAddressFragment : Fragment() , OnMapReadyCallback,
//    GoogleMap.OnMyLocationButtonClickListener{
//    private lateinit var binding: FragmentAddAddressBinding
//    private lateinit var mMap: GoogleMap

class AddAddressFragment : Fragment(){
    private lateinit var binding: FragmentAddAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding =  FragmentAddAddressBinding.inflate(inflater, container, false)

//        val mapFragment =
//            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        return binding.root
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//        mMap.setOnMyLocationButtonClickListener(this)
//
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            mMap.isMyLocationEnabled = true
//            // Show the current location on the map
//            showCurrentLocation()
//        }
//    }
//
//    override fun onMyLocationButtonClick(): Boolean {
//        // Handle the My Location button click
//        // You can customize this behavior if needed
//        return false
//    }
//
//    private fun showCurrentLocation() {
//        // Get the last known location from the FusedLocationProviderClient
//        // Note: You should handle location updates more gracefully in a production app
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location ->
//                if (location != null) {
//                    val currentLatLng = LatLng(location.latitude, location.longitude)
//                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(
//                    requireContext(),
//                    "Error getting location: ${e.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//    }
//
//    companion object {
//        const val DEFAULT_ZOOM = 15.0f
//        const val LOCATION_PERMISSION_REQUEST_CODE = 123
//    }

}