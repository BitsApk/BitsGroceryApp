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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class OrderTrackingFragment : Fragment() , OnMapReadyCallback {
    private lateinit var binding:FragmentOrderTrackingBinding
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
//        updateCurrentLocationMarker(LatLng(userLatitude,userLongitude),"User Location")

        // If the delivery boy marker hasn't been added yet, add it
        if (deliveryBoyMarker == null) {
            updateCurrentLocationMarker(LatLng(userLatitude,userLongitude),"User Location")
        }

    }

//    private fun updateCurrentLocationMarker(latLng: LatLng, locationName: String) {
//        mMap.addMarker(MarkerOptions().position(latLng).title(locationName).icon(BitmapFromVector(
//            requireContext(),
//           if (locationName=="User Location") R.drawable.icon_location_svg else R.drawable.icon_bike)))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
//    }

    private fun updateCurrentLocationMarker(latLng: LatLng, locationName: String) {
        val icon =  if (locationName=="User Location") R.drawable.icon_location_svg else R.drawable.icon_bike
        // If the delivery boy marker already exists, update its position
        if (deliveryBoyMarker != null) {
            deliveryBoyMarker!!.position = latLng
            deliveryBoyMarker!!.title = locationName
            deliveryBoyMarker!!.setIcon(BitmapFromVector(requireContext(),icon))
        } else {
            // Otherwise, create a new marker
            deliveryBoyMarker = mMap.addMarker(MarkerOptions().position(latLng).title(locationName).icon(BitmapFromVector(
                requireContext(), icon)))
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
        // Move camera to the new position with extra zoom
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, calculateZoomLevel()))
    }

    private fun calculateZoomLevel(): Float {
        val builder = LatLngBounds.Builder()
        builder.include(LatLng(userLatitude, userLongitude))
        deliveryBoyMarker?.position?.let { builder.include(it) }
        val bounds = builder.build()
        val padding = 200 // Padding around bounds in pixels
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        return mMap.cameraPosition.zoom
    }

//    private fun updateMapWithLocations(orderId: String, userId: String="deepak123") {
//        val db = Firebase.firestore
//        val orderRef = db.collection("orders").document(orderId)
//
//        orderRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w("TAG", "Listen failed", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && snapshot.exists()) {
//                val geoPoint = snapshot.getGeoPoint("geoPoint")
//                if (geoPoint != null) {
//                    latitude = geoPoint.latitude
//                    longitude = geoPoint.longitude
//                    val location = LatLng(latitude, longitude)
//
//                    drawPolylineOnMap(getPolylineOptions())
//                    updateCurrentLocationMarker(location, "Delivery Boy Location")
//
//                    // Now you have the location (LatLng) from the Firestore document
//                    // You can use it as needed, such as updating markers on a map
//                } else {
//                    Log.e("TAG", "GeoPoint is null")
//                }
//            } else {
//                Log.e("TAG", "Document does not exist")
//            }
//        }
//    }

    private fun updateMapWithLocations(orderId: String, userId: String="deepak123") {
        val db = Firebase.firestore
        val orderRef = db.collection("orders").document(orderId)

        orderRef.addSnapshotListener { snapshot, e ->
            // Existing code...
            if (snapshot != null && snapshot.exists()) {
                val geoPoint = snapshot.getGeoPoint("geoPoint")
                if (geoPoint != null) {
                    latitude = geoPoint.latitude
                    longitude = geoPoint.longitude
                    val location = LatLng(latitude, longitude)

                    // Update the marker's position
                    updateCurrentLocationMarker(location, "Delivery Boy Location")

                    // Update the polyline
                    drawPolylineOnMap(getPolylineOptions())
                } else {
                    Log.e("TAG", "GeoPoint is null")
                }
            } else {
                Log.e("TAG", "Document does not exist")
            }
        }
    }


//    private fun drawPolylineOnMap(polylineOptions: PolylineOptions) {
//        // Add polyline to the map
//        mMap.addPolyline(polylineOptions)
//    }

    private fun drawPolylineOnMap(polylineOptions: PolylineOptions) {
        // If the polyline already exists, remove it before drawing a new one
        polyline?.remove()
        // Add the new polyline to the map
        polyline = mMap.addPolyline(polylineOptions)
    }


    private fun getPolylineOptions(): PolylineOptions {
        // Create polyline options
        val polylineOptions = PolylineOptions()
        // Add coordinates to the polyline options (example coordinates)
        polylineOptions.add(LatLng(latitude, longitude)) // San Francisco
            .add(LatLng(userLatitude,userLongitude))   // Los Angeles
        // Add more coordinates as needed...
        // Customize polyline appearance
        polylineOptions.width(12f)  // Width of the polyline
            .color(Color.RED) // Color of the polyline
            .geodesic(true)  // Whether to draw the polyline as a geodesic (true/false)
        return polylineOptions
    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(
            context, vectorResId
        )

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}