package com.bitspanindia.groceryapp.ui.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.showShortToast
import com.bitspanindia.groceryapp.AppUtils.stopLocationUpdates
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.CheckLocalityReq
import com.bitspanindia.groceryapp.data.model.response.MyAddress
import com.bitspanindia.groceryapp.databinding.FragmentGoogleMapBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.AddressViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.HomeViewModel
import com.bitspanindia.groceryapp.ui.mainFragments.AddressListFragmentDirections
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentGoogleMapBinding
    private lateinit var dialogHelper: DialogHelper
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocationMarker: Marker? = null
    private val args: MapFragmentArgs by navArgs()

    private var isLocationUpdatesStarted = false
    private lateinit var address: Address
    private var latitude = 0.0
    private var longitude = 0.0

    private val homeVM: HomeViewModel by activityViewModels()
    private val addViewModel: AddressViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        dialogHelper = DialogHelper(requireContext(), requireActivity())
        AppUtils.cartLayoutVisibility(requireActivity(), View.GONE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Places.initialize(requireContext(), getString(R.string.google_maps_key))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        latitude = args.latitude.toDouble()
        longitude = args.longitude.toDouble()

        searchPlaces()

        binding.btnContinue.setOnClickListener {
            if (args.redirectBy == "addAddress") {
                val action = AddressListFragmentDirections.actionGlobalAddAddressFragment(
                    latitude.toString(),
                    longitude.toString()
                )
                findNavController().navigate(action)
            } else {
                Constant.latitude = latitude
                Constant.longitude = longitude
                Constant.userLocation = address.getAddressLine(0)

                addViewModel.myAddress.value = MyAddress(
                    latitude = Constant.latitude.toString(),
                    longitude = Constant.longitude.toString(),
                    permanentAdd = address.getAddressLine(0),
                    city = address.locality,
                    country = address.countryName,
                    zipcode = address.postalCode
                )
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

    private fun requestLocationUpdates() {
        if (isLocationUpdatesStarted) return
        dialogHelper.showProgressDialog()
        binding.tvFullAddress.visibility = View.GONE
        binding.tvCity.visibility = View.GONE

        locationRequest =
            LocationRequest.Builder(1000L).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return

                dialogHelper.hideProgressDialog()
                binding.tvFullAddress.visibility = View.VISIBLE
                binding.tvCity.visibility = View.VISIBLE

                for (location in locationResult.locations) {

                    latitude = location.latitude
                    longitude = location.longitude

                    updateCurrentLocationMarker(LatLng(latitude, longitude))
                    address = AppUtils.getAddressFromLocation(requireContext(), latitude, longitude)
                    setAddress(address)

                    stopLocationUpdates(fusedLocationClient, locationCallback)
                    isLocationUpdatesStarted = false
                    return
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AppUtils.requestLocationPermissions(requireActivity(), 1)
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        isLocationUpdatesStarted = true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        //check if user coming from add address or address list fragment
        if (args.redirectBy == "addAddress" && latitude == 0.0 && longitude == 0.0) {
            if (AppUtils.checkGpsStatus(requireActivity())) {
                requestLocationUpdates()
            } else {
                AppUtils.gpsPermission(requireContext(), locationSettingsResultLauncher) {
                    requestLocationUpdates()
                }
            }
        }

        setLocationPre()

        mMap.setOnCameraIdleListener {
            val target = mMap.cameraPosition.target

            setAddWithLocality(target)
        }

        mMap.setOnMapClickListener { latLng ->
            setAddWithLocality(latLng)
        }


    }

    private fun setAddWithLocality(latLng: LatLng){
        latitude = latLng.latitude
        longitude = latLng.longitude
        address = AppUtils.getAddressFromLocation(requireContext(), latitude, longitude)
        setAddress(address)
        currentLocationMarker?.position = latLng
        checkLocality(latitude,longitude)
    }

    private fun searchPlaces() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val placeId = place.id
                val fields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)

                val request = FetchPlaceRequest.newInstance(placeId ?: "", fields)
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
                Log.d("TAG", "onError: ${status.statusMessage}")
            }
        })

    }

    private fun checkLocality(latitude: Double, longitude: Double) {
        showProgress()

        val checkLocalityReq = CheckLocalityReq()
        checkLocalityReq.latitude = latitude
        checkLocalityReq.longitude = longitude

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                homeVM.checkLocality(checkLocalityReq).let {
                    hideProgress()
                    if (it.body() != null) {
                        val data = it.body()

                        if (data?.statusCode == 200) {
                            binding.tvCity.visibility = View.VISIBLE
                            binding.tvFullAddress.visibility = View.VISIBLE
                            binding.btnContinue.isEnabled = true
                        } else {
                            binding.tvFullAddress.text = getString(
                                R.string.one_str,
                                "Bits Grocery is not available at ${address.getAddressLine(0)} at the moment.\nPlease select a different location."
                            )
                            binding.tvCity.text = getString(R.string.one_str, "Oops !")
                            binding.btnContinue.isEnabled = false
                        }
                    } else {
                        Log.d("TAG", "checkLocality: Something went wrong")
                    }
                }
            } catch (e: Exception) {
                Log.d("TAG", "checkLocality: Something went wrong")
            }
        }
    }

    private fun setAddress(address: Address) {
        binding.apply {
            tvCity.text = address.locality
            tvFullAddress.text = address.getAddressLine(0) ?: ""
        }

    }

    private fun setLocationPre() {
        latitude = args.latitude.toDouble()
        longitude = args.longitude.toDouble()

        binding.tvFullAddress.visibility = View.VISIBLE
        binding.tvCity.visibility = View.VISIBLE

        updateCurrentLocationMarker(LatLng(latitude, longitude))

        address = AppUtils.getAddressFromLocation(requireContext(), latitude, longitude)
        setAddress(address)
    }

    private fun updateCurrentLocationMarker(latLng: LatLng) {
        if (currentLocationMarker == null) {
            currentLocationMarker =
                mMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
        } else {
            currentLocationMarker?.position = latLng
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private val locationSettingsResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                requestLocationUpdates()
            } else {
                showShortToast(requireContext(), "Error for getting current location")
            }
        }


    private fun showProgress() {
        binding.apply {
            progress.visibility = View.VISIBLE
            tvCity.visibility = View.INVISIBLE
            tvFullAddress.visibility = View.INVISIBLE
        }
    }

    private fun hideProgress() {
        binding.apply {
            progress.visibility = View.GONE
            tvCity.visibility = View.VISIBLE
            tvFullAddress.visibility = View.VISIBLE
        }
    }

}