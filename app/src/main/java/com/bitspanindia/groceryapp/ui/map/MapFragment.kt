package com.bitspanindia.groceryapp.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.gpsPermission
import com.bitspanindia.groceryapp.AppUtils.showShortToast
import com.bitspanindia.groceryapp.AppUtils.stopLocationUpdates
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
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
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

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
    private var addressString = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        dialogHelper = DialogHelper(requireContext(),requireActivity())
        AppUtils.cartLayoutVisibility(requireActivity(), View.GONE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Places API
        Places.initialize(requireContext(), getString(R.string.google_maps_key))

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        latitude = args.latitude.toDouble()
        longitude = args.longitude.toDouble()

        searchPlaces()

        binding.btnContinue.setOnClickListener {
            if (args.redirectBy == "addAddress"){
                val action =  AddressListFragmentDirections.actionGlobalAddAddressFragment(latitude.toString(),longitude.toString())
                findNavController().navigate(action)
            }else{
                Constant.latitude = latitude
                Constant.longitude = longitude
                Constant.userLocation = addressString
                findNavController().popBackStack()
            }

        }

        binding.clCurrentLocation.setOnClickListener {
            if (AppUtils.checkGpsStatus(requireActivity())) {
                requestLocationUpdates()
            } else {
                AppUtils.gpsPermission(requireContext(), locationSettingsResultLauncher) {
                    requestLocationUpdates()
                }
            }
        }

    }

    private val locationSettingsResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            requestLocationUpdates()
        } else {
            showShortToast(requireContext(),"Error for getting current location")
        }
    }

    private var isLocationUpdatesStarted = false // Keep track of whether location updates are started

    private fun requestLocationUpdates() {
        if (isLocationUpdatesStarted) return // Check if location updates are already started
        dialogHelper.showProgressDialog()
        binding.tvFullAddress.visibility = View.GONE
        binding.tvCity.visibility = View.GONE

        // Create location request
        locationRequest = LocationRequest.Builder(1000L).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()


        // Create location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    dialogHelper.hideProgressDialog()
                    binding.tvFullAddress.visibility = View.VISIBLE
                    binding.tvCity.visibility = View.VISIBLE
                    // Handle location updates here
                    Log.e("TAG", "onLocationResult: ${location.latitude} \n ${location.longitude}", )
                    updateCurrentLocationMarker(LatLng(location.latitude, location.longitude))
                    latitude = location.latitude
                    longitude = location.longitude
//                    getAddressFromLocation(location.latitude, location.longitude)

                    val address = AppUtils.getAddressFromLocation(requireContext(),latitude,longitude)
                    setAddress(address)
                    // Stop location updates after receiving the location
                    stopLocationUpdates(fusedLocationClient,locationCallback)
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
//        addressString = "${address.getAddressLine(0)}, ${address.locality}, ${address.adminArea}, ${address.countryName}"
        addressString = address.getAddressLine(0)?:""
        binding.apply {
            tvCity.text = address.locality
            tvFullAddress.text = addressString
        }

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

    private fun setLocationPre() {
//        dialogHelper.hideProgressDialog()
        latitude = args.latitude.toDouble()
        longitude = args.longitude.toDouble()

        binding.tvFullAddress.visibility = View.VISIBLE
        binding.tvCity.visibility = View.VISIBLE
        // Handle location updates here
        updateCurrentLocationMarker(LatLng(latitude, longitude))

        val address = AppUtils.getAddressFromLocation(requireContext(),latitude,longitude)
        setAddress(address)
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_LOCATION = 1
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        //check if user coming from add address and address list fragment
        if (args.redirectBy=="addAddress" && latitude==0.0 && longitude==0.0){
            if (AppUtils.checkGpsStatus(requireActivity())) {
                requestLocationUpdates()
            } else {
                AppUtils.gpsPermission(requireContext(), locationSettingsResultLauncher) {
                    requestLocationUpdates()
                }
            }
        }

        setLocationPre()

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
            val address = AppUtils.getAddressFromLocation(requireContext(),latitude,longitude)
            setAddress(address)

            // Update marker position
            currentLocationMarker?.position = target
        }


    }

    private fun searchPlaces() {

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the selection of a place.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Get info about the selected place.
                val placeId = place.id
                val fields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)

                // Specify the types of place data to return.
                val request = FetchPlaceRequest.newInstance(placeId, fields)

                // Fetch Place by ID
                val placesClient = Places.createClient(requireContext())
                placesClient.fetchPlace(request).addOnSuccessListener { response ->
                    val latLng = response.place.latLng
                    if (latLng != null) {
                        updateCurrentLocationMarker(latLng)
                    }
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                showShortToast(requireContext(),status.statusMessage.toString())
            }
        })

    }

}