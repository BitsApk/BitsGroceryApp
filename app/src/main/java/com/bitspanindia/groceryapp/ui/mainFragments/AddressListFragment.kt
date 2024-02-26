package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
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
import com.bitspanindia.groceryapp.AppUtils.cartLayoutVisibility
import com.bitspanindia.groceryapp.AppUtils.startShimmer
import com.bitspanindia.groceryapp.AppUtils.stopShimmer
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.presentation.adapter.AddressesAdapter
import com.bitspanindia.groceryapp.databinding.FragmentAddressListBinding
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressListFragment : Fragment() {
    private lateinit var binding:FragmentAddressListBinding
    private val pvm: ProfileViewModel by activityViewModels()
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressListBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()

        cartLayoutVisibility(mActivity,View.GONE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAddressList()

        binding.btnAddAddress.setOnClickListener {
//            val action = AddressListFragmentDirections.actionAddressListFragmentToAddAddressFragment()
//            findNavController().navigate(action)
            val action = AddressListFragmentDirections.actionGlobalMapFragment("addAddress")
            findNavController().navigate(action)
        }

    }

    private fun getAddressList() {
        startShimmer(binding.shimmer2,binding.rvAddresses)
        binding.btnAddAddress.visibility = View.GONE
        val getAddressReq = HomeDataReq(userId = "1")

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.getAddressList(getAddressReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        stopShimmer(binding.shimmer2,binding.rvAddresses)
                        binding.btnAddAddress.visibility = View.VISIBLE
                        if (it.body()?.statusCode == 200) {
                            val data = it.body()?.myAddress
                            binding.rvAddresses.adapter = AddressesAdapter(mContext,data?: listOf()){addressId->
                                removeAddress(addressId)
                            }

                        } else {
                            Toast.makeText(
                                mContext,
                                it.body()?.message ?: "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun removeAddress(addressId:String) {
        val removeAddress = CommonDataReq(userId = "1", addressId = addressId)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.removeAddress(removeAddress).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            Toast.makeText(mContext, "Address Deleted Successfully", Toast.LENGTH_SHORT).show()
                            getAddressList()
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