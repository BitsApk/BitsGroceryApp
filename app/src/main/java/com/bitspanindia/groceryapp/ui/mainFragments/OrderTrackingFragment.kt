package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentOrderTrackingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

class OrderTrackingFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentOrderTrackingBinding
    private lateinit var dialogHelper: DialogHelper
    private lateinit var mMap: GoogleMap
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private var latitude = 0.0
    private var longitude = 0.0
    private var userLongitude = 78.56315524529306
    private var userLatitude = 28.586321056126064

    // Define marker and polyline variables
    private var deliveryBoyMarker: Marker? = null
    private var polyline: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderTrackingBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()

        dialogHelper = DialogHelper(mContext, mActivity)

        setLocationOnMap()

        return binding.root
    }

    private fun setLocationOnMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve location data from Firestore and update map
        updateMapWithLocations("order123")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        updateCurrentLocationMarker(LatLng(28.5997677, 78.57035796),"User Location")

    }

    private var userMarker: Marker? = null

    private fun updateCurrentLocationMarker(latLng: LatLng, locationName: String) {
        val icon = if (locationName == "User Location") R.drawable.icon_location_svg else R.drawable.icon_bike

        if (locationName == "User Location") {
            // If the user marker already exists, update its position and icon
            if (userMarker != null) {
                userMarker!!.position = latLng
                userMarker!!.title = locationName
                userMarker!!.setIcon(BitmapFromVector(requireContext(), R.drawable.icon_location_svg))
            } else {
                // Otherwise, create a new user marker
                userMarker = mMap.addMarker(
                    MarkerOptions().position(latLng).title(locationName).icon(
                        BitmapFromVector(
                            requireContext(),
                            R.drawable.icon_location_svg
                        )
                    )
                )
            }
        } else {
            // For the delivery boy marker
            // If the delivery boy marker already exists, update its position and icon
            if (deliveryBoyMarker != null) {
                deliveryBoyMarker!!.position = latLng
                deliveryBoyMarker!!.title = locationName
                deliveryBoyMarker!!.setIcon(BitmapFromVector(requireContext(), R.drawable.icon_bike))
            } else {
                // Otherwise, create a new delivery boy marker
                deliveryBoyMarker = mMap.addMarker(
                    MarkerOptions().position(latLng).title(locationName).icon(
                        BitmapFromVector(
                            requireContext(),
                            R.drawable.icon_bike
                        )
                    )
                )
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
    }

    private fun updateMapWithLocations(orderId: String, userId: String = "deepak123") {
        val db = FirebaseFirestore.getInstance()
        val orderRef = db.collection("orders").document(orderId)

        orderRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                val geoPoint = snapshot.getGeoPoint("geoPoint")
                if (geoPoint != null) {
                    latitude = geoPoint.latitude
                    longitude = geoPoint.longitude
                    val location = LatLng(latitude, longitude)

                    // Update the marker's position
                    updateCurrentLocationMarker(location, "Delivery Boy Location")

                    // Fetch directions and draw polyline
                    fetchDirectionsAndDrawPolyline(location)
                } else {
                    Log.e("TAG", "GeoPoint is null")
                }
            } else {
                Log.e("TAG", "Document does not exist")
            }
        }
    }

    private fun fetchDirectionsAndDrawPolyline(destination: LatLng) {
        val origin = LatLng(destination.latitude, destination.longitude)
        val geoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .build()

        val directionsApiRequest: DirectionsApiRequest = com.google.maps.DirectionsApi.newRequest(geoApiContext)
        directionsApiRequest.mode(TravelMode.DRIVING)
        directionsApiRequest.origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
        directionsApiRequest.destination(com.google.maps.model.LatLng(28.5997677, 78.57035796))

        directionsApiRequest.setCallback(object : com.google.maps.PendingResult.Callback<DirectionsResult?> {
            override fun onResult(result: DirectionsResult?) {
                // Execute UI operations on the main thread
                activity?.runOnUiThread {

                    val route = result!!.routes[0]
//                    val polylineOptions = PolylineOptions()
//                    val polylinePoints = route.overviewPolyline.decodePath()
//
//                    Log.e("TAG", "onResult: $route")

//                    for (point in polylinePoints) {
//                        Log.e("TAG", "onResultPoint: ${point.lat} \n ${point.lng}")
//                        polylineOptions.add(
//                            LatLng(
//                                point.lat,
//                                point.lng
//                            )
//                        )
//                    }
//
//                    // Remove existing polyline if any
//                    polyline?.remove()
//
//                    // Add new polyline to the map
//                    polyline = mMap.addPolyline(polylineOptions)

                    drawPolylineOnMap(getPolylineOptions(route.overviewPolyline.decodePath()))

                }
            }

            override fun onFailure(e: Throwable?) {
                // Execute UI operations on the main thread
                activity?.runOnUiThread {
                    Log.e("TAG", "Failed to get directions: ${e?.message}")
                }
            }
        })
    }

    private fun drawPolylineOnMap(polylineOptions: PolylineOptions) {
        // If the polyline already exists, remove it before drawing a new one
        polyline?.remove()
        // Add the new polyline to the map
        polyline = mMap.addPolyline(polylineOptions)
    }


    private fun getPolylineOptions(polylinePoints: MutableList<com.google.maps.model.LatLng>): PolylineOptions {
        // Create polyline options
        val polylineOptions = PolylineOptions()
        // Add coordinates to the polyline options (example coordinates)
//        polylineOptions.add(LatLng(latitude, longitude)) // San Francisco
//            .add(LatLng(userLatitude,userLongitude))   // Los Angeles

        for (point in polylinePoints) {
            Log.e("TAG", "onResultPoint: ${point.lat} \n ${point.lng}")
            polylineOptions.add(
                LatLng(
                    point.lat,
                    point.lng
                )
            )
        }

        // Remove existing polyline if any
//        polyline?.remove()


        // Add more coordinates as needed...
        // Customize polyline appearance
        polylineOptions.width(12f)  // Width of the polyline
            .color(Color.RED) // Color of the polyline
            .geodesic(true)  // Whether to draw the polyline as a geodesic (true/false)
        return polylineOptions
    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
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
}
