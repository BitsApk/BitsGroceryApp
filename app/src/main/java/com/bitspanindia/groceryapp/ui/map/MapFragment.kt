package com.bitspanindia.groceryapp.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.showShortToast
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentGoogleMapBinding
import com.bitspanindia.groceryapp.ui.mainFragments.AddressListFragmentDirections
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.io.IOException

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentGoogleMapBinding
    private lateinit var dialogHelper: DialogHelper
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocationMarker: Marker? = null
    private val args: MapFragmentArgs by navArgs()
    private var latitude = 0.0
    private var longitude = 0.0

    private val enableGpsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // GPS is enabled, proceed with getting the current location
                requestLocationUpdates()
            } else {
                // User canceled or dismissed the dialog, handle accordingly
                // For example, you can show a message to the user
            }
        }

    override fun onStart() {
        super.onStart()
        // Check if GPS is enabled, if not, prompt the user to enable it
        checkGpsStatus()
    }

    private fun checkGpsStatus() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is not enabled, show a dialog to prompt the user to enable it
            showGpsDialog()
        } else {
            // GPS is enabled, proceed with getting the current location
            requestLocationUpdates()
        }
    }

    private val REQUEST_CHECK_SETTINGS = 132
    private fun showGpsDialog() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // Location settings are satisfied, the client can initialize location requests here
            requestLocationUpdates()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult()
                    exception.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            }
        }
    }


    private var isLocationUpdatesStarted = false // Keep track of whether location updates are started

    private fun requestLocationUpdates() {
        if (isLocationUpdatesStarted) return // Check if location updates are already started
        dialogHelper.showProgressDialog()

        // Create location request
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000 // Update interval in milliseconds
        }

        // Create location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    dialogHelper.hideProgressDialog()
                    // Handle location updates here
                    updateCurrentLocationMarker(LatLng(location.latitude, location.longitude))
                    latitude = location.latitude
                    longitude = location.longitude
//                    getAddressFromLocation(location.latitude, location.longitude)

                    val address = AppUtils.getAddressFromLocation(requireContext(),latitude,longitude)
                    setAddress(address)
                    // Stop location updates after receiving the location
                    stopLocationUpdates()
                    isLocationUpdatesStarted = false // Update flag to indicate that updates are stopped
                    return // Exit loop after handling first location update
                }
            }
        }

        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            requestLocationPermissions()
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        isLocationUpdatesStarted = true // Update flag to indicate that updates are started
    }

    private fun setAddress(address: Address) {
        val addressString = "${address.getAddressLine(0)}, ${address.locality}, ${address.adminArea}, ${address.countryName}"
        binding.apply {
            tvCity.text = address.locality
            tvFullAddress.text = addressString
        }

    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    private fun updateCurrentLocationMarker(latLng: LatLng) {
        if (currentLocationMarker == null) {
            currentLocationMarker = mMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
        } else {
            currentLocationMarker?.position = latLng
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSIONS_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        dialogHelper = DialogHelper(requireContext(),requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize map fragment
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.etSearch.setOnClickListener {
            mMap.clear() // Clear previous markers
            val locationName = binding.etSearch.text.toString()

            if (locationName.isNotEmpty()) {
                val geoCoder = Geocoder(requireContext())
                val addressList = geoCoder.getFromLocationName(locationName, 1)

                if (addressList?.isNotEmpty() == true) {
                    val address = addressList[0]
                    val latLng = LatLng(address.latitude, address.longitude)

                    mMap.addMarker(MarkerOptions().position(latLng).title(locationName))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                } else {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnContinue.setOnClickListener {
            if (args.redirectBy == "addAddress"){
             val action =  AddressListFragmentDirections.actionGlobalAddAddressFragment(latitude.toString(),longitude.toString())
                findNavController().navigate(action)
            }

        }

}

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_LOCATION = 1
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        // Request location updates
        requestLocationUpdates()

        // Implement marker drag listener
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                // Do something when marker drag starts
            }

            override fun onMarkerDrag(marker: Marker) {
                // Do something when marker is dragged
                Log.d("MarkerPosition", "New Marker Position: ${marker.position}")
            }

            override fun onMarkerDragEnd(marker: Marker) {
                // Do something when marker drag ends
                Log.d("MarkerPosition", "Final Marker Position: ${marker.position}")
            }
        })

        // Implement camera idle listener
        mMap.setOnCameraIdleListener {
            val target = mMap.cameraPosition.target
            latitude = target.latitude
            longitude = target.longitude
//            getAddressFromLocation(target.latitude, target.longitude)
            val address = AppUtils.getAddressFromLocation(requireContext(),latitude,longitude)
            setAddress(address)
            // Update marker position
            currentLocationMarker?.position = target
        }
    }

//    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
//        val geocoder = Geocoder(requireContext())
//        try {
//            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
//            if (addresses?.isNotEmpty()==true) {
//                val address = addresses[0]
//                val addressString = "${address.getAddressLine(0)}, ${address.locality}, ${address.adminArea}, ${address.countryName}"
//                binding.apply {
//                    tvCity.text = address.locality
//                    tvFullAddress.text = addressString
//                }
//                Log.d("Address", "Address: $addressString")
//                // Now you can use the addressString as needed, such as displaying it in a TextView
//            }
//        } catch (e: IOException) {
//            Log.e("Geocoder", "Error getting address: ${e.message}")
//        }
//    }

}
