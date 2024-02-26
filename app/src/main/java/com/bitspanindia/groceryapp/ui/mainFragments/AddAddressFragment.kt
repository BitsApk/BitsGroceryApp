package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.location.Address
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.showShortToast
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.AddAddressReq
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.databinding.FragmentAddAddressBinding
import com.bitspanindia.groceryapp.presentation.adapter.OrderItemAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.ProfileViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAddressFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentAddAddressBinding
    private lateinit var mMap: GoogleMap
    private val args:AddAddressFragmentArgs by navArgs()
    private lateinit var dialogHelper: DialogHelper
    private lateinit var mContext:Context
    private lateinit var mActivity:FragmentActivity
    private val pvm: ProfileViewModel by activityViewModels()
    private val addressReq = AddAddressReq()
    private lateinit var address : Address

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding =  FragmentAddAddressBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()

        dialogHelper = DialogHelper(mContext,mActivity)

        setLocationOnMap()
        binding.chipGrpAddressType.check(R.id.chipHome)
        binding.chipHome.isChecked = true

        return binding.root
    }

    private fun setLocationOnMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChange.setOnClickListener {
            val action = AddAddressFragmentDirections.actionGlobalMapFragment("addAddress")
            findNavController().navigate(action)
        }

        binding.btnAddAddress.setOnClickListener {
            addAddress()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        updateCurrentLocationMarker(LatLng(args.latitude.toDouble(),args.longitude.toDouble()))

        address = AppUtils.getAddressFromLocation(requireContext(),args.latitude.toDouble(),args.longitude.toDouble())
        val fullAddress = "${address.getAddressLine(0)}, ${address.locality}, ${address.adminArea}, ${address.countryName}"
        binding.tvAddress.text = address.locality
        binding.tvFullAddress.text = fullAddress

    }

    private fun updateCurrentLocationMarker(latLng: LatLng) {
        mMap.addMarker(MarkerOptions().position(latLng).title("MarkerLocation"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }


    private fun addAddress() {
        dialogHelper.showProgressDialog()
        binding.apply {
            addressReq.userId = Constant.userId
            addressReq.perAdd = etAreaStreet.text.toString()
            addressReq.city = address.locality
            addressReq.locality = address.locality
            addressReq.country = address.countryName
            addressReq.state = address.adminArea
            addressReq.zipcode = address.postalCode
            addressReq.latitude = args.latitude
            addressReq.longitude = args.longitude
            addressReq.landMark = etLandMark.text.toString()
            addressReq.phone = etPhoneNumber.text.toString()
            addressReq.addressName = when (chipGrpAddressType.checkedChipId) {
                R.id.chipHome -> "Home"
                R.id.chipWork -> "Work"
                else -> "Other"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.addAddress(addressReq).let {
                    dialogHelper.hideProgressDialog()
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            showShortToast(mContext,"Address add successfully")
                            findNavController().popBackStack()
                        } else {
                            Toast.makeText(mContext, it.body()?.message ?: "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        }

    }

}