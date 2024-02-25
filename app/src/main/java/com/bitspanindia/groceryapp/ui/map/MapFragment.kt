package com.bitspanindia.groceryapp.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.showShortToast
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentGoogleMapBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class MapFragment : Fragment(), OnMapReadyCallback{
    private lateinit var binding:FragmentGoogleMapBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val enableGpsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // GPS is enabled, proceed with getting the current location
            getCurrentLocation()
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
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is not enabled, show a dialog to prompt the user to enable it
            showGpsDialog()
        } else {
            // GPS is enabled, proceed with getting the current location
            getCurrentLocation()
        }
    }

    private fun showGpsDialog() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        enableGpsLauncher.launch(intent)
    }

    private fun getCurrentLocation() {
        // Get current location using FusedLocationProviderClient
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Handle success, update UI with location data
                if (location != null) {
                    showShortToast(requireContext(), location.latitude.toString())
                    Log.e("TAG", "onViewCreated: $location")
                    moveMapToLocation(location.latitude, location.longitude, 2f)
                } else {
                    // Handle the case when the last known location is null
                    showShortToast(requireContext(), "Last known location is not available at the moment")
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure, show error message
                // For example, you can show a toast message with the error
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoogleMapBinding.inflate(inflater, container, false)

        AppUtils.cartLayoutVisibility(requireActivity(),View.GONE)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Places SDK
        Places.initialize(requireContext(),getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireContext())

        // Initialize map fragment
        mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.uiSettings.isZoomControlsEnabled = true
        }

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            showShortToast(requireContext(),"ask for permission")
//            askForPermissions()
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                showShortToast(requireContext(),location?.latitude.toString())
//                Log.e("TAG", "onViewCreated: ${location}", )
//                moveMapToLocation(location?.latitude?:0.0,location?.longitude?:0.0,2f)
//                // Got last known location. In some rare situations this can be null.
//            }

        getCurrentLocation_()

        binding.etSearch.setOnClickListener {
//            getCurrentLocation_()
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
    }


    private fun getCurrentLocation_() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showShortToast(requireContext(),"ask for permission")
            askForPermissions()
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    showShortToast(requireContext(), location.latitude.toString())
                    Log.e("TAG", "onViewCreated: $location")
                    moveMapToLocation(location.latitude, location.longitude, 12f)
                } else {
                    // Handle the case when the last known location is null
                    showShortToast(requireContext(), "Last known location is not available at the moment")
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                showShortToast(requireContext(), "Error getting last known location: ${exception.message}")
            }
    }

    private fun updateMapLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(currentLatLng)
                .title("Current Location")
        )
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                currentLatLng,
                12f
            )
        )

    }

    private fun moveMapToLocation(latitude: Double, longitude: Double, extraZoom: Float) {
        val location = LatLng(latitude, longitude)

        // Calculate the current camera position
        val currentCameraPosition = mMap.cameraPosition

        // Adjust zoom level
        val newZoomLevel = currentCameraPosition.zoom + extraZoom

        // Move camera to the specified location with adjusted zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, newZoomLevel))

        // Add a marker at the specified location
        mMap.addMarker(MarkerOptions().position(location).title("Marker at Location"))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        return
    }

    private fun askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionsToRequest = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            val permissionsToRationale = mutableListOf<String>()

            permissionsToRequest.forEach { permission ->
                if (ContextCompat.checkSelfPermission(requireContext(), permission) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRationale.add(permission)
                }
            }

            if (permissionsToRationale.isNotEmpty()) {
                // Show rationale if needed
                permissionsToRationale.forEach { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) {
                        // TODO: Display rationale for each permission
                        AppUtils.showShortToast(requireContext(), "Rationale needed for $permission")
                    }
                }
            } else {
                // All permissions are already granted, no need to request
//                showShortToast(this,"All permissions already granted")
                return
            }

            // Request permissions
            requestMultiplePermissionsLauncher.launch(permissionsToRequest)
        }
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { entry ->
            val permission = entry.key
            val isGranted = entry.value
            if (isGranted) {
                // Permission granted, handle accordingly
                when (permission) {
                    android.Manifest.permission.ACCESS_FINE_LOCATION -> {
                    }

                    android.Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    }
                }
            } else {
                // Permission denied, handle accordingly
                // Inform user that the app will not show notifications or read videos
                AppUtils.showShortToast(requireContext(), "Permission denied for $permission")
            }
        }
    }

}
