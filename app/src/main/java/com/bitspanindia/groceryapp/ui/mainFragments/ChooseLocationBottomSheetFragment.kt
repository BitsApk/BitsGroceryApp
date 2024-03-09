package com.bitspanindia.groceryapp.ui.mainFragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.requestLocationPermissions
import com.bitspanindia.groceryapp.AppUtils.stopLocationUpdates
import com.bitspanindia.groceryapp.AppUtils.toDp
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.PlaceModel
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.databinding.FragmentChooseLocationBinding
import com.bitspanindia.groceryapp.presentation.adapter.AddressesAdapter
import com.bitspanindia.groceryapp.presentation.adapter.PlaceAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.AddressViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.ProfileViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseLocationBottomSheetFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentChooseLocationBinding
    private lateinit var mBehave: BottomSheetBehavior<FrameLayout>
    private val pvm: ProfileViewModel by activityViewModels()
    private val addViewModel: AddressViewModel by activityViewModels()

    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var dialogHelper: DialogHelper

    private lateinit var placesClient: PlacesClient
    private lateinit var adapter: PlaceAdapter

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val TAG = "LocationBottomSheet"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            mBehave = this.behavior
            mBehave.maxHeight = 600.toDp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseLocationBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()
        dialogHelper = DialogHelper(mContext, mActivity)

        if (addViewModel.redirectFrom == "Cart") {
            binding.clCurrentLocation.visibility = View.GONE
            binding.etSearch.visibility = View.GONE
            binding.tvText.visibility = View.GONE
            binding.btnAddAddress.visibility = View.VISIBLE
        } else {
            binding.clCurrentLocation.visibility = View.VISIBLE
            binding.etSearch.visibility = View.VISIBLE
            binding.tvText.visibility = View.VISIBLE
            binding.btnAddAddress.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAddressList()

        adapter = PlaceAdapter(ArrayList()) { latitude, longitude ->
            hideBottomSheet()
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalMapFragment("home", latitude, longitude)
            )
        }

        binding.rvLocation.adapter = adapter

        // Initialize Places API
        Places.initialize(requireContext(), getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireContext())

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Set up text change listener for EditText
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    placesAvailableVis()
                    fetchPredictions(s.toString())
                } else {
                    placesNotAvailableVis()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.clCurrentLocation.setOnClickListener {
            if (AppUtils.checkGpsStatus(requireActivity())) {
                requestLocationUpdates()
            } else {
                AppUtils.gpsPermission(requireContext(), locationSettingsResultLauncher) {
                    requestLocationUpdates()
                }
            }
        }

        binding.btnAddAddress.setOnClickListener {
            hideBottomSheet()
            val action = CartFragmentDirections.actionGlobalMapFragment("addAddress", "0.0", "0.0")
            findNavController().navigate(action)
        }

    }

    private val locationSettingsResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                requestLocationUpdates()
            } else {
                AppUtils.showShortToast(requireContext(), "Error for getting current location")
            }
        }


    private fun requestLocationUpdates() {
        dialogHelper.showProgressDialog()

        locationRequest =
            LocationRequest.Builder(1000L).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {

                dialogHelper.hideProgressDialog()
                hideBottomSheet()

                stopLocationUpdates(fusedLocationClient, locationCallback)

                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalMapFragment(
                        "home", locationResult.locations[0].latitude.toString(),
                        locationResult.locations[0].longitude.toString()
                    )
                )

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
            requestLocationPermissions(mActivity, 1)
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
//        isLocationUpdatesStarted = true // Update flag to indicate that updates are started
    }

    private fun fetchPredictions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->

            placesAvailableVis()

            val predictions = ArrayList<PlaceModel>()

            for (prediction: AutocompletePrediction in response.autocompletePredictions) {
                // Fetch place details using place ID
                val placeId = prediction.placeId
                val placeRequest =
                    FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.LAT_LNG))
                placesClient.fetchPlace(placeRequest).addOnSuccessListener { response ->
                    val place = response.place
                    val latitude = place.latLng?.latitude ?: 0.0
                    val longitude = place.latLng?.longitude ?: 0.0

                    // Create PlaceModel object with latitude and longitude
                    val placeModel = PlaceModel(
                        city = prediction.getPrimaryText(null).toString(),
                        latitude = latitude,
                        longitude = longitude,
                        fullAddress = prediction.getFullText(null).toString()
                    )
                    predictions.add(placeModel)
                    updateRecyclerView(predictions)
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                }
            }
        }.addOnFailureListener { exception ->
            placesNotAvailableVis()
            exception.printStackTrace()
        }
    }

    private fun placesAvailableVis() {
        binding.tvSavedAdd.visibility = View.GONE
        binding.rvAddedLocation.visibility = View.GONE
        binding.clCurrentLocation.visibility = View.GONE
        binding.rvLocation.visibility = View.VISIBLE
    }

    private fun placesNotAvailableVis() {
        binding.tvSavedAdd.visibility = View.VISIBLE
        binding.rvAddedLocation.visibility = View.VISIBLE
        binding.clCurrentLocation.visibility = View.VISIBLE
        binding.rvLocation.visibility = View.GONE
    }

    private fun updateRecyclerView(predictions: ArrayList<PlaceModel>) {
        adapter.places.clear()
        adapter.places.addAll(predictions)
        adapter.notifyDataSetChanged()
    }


    private fun getAddressList() {
        val getAddressReq = HomeDataReq(userId = Constant.userId)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.getAddressList(getAddressReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            val data = it.body()?.myAddress
                            binding.rvAddedLocation.adapter =
                                AddressesAdapter(mContext, data ?: listOf()) { address, clickType ->
                                    if (clickType == "del") {
                                        removeAddress(address.id ?: "")
                                    } else {
                                        addViewModel.myAddress.value = address
                                        hideBottomSheet()
                                    }

                                }

                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun removeAddress(addressId: String) {
        val removeAddress = CommonDataReq(userId = Constant.userId, addressId = addressId)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.removeAddress(removeAddress).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            Toast.makeText(
                                mContext,
                                "Address Deleted Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            getAddressList()
                        } else {
                            Toast.makeText(
                                mContext,
                                it.body()?.message ?: "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialogHelper.showErrorMsgDialog(
                                it.body()?.message ?: "Something went wrong"
                            ) {}
                        }
                    } else {
                        dialogHelper.showErrorMsgDialog(
                            "Something went wrong"
                        ) {}
                    }
                }
            } catch (e: Exception) {
                dialogHelper.showErrorMsgDialog(
                    "Something went wrong"
                ) {}
            }

        }

    }

    private fun hideBottomSheet() {
        mBehave.isHideable = true
        mBehave.state = BottomSheetBehavior.STATE_HIDDEN
    }

}