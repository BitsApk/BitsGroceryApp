package com.bitspan.bitsjobkaro.ui.mainFragments.recruiter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.data.models.ChangePassReq
import com.bitspan.bitsjobkaro.databinding.FragmentRecruiterChangePassBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecruiterChangePassFragment : Fragment() {


    private lateinit var binding: FragmentRecruiterChangePassBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val otherVM: OtherViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        // Inflate the layout for this fragment
        binding = FragmentRecruiterChangePassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveBtn.setOnClickListener {

            binding.progBar.visibility = View.VISIBLE
            binding.saveBtn.isEnabled = false
            if (checkField()) {
                if (userType) {
                    changeEmpPass()
                } else {
                    changeRecPass()
                }
            } else {
                binding.progBar.visibility = View.GONE
                binding.saveBtn.isEnabled = true
            }
        }

        binding.backImg.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun changeRecPass() {
        val changePassReq = ChangePassReq(
            oldPassword = binding.oldPassEdTxt.editText?.text.toString(),
            newPassword = binding.newPassEdTxt.editText?.text.toString(),
            recId = userId.toInt(),
            empId = null
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.recChangePass(changePassReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.saveBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Password change successfully", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to change password, please try again later") {

                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to change password, please try again later") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.saveBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Unable to change password, please try again later"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun changeEmpPass() {
        val changePassReq = ChangePassReq(
            oldPassword = binding.oldPassEdTxt.editText?.text.toString(),
            newPassword = binding.newPassEdTxt.editText?.text.toString(),
            empId = userId.toInt(),
            recId = null
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.empChangePass(changePassReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.saveBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Password change successfully", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Unable to change password, please try again later") {

                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to change password, please try again later") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.saveBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Unable to change password, please try again later"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun checkField(): Boolean {
        val oldPass = binding.oldPassEdTxt.editText?.text.toString()
        val newPass = binding.newPassEdTxt.editText?.text.toString()
        val check = if (oldPass.isBlank()) {
            resetErrorAndFields(oldPass = "Old password can't be empty")
            false
        } else if (newPass.isBlank() || newPass.length < 6) {
            resetErrorAndFields(newPass = "Password should contain atleast 6 character")
            false
        } else true
        return check
    }

    private fun resetErrorAndFields(
        oldPass: String? = null,
        newPass: String? = null
    ) {
        binding.oldPassEdTxt.editText?.error = oldPass
        binding.newPassEdTxt.error = newPass
    }

}