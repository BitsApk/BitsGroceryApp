package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.isValidEmail
import com.bitspanindia.groceryapp.AppUtils.showShortToast
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.UpdateProfileReq
import com.bitspanindia.groceryapp.databinding.FragmentEditProfileBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var dialogHelper: DialogHelper
    private val pvm: ProfileViewModel by activityViewModels()
    private var req = UpdateProfileReq()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()
        dialogHelper = DialogHelper(mContext, mActivity)

        AppUtils.cartLayoutVisibility(mActivity, View.GONE)


        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfileDetails()

        binding.btnUpdate.setOnClickListener {
            if (validation()) {
                updateProfile()
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun updateProfile() {

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.updateProfile(req).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            showShortToast(
                                mContext,
                                it.body()?.message ?: "Profile Update Successfully"
                            )
                        } else {
                            dialogHelper.showErrorMsgDialog(
                                it.body()?.message?:"Something went wrong"
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

    private fun getProfileDetails() {
        dialogHelper.showProgressDialog()
        val getProfileReq = HomeDataReq(userId = Constant.userId)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.getProfileData(getProfileReq).let {
                    dialogHelper.hideProgressDialog()
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            val data = it.body()
                            binding.apply {
                                etUserName.setText(data?.name ?: "")
                                etPhoneNumber.setText(data?.phone ?: "")
                                etEmail.setText(data?.email ?: "")

                                if (data?.gender == "Male") binding.rbMale.isChecked = true
                                if (data?.gender == "Female") binding.rbFemale.isChecked = true
                            }
                        } else {
                            dialogHelper.showErrorMsgDialog(
                                it.body()?.message?:"Something went wrong"
                            ) {findNavController().popBackStack()}
                        }
                    } else {
                        dialogHelper.showErrorMsgDialog(
                            "Something went wrong"
                        ) {findNavController().popBackStack()}
                    }
                }
            } catch (e: Exception) {
                dialogHelper.showErrorMsgDialog(
                    "Something went wrong"
                ) {findNavController().popBackStack()}
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.green_500)
    }


    private fun validation(): Boolean {
        val selectedRadio: RadioButton =
            mActivity.findViewById(binding.radioGroup.checkedRadioButtonId)

        binding.apply {

            req.userId = Constant.userId
            req.gender = selectedRadio.text.toString()
            req.name = etUserName.text.toString()
            req.phone = etPhoneNumber.text.toString()
            req.email = etEmail.text.toString()

            if (req.gender!!.isEmpty()) {
                showShortToast(mContext, "Select Gender")
            } else if (req.name!!.isEmpty()) {
                showShortToast(mContext, "Enter username")
                return false
            } else if (req.phone!!.length < 10) {
                showShortToast(mContext, "Enter valid phone number")
                return false
            } else if (!isValidEmail(req.email!!)) {
                showShortToast(mContext, "Enter valid email")
                return false
            }

        }

        return true
    }

}