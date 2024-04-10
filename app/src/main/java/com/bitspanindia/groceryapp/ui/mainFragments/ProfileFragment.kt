package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.showLogoutDialog
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.presentation.adapter.ProfileSettingAdapter
import com.bitspanindia.groceryapp.databinding.FragmentProfileBinding
import com.bitspanindia.groceryapp.data.model.ProfileSettingItemModel
import com.bitspanindia.groceryapp.databinding.SuggestProductBottomSheetBinding
import com.bitspanindia.groceryapp.storage.SharedPreferenceUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var mContext:Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var dialogHelper: DialogHelper


    @Inject
    lateinit var pref: SharedPreferenceUtil



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()
        dialogHelper = DialogHelper(mContext,mActivity)

//        binding.clProfile.visibility = if (Constant.userId == "0") View.GONE else View.VISIBLE

        binding.tvUserName.text = Constant.name
        binding.tvUserId.text = Constant.phoneNo
        AppUtils.cartLayoutVisibility(mActivity, View.GONE)
        setSettingItem()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clProfile.setOnClickListener {
            if (Constant.userId=="0") {
                dialogHelper.showErrorMsgDialog("Please login before seeing profile details"){}
            } else {
                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                findNavController().navigate(action)
            }

        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setSettingItem(){
        val settingItemList = listOf(
            ProfileSettingItemModel(
                0,
                "Your Orders",R.drawable.icon_shopping
            ),
            ProfileSettingItemModel(
                1,
                "Addresses",R.drawable.icon_location_svg
            ),
            ProfileSettingItemModel(
                2,
                "Share the app",R.drawable.icon_share
            ),
            ProfileSettingItemModel(
                3,
                "About Us",R.drawable.icon_about
            ),
            ProfileSettingItemModel(
                4,
                "Rate Us",R.drawable.icon_start
            ),
            ProfileSettingItemModel(
                5,
                "Logout",R.drawable.icon_logout
            ),
        )

        binding.rvItems.adapter = ProfileSettingAdapter(settingItemList){pos->
            when(pos){
                0 -> {
                    if (Constant.userId=="0"){
                        dialogHelper.showErrorMsgDialog("Please login before seeing orders"){}
                    }else{
                        val action = ProfileFragmentDirections.actionProfileFragmentToOrderListFragment()
                        findNavController().navigate(action)
                    }

                }
                1 -> {
                    if (Constant.userId=="0"){
                        dialogHelper.showErrorMsgDialog("Please login before seeing your addresses"){}
                    }else{
                        val action = ProfileFragmentDirections.actionProfileFragmentToAddressListFragment()
                        findNavController().navigate(action)
                    }
                }
                5 -> {
                    showLogoutDialog(mContext) {
                        pref.deletePreferences()
                        Constant.userId = ""
                        Constant.name = ""
                        Constant.phoneNo = ""
                        Constant.email = ""
                        val directions = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                        findNavController().navigate(directions)
                    }
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