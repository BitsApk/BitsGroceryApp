package com.bitspan.bitsjobkaro.ui.mainFragments.employee

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.models.NewPDetail
import com.bitspan.bitsjobkaro.data.models.ProfileImageReq
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.employee.EmpBasicData
import com.bitspan.bitsjobkaro.databinding.FragmentProfileBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpProffessionalViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import com.bitspan.bitsjobkaro.ui.DialogHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val commonViewModel: CommonViewModel by viewModels()
    private val empProffViewModel: EmpProffessionalViewModel by activityViewModels()
    private val otherVM: OtherViewModel by viewModels()
    private var profImageUrl: String? = null
    private var resumeUrl: String? = null
    private var formId: String? = "-1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.VISIBLE)
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            profileCareerPref.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_careerPreferencesFragment) }
            profProffEditImg.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_editEmpProffDetailFragment) }
            eProfDownResTxt.setOnClickListener { navigateToDownload() }
            profBasicEditImg.setOnClickListener {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToEditEmpBasicFragment(formId!!.toInt())
                findNavController().navigate(action)
            }
            rProfMainImg.setOnClickListener { navigateToUploadImage() }
            profRateUsText.setOnClickListener { openPlayStoreForRating() }


            profHelpDeskTxt.setOnClickListener {
                val dialog = DialogHelper(mContext)
                dialog.showDifferentStoreAlert()
            }
        }

        binding.eProfChangePassTxt.setOnClickListener {
            val direction = ProfileFragmentDirections.actionProfileFragmentToRecruiterChangePassFragment()
            findNavController().navigate(direction)
        }


        binding.profLogoutText.setOnClickListener {
            Constant.userLogin = false
            commonViewModel.saveLogin(false)
            commonViewModel.getLogin().observe(viewLifecycleOwner) {
                if (!it) {
                    findNavController().popBackStack(R.id.homeFragmentSeeker, true)
                    findNavController().navigate(R.id.userSelectFragment)
                }
            }
        }

        getEmpProffData(userId.toInt())

        getBasicData(userId.toInt())
        getDocs()

        getChatAppliedCount()
    }

    fun openPlayStoreForRating() {
        try {
            // Replace with your app's package name
            val packageName = "com.bitspan.bitsjobkaro"
//            val packageName = context?.packageName
            val uri = Uri.parse("market://details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)

            // Ensure that the Play Store app is available
            intent.setPackage("com.android.vending")

            mContext.startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            // If the Play Store app is not available, open the Play Store website
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.bitspan.bitsjobkaro")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mContext.startActivity(intent)
        }
    }



    private fun getDocs() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.getEmpProfileImage(ProfileImageReq(employeeId = userId.toInt(), recId = null)).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.status == 200) {
                        resumeUrl = it.body()!!.resume
                        profImageUrl = it.body()!!.profile
                        Glide.with(this@ProfileFragment)
                            .load(profImageUrl)
                            .placeholder(R.drawable.avataar_placeholder) // Placeholder image while loading
                            .centerCrop() // Center-crop the image
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image in both memory and disk
                            .into(binding.rProfMainImg)
                    }
                }
            } catch (_: Exception) {

            }
        }
    }

    private fun navigateToDownload() {
        val file = resumeUrl?.let { File(it) }
        if (file?.name == null) {
            Toast.makeText(mContext, "Resume not uploaded, please upload them first", Toast.LENGTH_SHORT).show()
        } else {
            val direction = ProfileFragmentDirections.actionProfileFragmentToDownloadFragment(
                docName = file.name,
                docUrl = resumeUrl ?: ""
            )
            findNavController().navigate(direction)
        }
    }

    private fun getEmpProffData(userId: Int) {
        startProfileShimmer()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                empProffViewModel.getEmpProffData(userId).let {
                    if (it.isSuccessful&&it.body()!=null) {
                        stopProfileShimmer()
                        if (it.body()?.status == 200) {
                            val proffDetail = it.body()?.data?.newPDetails ?: listOf()
                            if (proffDetail.isNotEmpty()) {
                                setProffData(proffDetail[0])
                                empProffViewModel.professDetails = proffDetail[0]
                            } else {}
                        } else {
                            Log.d("TAG", "Unable to fetch details")
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching professional detail, please try again later") {
//                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                CommonUiFunctions.showErrorMsgDialog(mContext, "Technical Error in fetching professional detail, please try again later") {
//                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun getBasicData(userId: Int) {
        startProfileShimmer()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                empProffViewModel.getEmpBasicData(userId).let {
                    if (it.isSuccessful && it.body() != null) {
                        stopProfileShimmer()
                        if (it.body()?.status == 200) {
                            val data = it.body()?.data?.get(0)
                            formId = data?.formId
                            empProffViewModel.basicDetails = data ?: EmpBasicData()
                            setBasicData(data)
                        } else {
                            Toast.makeText(
                                mContext,
                                "Unable to fetch basic details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching basic details, please try again later") {
//                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching basic details, please try again later") {
//                    findNavController().popBackStack()
                }
            }

        }
    }

    private fun getChatAppliedCount() {
        startProfileShimmer()
        viewLifecycleOwner.lifecycleScope.launch {
            try {

                empProffViewModel.getChatAppliedCount(RecEmpIdRequest(empId = userId.toInt())).let {
                    if (it.isSuccessful && it.body() != null) {
                        stopProfileShimmer()
                        if (it.body()?.status == 200) {
                            binding.eProfAppCountTxt.text = it.body()?.totalJobApplied.toString()
                            binding.tvChatCount.text = it.body()?.totalJobApplied.toString()
                        } else {
                            Toast.makeText(
                                mContext,
                                "Unable to fetch details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching profile count, please try again later") {
//                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                CommonUiFunctions.showErrorMsgDialog(mContext, "Error in fetching profile count, please try again later") {
//                    findNavController().popBackStack()
                }
            }

        }
    }


    private fun navigateToUploadImage() {
        val direction =
            ProfileFragmentDirections.actionProfileFragmentToUploadImageFragment(profImageUrl ?: "")
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        findNavController().navigate(direction)
    }


    private fun stopProfileShimmer() {
        binding.apply {
            profileShimmer.stopShimmer()
            profileShimmer.visibility = View.GONE
            clData.visibility = View.VISIBLE
        }
    }

    private fun startProfileShimmer() {
        binding.apply {
            profileShimmer.startShimmer()
            profileShimmer.visibility = View.VISIBLE
            clData.visibility = View.GONE
        }
    }

    private fun setBasicData(data: EmpBasicData?) {
        binding.apply {



            eProfContactTxt.text = data?.empContact
            eProfDobTxt.text = data?.empDob
            eProfGenderTxt.text = data?.empGender
            rProfNameTxt.text = data?.empName
            eProfEmailTxt.text = data?.empEmail
            eProfQualTxt.text = data?.empHigQualification
        }
    }

    private fun setProffData(proffDetail: NewPDetail?) {
        binding.apply {

            if (!proffDetail?.currComName.isNullOrEmpty() && !proffDetail?.currComDesig.isNullOrEmpty()) {
                eProfPrevComTxt.text = proffDetail?.currComName
                eProfDesigTxt.text = proffDetail?.currComDesig
                eProfFromDateTxt.text = proffDetail?.currDFrom
                eProfToDateTxt.text = proffDetail?.currDTo
                eProfNoticeTxt.text = proffDetail?.currNotice
            }

        }
    }


}