package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.models.ChangePassReq
import com.bitspan.bitsjobkaro.data.models.ForgotPassReq
import com.bitspan.bitsjobkaro.databinding.FragmentForgotPassBinding
import com.bitspan.bitsjobkaro.databinding.FragmentOtpBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ForgotPassFragment : Fragment() {

    private lateinit var binding: FragmentForgotPassBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val otherVM: OtherViewModel by viewModels()
    private val forgotArgs: ForgotPassFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentForgotPassBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.saveBtn.setOnClickListener {
            binding.progBar.visibility = View.VISIBLE
            binding.saveBtn.isEnabled = false
            if (checkField()) {
                changePass()
            } else {
                binding.progBar.visibility = View.GONE
                binding.saveBtn.isEnabled = true
            }
        }
    }

    private fun changePass() {
        val changePassReq = ForgotPassReq(
            number = forgotArgs.number,
            password = binding.newPassEdTxt.editText?.text.toString(),
            confirmpss = binding.confirmPassEdTxt.editText?.text.toString(),
            server = otherVM.server
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.setNewForgotPass(changePassReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.saveBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Password set successfully", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to set password, please try again later") {
                                findNavController().popBackStack()
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to set password, please try again later") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.saveBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Unable to set password, please try again later"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun checkField(): Boolean {
        val newPass = binding.newPassEdTxt.editText?.text.toString()
        val confirmPass = binding.confirmPassEdTxt.editText?.text.toString()
        val check = if (newPass.isBlank() || newPass.length < 6) {
            resetErrorAndFields(newPass = "Password should contain atleast 6 character")
            false
        } else if (newPass != confirmPass) {
            resetErrorAndFields(confirmPass = "Confirm password didn't match your new password")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(
        newPass: String? = null,
        confirmPass: String? = null
    ) {
        binding.newPassEdTxt.error = newPass
        binding.confirmPassEdTxt.error = confirmPass
    }


}