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
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils.cartLayoutVisibility
import com.bitspanindia.groceryapp.AppUtils.startShimmer
import com.bitspanindia.groceryapp.AppUtils.stopShimmer
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
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
    private lateinit var dialogHelper: DialogHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressListBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()
        dialogHelper = DialogHelper(mContext, mActivity)

        cartLayoutVisibility(mActivity,View.GONE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAddressList()

        binding.btnAddAddress.setOnClickListener {
            val action = AddressListFragmentDirections.actionGlobalMapFragment("addAddress","0.0","0.0")
            findNavController().navigate(action)
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun getAddressList() {
        binding.noProduct.tvNotFound.text = getString(R.string.one_str,"No Address Found")
        startShimmer(binding.shimmer2,binding.rvAddresses)
        binding.btnAddAddress.visibility = View.GONE
        val getAddressReq = HomeDataReq(userId = Constant.userId)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.getAddressList(getAddressReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        stopShimmer(binding.shimmer2,binding.rvAddresses)
                        binding.btnAddAddress.visibility = View.VISIBLE
                        if (it.body()?.statusCode == 200) {
                            val data = it.body()?.myAddress
                            binding.rvAddresses.adapter = AddressesAdapter(mContext,data?: listOf()){address,clickType->
                                if (clickType=="del") removeAddress(address.id?:"")
                            }

                        } else {
                            binding.noProduct.clNoProduct.visibility = View.VISIBLE
                            binding.rvAddresses.visibility = View.GONE
                        }
                    } else {
                        binding.noProduct.clNoProduct.visibility = View.VISIBLE
                        binding.rvAddresses.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                binding.noProduct.clNoProduct.visibility = View.VISIBLE
                binding.rvAddresses.visibility = View.GONE
            }

        }

    }

    private fun removeAddress(addressId:String) {
        val removeAddress = CommonDataReq(userId = Constant.userId, addressId = addressId)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.removeAddress(removeAddress).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            Toast.makeText(mContext, "Address Deleted Successfully", Toast.LENGTH_SHORT).show()
                            getAddressList()
                        } else {
                            Toast.makeText(mContext, it.body()?.message ?: "Something went wrong", Toast.LENGTH_SHORT).show()
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

}