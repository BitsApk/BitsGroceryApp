package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.presentation.adapter.ProfileSettingAdapter
import com.bitspanindia.groceryapp.databinding.FragmentProfileBinding
import com.bitspanindia.groceryapp.data.model.ProfileSettingItemModel
import com.bitspanindia.groceryapp.databinding.LocationEnableBottomSheetBinding
import com.bitspanindia.groceryapp.databinding.SuggestProductBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var mContext:Context
    private lateinit var mActivity: FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()

        AppUtils.cartLayoutVisibility(mActivity, View.GONE)
        setSettingItem()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            findNavController().navigate(action)
        }

    }

    private fun setSettingItem(){
        val settingItemList = listOf<ProfileSettingItemModel>(
            ProfileSettingItemModel(
                "Wallet",R.drawable.icon_wallet
            ),
            ProfileSettingItemModel(
                "Your Orders",R.drawable.icon_shopping
            ),
            ProfileSettingItemModel(
                "Addresses",R.drawable.icon_location_svg
            ),
            ProfileSettingItemModel(
                "Refund",R.drawable.icon_shopping
            ),
            ProfileSettingItemModel(
                "Suggest Product",R.drawable.icon_new
            ),
            ProfileSettingItemModel(
                "Share the app",R.drawable.icon_share
            ),
            ProfileSettingItemModel(
                "About Us",R.drawable.icon_about
            ),
            ProfileSettingItemModel(
                "Rate Us",R.drawable.icon_start
            ),
            ProfileSettingItemModel(
                "Logout",R.drawable.icon_logout
            ),
        )

        binding.rvItems.adapter = ProfileSettingAdapter(settingItemList){pos->
            when(pos){
                1->{
                    val action = ProfileFragmentDirections.actionProfileFragmentToOrderListFragment()
                    findNavController().navigate(action)
                }
                2->{
                    val action = ProfileFragmentDirections.actionProfileFragmentToAddressListFragment()
                    findNavController().navigate(action)
                }
                4->{
                    showSuggestProductBottomSheet()
                }
            }
        }


    }

    private fun showSuggestProductBottomSheet() {
        val dialog = BottomSheetDialog(mContext)
        val bindingDialog = SuggestProductBottomSheetBinding.inflate(layoutInflater)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)

        bindingDialog.btnSend.setOnClickListener {
            dialog.dismiss()
        }

        bindingDialog.ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}