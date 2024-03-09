package com.bitspan.bitsjobkaro.ui.mainFragments.recruiter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.recruiter.RecProfileData
import com.bitspan.bitsjobkaro.databinding.FragmentRecruiterProfileBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterNewViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import com.bitspan.bitsjobkaro.ui.DialogHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecruiterProfileFragment : Fragment() {

    private lateinit var binding: FragmentRecruiterProfileBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var recProfileData: RecProfileData
    private val commonViewModel: CommonViewModel by viewModels()
    private val recViewModel: RecruiterViewModel by viewModels()
    private val recNewVM: RecruiterNewViewModel by activityViewModels()
    private var profImageUrl: String? = null
    private var profDocUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentRecruiterProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUiFunctions.apply {
            bottomNavBarVisibility(mActivity, View.VISIBLE)
            changeStatusBarColor(mActivity, R.color.text_heading)
        }

        getProfData()
        getCountData()

        binding.profLogoutText.setOnClickListener {
            Constant.userLogin = false
            commonViewModel.saveLogin(false)
            commonViewModel.getLogin().observe(viewLifecycleOwner) {
                if (!it) {
                    findNavController().popBackStack(R.id.recruiterHomeFragment, true)
                    findNavController().navigate(R.id.userSelectFragment)
                }
            }
        }

        binding.profHelpDeskTxt.setOnClickListener {
            val dialog = DialogHelper(mContext)
            dialog.showDifferentStoreAlert()
        }

        binding.apply {
            rProfManageJobTxt.setOnClickListener { navigateToManageJob() }
            rProfPostJobTxt.setOnClickListener { navigateToPostJob() }
            rProfBasicEditImg.setOnClickListener { navigateToRecEdit() }
            rProfDesEditImg.setOnClickListener { navigateToEditDesc() }
            rProfEditImg.setOnClickListener { navigateToEditCompInfo() }
            profLocatEditImg.setOnClickListener { navigateToEditCompLocation() }
            rProfMainImg.setOnClickListener { navigateToUploadImage() }
            rProfDownDocTxt.setOnClickListener { navigateToDownload() }
            profRateUsText.setOnClickListener { openPlayStoreForRating() }
        }


        binding.rProfChangePassTxt.setOnClickListener {
            val direction = RecruiterProfileFragmentDirections.actionRecruiterProfileFragmentToRecruiterChangePassFragment()
            findNavController().navigate(direction)
        }
    }

    private fun getCountData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recViewModel.getRecProfileCount(RecEmpIdRequest(recId = userId.toInt())).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            binding.rProfSavCountTxt.text = it.body()!!.totalSaveJob
                            binding.rProfJobCountTxt.text = it.body()!!.totalPosted
                        }
                    } else {
                        // Nothing to do just show 0 by default
                    }
                }
            } catch (e: Exception) {
                // Nothing to do just show 0 by default
            }
        }
    }


    private fun stopShimmer() {
        binding.apply {
            profShimRecTop.stopShimmer()
            profShimCards.stopShimmer()
            profShimTop.visibility = View.GONE
            profShimCardLay.visibility = View.GONE

            rProfFirstLayout.visibility = View.VISIBLE
            rProfComBasicLayout.visibility = View.VISIBLE
            rJobDescLayout.visibility = View.VISIBLE
            rProfComLayout.visibility = View.VISIBLE
            rProfComAddressLayout.visibility = View.VISIBLE
            rProfOthInfoLayout.visibility = View.VISIBLE
        }
    }

    fun openPlayStoreForRating() {
        try {
            // Replace with your app's package name
            val packageName = "com.bitspan.bitsjobkaro"

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


    private fun startShimmer() {
        binding.apply {
            profShimRecTop.startShimmer()
            profShimCards.startShimmer()
            profShimTop.visibility = View.VISIBLE
            profShimCardLay.visibility = View.VISIBLE

            rProfFirstLayout.visibility = View.INVISIBLE
            rProfComBasicLayout.visibility = View.INVISIBLE
            rJobDescLayout.visibility = View.INVISIBLE
            rProfComLayout.visibility = View.INVISIBLE
            rProfComAddressLayout.visibility = View.INVISIBLE
            rProfOthInfoLayout.visibility = View.INVISIBLE
        }
    }


    private fun getProfData() {
        startShimmer()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recViewModel.getRecProfileData(userId.toInt()).let {
                    stopShimmer()
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            recProfileData = it.body()!!.data?.get((it.body()!!.data?.size ?: 1) - 1) ?: RecProfileData()
                            profImageUrl = "${it.body()!!.imgPath}${recProfileData.cLogo}"
                            profDocUrl = "${it.body()!!.imgPath}${recProfileData.documents}"
                            setProfileData()
                            setProfileAboutCompanyData()
                        } else {
                            Toast.makeText(mContext, it.body()!!.mess ?: "Profile data not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Some error in fetching profile data, Please try again later") {}
                    }
                }
            } catch (e: Exception) {
                stopShimmer()
                CommonUiFunctions.showErrorMsgDialog(mContext, "Some technical error in fetching profile data, Please try again later") {}
            }
        }
    }

    private fun setProfileAboutCompanyData() {
        recNewVM.postAboutCompanyData.apply {
            recId = userId
            formId = recProfileData.id

            rName = recProfileData.rName
            rMobile = recProfileData.rMobile
            rDesig = recProfileData.rDesig

            name = recProfileData.name
            email = recProfileData.email
            number = recProfileData.number

            cDesc = recProfileData.cDesc

            comType = recProfileData.comType
            nEmp = recProfileData.nEmp
            cGst = recProfileData.cGst
            link = recProfileData.link

            documents11 = recProfileData.documents
            cLogo11 = recProfileData.cLogo
            docType = recProfileData.docType

            adress = recProfileData.adress
            district = recProfileData.district
            city = recProfileData.city
            state = recProfileData.state
            zip = recProfileData.zip
        }

    }

    private fun setProfileData() {

        binding.apply {

            Glide.with(this@RecruiterProfileFragment)
                .load(profImageUrl)
                .placeholder(R.drawable.avataar_placeholder) // Placeholder image while loading
                .centerCrop() // Center-crop the image
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image in both memory and disk
                .into(binding.rProfMainImg)


            rProfNameTxt.text = recProfileData.rName
            rProfRecNum.text = recProfileData.rMobile
            rProfRecDesignTxt.text = recProfileData.rDesig

            rProfJobDescTxt.text = recProfileData.cDesc

            rProfCompNameTxt.text = recProfileData.name
            rProfIndTypeTxt.text = CommonDataFunctions.getIndusType(recProfileData.comType)
            rProfEmailTxt.text = recProfileData.email
            rProfConNumTxt.text = recProfileData.number
            rProfComGSTNumTxt.text = if (recProfileData.cGst == "") getString(R.string.na) else recProfileData.cGst
            rProfNumEmpTxt.text = recProfileData.nEmp
            rProfWebSiteTxt.text = if (recProfileData.link == "") getString(R.string.na) else recProfileData.link

            rProfAddTxt.text = recProfileData.adress
            rProfDistTxt.text = CommonUiFunctions.getSingleDistrict(recProfileData.district ?: "")
            rProfCityTxt.text = recProfileData.city
            rProfStateTxt.text = CommonUiFunctions.getSingleState(recProfileData.state ?: "")
            rProfZipTxt.text = recProfileData.zip
        }
    }



    private fun navigateToDownload() {
        if (recProfileData.documents == null) {
            Toast.makeText(mContext, "Documents not uploaded, please upload them first", Toast.LENGTH_SHORT).show()
        } else {
            val direction = RecruiterProfileFragmentDirections.actionRecruiterProfileFragmentToDownloadFragment(
                docName = recProfileData.documents ?: "",
                docUrl = profDocUrl ?: ""
            )
            findNavController().navigate(direction)
        }
    }

    private fun navigateToUploadImage() {
        val direction = RecruiterProfileFragmentDirections.actionRecruiterProfileFragmentToUploadImageFragment(profImageUrl ?: "")
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        findNavController().navigate(direction)
    }


    private fun navigateToManageJob() {
        val bundle = Bundle()
        bundle.putBoolean("profile", true)
        findNavController().navigate(R.id.recruiterManageJobFragment, bundle)
    }

    private fun navigateToRecEdit() {
        val direction = RecruiterProfileFragmentDirections.actionRecruiterProfileFragmentToEditRecDetailFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToEditDesc() {
        val direction = RecruiterProfileFragmentDirections.actionRecruiterProfileFragmentToEditCompDescFragment(fromProfile = true)
        findNavController().navigate(direction)
    }

    private fun navigateToEditCompLocation() {
        val direction = RecruiterProfileFragmentDirections.actionRecruiterProfileFragmentToEditCompLocationFragment(fromProfile = true)
        findNavController().navigate(direction)
    }

    private fun navigateToEditCompInfo() {
        val direction = RecruiterProfileFragmentDirections.actionRecruiterProfileFragmentToEditCompanyInfoFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToPostJob() {
        val directions = RecruiterProfileFragmentDirections.actionRecruiterProfileFragmentToPostJobFragment(fromProfile = true)
        findNavController().navigate(directions)
    }


}