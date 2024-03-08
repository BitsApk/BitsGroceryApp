package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.bitspanindia.groceryapp.databinding.FragmentAddAddressBinding
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
    private val args: AddAddressFragmentArgs by navArgs()
    private lateinit var dialogHelper: DialogHelper
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val pvm: ProfileViewModel by activityViewModels()
    private val addressReq = AddAddressReq()
    private lateinit var address: Address

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()

        dialogHelper = DialogHelper(mContext, mActivity)

        setLocationOnMap()

        return binding.root
    }

    private fun setLocationOnMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChange.setOnClickListener {
            val action = AddAddressFragmentDirections.actionGlobalMapFragment("addAddress",args.latitude,args.longitude)
            findNavController().navigate(action)
        }

        binding.btnAddAddress.setOnClickListener {
            if (validation()) {
                addAddress()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        updateCurrentLocationMarker(LatLng(args.latitude.toDouble(), args.longitude.toDouble()))

        address = AppUtils.getAddressFromLocation(
            requireContext(),
            args.latitude.toDouble(),
            args.longitude.toDouble()
        )
        val fullAddress = "${address.getAddressLine(0)}, ${address.locality}, ${address.adminArea}, ${address.countryName}"
        binding.tvAddress.text = address.locality
        binding.tvFullAddress.text = fullAddress
        binding.etAreaStreet.setText(fullAddress)

    }

    private fun updateCurrentLocationMarker(latLng: LatLng) {
        mMap.addMarker(MarkerOptions().position(latLng).title("MarkerLocation"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }


    private fun addAddress() {
        dialogHelper.showProgressDialog()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.addAddress(addressReq).let {
                    dialogHelper.hideProgressDialog()
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            showShortToast(mContext, "Address add successfully")
                            findNavController().popBackStack()
                        } else {
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

    private fun validation(): Boolean {
        binding.apply {
            addressReq.userId = Constant.userId
            addressReq.perAdd = etAreaStreet.text.toString()
            addressReq.city = address.locality
            addressReq.country = address.countryName
            addressReq.state = address.adminArea
            addressReq.zipcode = address.postalCode
            addressReq.latitude = args.latitude
            addressReq.longitude = args.longitude
            addressReq.phone = etPhoneNumber.text.toString()
            addressReq.addressName = when (addTypeChipGroup.checkedChipId) {
                R.id.chipHome -> "Home"
                R.id.chipWork -> "Work"
                else -> "Other"
            }

            with(addressReq) {
                if (userId=="0" || userId.isNullOrEmpty()){
                    showShortToast(mContext,"Please login before add address")
                    return false
                }
                else if (perAdd.isNullOrEmpty()) {
                    etAreaStreet.error = "Enter Full Address"
                    etAreaStreet.isFocusable = true
                    return false
                } else if (phone?.length != 10) {
                    etPhoneNumber.error = "Enter valid phone number"
                    etPhoneNumber.isFocusable = true
                    return false
                }
                else if (addressName.isNullOrEmpty()) {
                    showShortToast(mContext, "Select address type")
                    return false
                }
                else if (latitude.isNullOrEmpty()||longitude.isNullOrEmpty()||zipcode.isNullOrEmpty()) {
                    showShortToast(mContext,"Enter Valid Location")
                    return false
                }
                else if (state.isNullOrEmpty()||city.isNullOrEmpty()) {
                    showShortToast(mContext,"Choose location again")
                    return false
                }
            }

        }

        return true
    }

}