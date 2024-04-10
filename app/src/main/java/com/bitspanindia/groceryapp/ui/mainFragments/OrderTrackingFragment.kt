package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.databinding.FragmentOrderTrackingBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.ProfileViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderTrackingFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentOrderTrackingBinding
    private lateinit var dialogHelper: DialogHelper
    private lateinit var mMap: GoogleMap
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val args: OrderTrackingFragmentArgs by navArgs()
    private var userLatitude = 0.0
    private var userLongitude = 0.0

    // Define marker and polyline variables
    private var deliveryBoyMarker: Marker? = null
    private var deliveryBoyLocationListener: ListenerRegistration? = null
    private var polyline: Polyline? = null

    private val pvm: ProfileViewModel by activityViewModels()
    private var runnable: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())


    override fun onStart() {
        super.onStart()

        userLatitude = args.latitude.toDouble()
        userLongitude = args.longitude.toDouble()

        Log.e(
            "TAG",
            "onStart: $userLatitude \n $userLongitude \n ${
                AppUtils.getAddressFromLocation(
                    requireContext(),
                    userLatitude,
                    userLongitude
                )
            }"
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderTrackingBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()

        dialogHelper = DialogHelper(mContext, mActivity)

        setupLocationOnMap()

        return binding.root
    }


    private fun setupLocationOnMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDeliveryAddVal.text = AppUtils.getAddressFromLocation(mContext, userLatitude, userLongitude).getAddressLine(0)

        callOrderDetailsApi()
        trackDeliveryBoyLoc()

//        binding.btnTrackOrder.setOnClickListener {
//            navigateToOrderDetails()
//        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopOrderDetailsApiCall()
    }

    private fun callOrderDetailsApi() {
        runnable = object : Runnable {
            override fun run() {
                handler.post {
                    getOrderDetails()
                }
                handler.postDelayed(this, 15000)
            }
        }
        handler.post(runnable!!)
    }

    private fun stopOrderDetailsApiCall() {
        handler.removeCallbacks(runnable!!)
    }

    private fun getOrderDetails() {
        val orderDetailReq = CommonDataReq(userId = Constant.userId, orderId = args.orderId)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.getOrderDetails(orderDetailReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            when (it.body()?.orderStatus) {
                                "S" -> {}
                                else -> {
                                    navigateToOrderDetails(it.body()?.orderId ?: "")
                                }
                            }
                        } else {
                            dialogHelper.showErrorMsgDialog(
                                it.body()?.message ?: "Something went wrong"
                            ) {
                                findNavController().popBackStack()
                            }
                        }
                    } else {
                        dialogHelper.showErrorMsgDialog("Something went wrong") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                dialogHelper.showErrorMsgDialog("Something went wrong") {
                    findNavController().popBackStack()
                }
            }

        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        updateCurrentLocationMarker(LatLng(userLatitude, userLongitude), "User Location")

    }


    private fun updateCurrentLocationMarker(latLng: LatLng, locationName: String) {

        if (locationName == "User Location") {
            // Add marker for user location
            mMap.addMarker(
                MarkerOptions().position(latLng).title(locationName).icon(
                    bitmapFromVector(
                        requireContext(),
                        R.drawable.icon_location_svg
                    )
                )
            )
        } else {
            // For the delivery boy marker
            // If the delivery boy marker already exists, update its position and icon
            if (deliveryBoyMarker != null) {
                deliveryBoyMarker!!.position = latLng
                deliveryBoyMarker!!.title = locationName
                deliveryBoyMarker!!.setIcon(
                    bitmapFromVector(
                        requireContext(),
                        R.drawable.icon_bike
                    )
                )
            } else {
                // Otherwise, create a new delivery boy marker
                deliveryBoyMarker = mMap.addMarker(
                    MarkerOptions().position(latLng).title(locationName).icon(
                        bitmapFromVector(
                            requireContext(),
                            R.drawable.icon_bike
                        )
                    )
                )
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
    }

    private fun trackDeliveryBoyLoc() {
        val db = FirebaseFirestore.getInstance()
        val orderRef = db.collection("orders").document(args.orderId)
//        val orderRef = db.collection("orders").document("order123")

        // Listen for changes in the delivery boy's location in real-time
        deliveryBoyLocationListener = orderRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                val geoPoint = snapshot.getGeoPoint("geoPoint")
                if (geoPoint != null) {
                    val deliveryBoyLatitude = geoPoint.latitude
                    val deliveryBoyLongitude = geoPoint.longitude
                    val deliveryBoyLocation = LatLng(deliveryBoyLatitude, deliveryBoyLongitude)

                    updateCurrentLocationMarker(deliveryBoyLocation, "Delivery Boy Location")
                    fetchDirectionsAndDrawPolyline(deliveryBoyLocation)

                } else {
                    Log.e("TAG", "GeoPoint is null")
                }
            } else {
                Log.e("TAG", "Document does not exist")
            }
        }
    }

    private fun fetchDirectionsAndDrawPolyline(origin: LatLng) {
        val destination = LatLng(userLatitude, userLongitude)
        val geoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .build()

        val directionsApiRequest: DirectionsApiRequest =
            com.google.maps.DirectionsApi.newRequest(geoApiContext)
        directionsApiRequest.mode(TravelMode.DRIVING)
        directionsApiRequest.origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
        directionsApiRequest.destination(
            com.google.maps.model.LatLng(
                destination.latitude,
                destination.longitude
            )
        )

        directionsApiRequest.setCallback(object :
            com.google.maps.PendingResult.Callback<DirectionsResult?> {
            override fun onResult(result: DirectionsResult?) {

                activity?.runOnUiThread {

                    val route = result!!.routes[0]
                    val polylineOptions = getPolylineOptions(route.overviewPolyline.decodePath())
                    drawPolylineOnMap(polylineOptions)

                    // Calculate midpoint between origin and destination
                    val midpoint = LatLng(
                        (origin.latitude + destination.latitude) / 2,
                        (origin.longitude + destination.longitude) / 2
                    )

//                    val distance = calculateDistanceAlongPolyline(polylineOptions.points)
//                    val duration = calculateDuration(route)
//                    binding.tvDistanceValue.text = getString(R.string.distance, distance)
//                    binding.tvTimeValue.text = getString(R.string.distance, duration)

                    // Calculate bearing between origin and destination
                    val bearing = calculateBearing(origin, destination)

                    // Adjust the camera position to focus on midpoint with bearing
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(midpoint, 20f))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(midpoint, 20f), 2000, null)
                    mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(midpoint)
                                .zoom(20f)
                                .bearing(bearing) // Set bearing
                                .tilt(45f) // Optionally set tilt
                                .build()
                        ), 2000, null
                    )

                }
            }

            override fun onFailure(e: Throwable?) {
                activity?.runOnUiThread {
                    Log.e("TAG", "Failed to get directions: ${e?.message}")
                }
            }
        })
    }

    private fun drawPolylineOnMap(polylineOptions: PolylineOptions) {
        polyline?.remove()
        polyline = mMap.addPolyline(polylineOptions)
    }

    private fun getPolylineOptions(polylinePoints: MutableList<com.google.maps.model.LatLng>): PolylineOptions {
        val polylineOptions = PolylineOptions()

        for (point in polylinePoints) {
            polylineOptions.add(
                LatLng(
                    point.lat,
                    point.lng
                )
            )
        }

        polylineOptions.width(12f)
            .color(Color.RED)
            .geodesic(true)

        return polylineOptions
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        deliveryBoyLocationListener?.remove()
    }

    private fun calculateBearing(startPoint: LatLng, endPoint: LatLng): Float {
        val startLocation = Location("")
        startLocation.latitude = startPoint.latitude
        startLocation.longitude = startPoint.longitude

        val endLocation = Location("")
        endLocation.latitude = endPoint.latitude
        endLocation.longitude = endPoint.longitude

        return startLocation.bearingTo(endLocation)
    }

    private fun navigateToOrderDetails(orderId: String) {
        findNavController().navigate(
            OrderTrackingFragmentDirections.actionOrderTrackingFragmentToOrderDetailsFragment(
                orderId
            )
        )
    }

}