package com.bitspan.bitsjobkaro.ui.mainFragments.employee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.showErrorMsgDialog
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.models.AllJobData
import com.bitspan.bitsjobkaro.data.models.ProfileImageReq
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.employee.EmpSavedBookmarkData
import com.bitspan.bitsjobkaro.data.models.employee.SaveUnsaveJobReq
import com.bitspan.bitsjobkaro.databinding.FragmentHomeSeekerBinding
import com.bitspan.bitsjobkaro.presentation.adapters.AllJobAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.AllJobViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragmentSeeker : Fragment() {


    private lateinit var binding: FragmentHomeSeekerBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val commonViewModel: CommonViewModel by viewModels()
    private val allJobViewModel: AllJobViewModel by viewModels()
    private val otherVM: OtherViewModel by viewModels()
    private lateinit var jobList: List<AllJobData>
    private lateinit var savedBookMarkList: MutableList<EmpSavedBookmarkData>
    private lateinit var adpater: AllJobAdapter
    private var shimmerAndDataFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shimmerAndDataFlag = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireContext()
        mActivity = requireActivity()
        // Inflate the layout for this fragment
        binding = FragmentHomeSeekerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commonViewModel.saveLogin(true)
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("Rishabh", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            val msg = "New token found: " + token
//            Log.d("Rishabh", msg)
//            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
//        })
        CommonUiFunctions.changeStatusBarColor(mActivity, R.color.text_heading)
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.VISIBLE)
        CommonUiFunctions.showHomeNav(mActivity, R.id.recruiterHomeFragment)
//        CommonUiFunctions.askNotifPermission(mActivity)

        if (shimmerAndDataFlag) {
            getAllSavedJobs()
            getProfileImage()
            CommonUiFunctions.startShimmer(binding.shimmer, binding.recyclerViewJobs)

        } else {

            binding.apply {
                recyclerViewJobs.apply {
                    adapter = adpater
                    visibility = View.VISIBLE
                }

            }
        }

        binding.homeSearchEdTxt.setOnClickListener {
            val direction = HomeFragmentSeekerDirections.actionHomeFragmentSeekerToSearchFragment()
            findNavController().navigate(direction)
        }


//        binding.secondaryText.setOnClickListener {
//            val direction = HomeFragmentSeekerDirections.actionHomeFragmentSeekerToRecruiterHomeFragment()
//            findNavController().navigate(direction)
//        }
//
//        binding.profileImage.setOnClickListener {
//            val direction = HomeFragmentSeekerDirections.actionHomeFragmentSeekerToRecruiterProfileFragment()
//            findNavController().navigate(direction)
//        }
    }

    private fun getProfileImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.getEmpProfileImage(ProfileImageReq(employeeId = userId.toInt(), recId = null)).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.status == 200) {
                        Glide.with(this@HomeFragmentSeeker)
                            .load(it.body()!!.profile)
                            .placeholder(R.drawable.avataar_placeholder) // Placeholder image while loading
                            .centerCrop() // Center-crop the image
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image in both memory and disk
                            .into(binding.profileImage)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun getAllSavedJobs() {
        CommonUiFunctions.startShimmer(binding.shimmer, binding.recyclerViewJobs)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.getEmpSavedBookmarkList(RecEmpIdRequest(empId = userId.toInt())).let {
                    if (it.isSuccessful && it.body() != null){
                        savedBookMarkList = mutableListOf()
                        if (it.body()!!.status == 200) {
                            savedBookMarkList = it.body()!!.data ?: mutableListOf()
                        }
                        getAllJobs()
                    } else {
                        CommonUiFunctions.stopShimmer(binding.shimmer, binding.recyclerViewJobs)
                        showErrorMsgDialog(
                            mContext,
                            "Error in fetching employess detail, please check internet"
                        ) {

                        }
                        noDataVisible(View.VISIBLE)
                    }
                }

            } catch (e: Exception) {
                CommonUiFunctions.stopShimmer(binding.shimmer, binding.recyclerViewJobs)
                showErrorMsgDialog(
                    mContext,
                    "Some technical error in fetching employess detail, please check internet"
                ) {

                }
            }
        }
    }

    private fun getAllJobs() {
        noDataVisible(View.GONE)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                allJobViewModel.getAllJobs().let {
                    CommonUiFunctions.stopShimmer(binding.shimmer, binding.recyclerViewJobs)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            jobList = it.body()!!.data ?: listOf()
                            adpater = AllJobAdapter(jobList, savedBookMarkList) {jobId ->
                                callSaveBook(jobId)
                            }
                            binding.recyclerViewJobs.layoutManager = LinearLayoutManager(mContext)
                            binding.recyclerViewJobs.adapter = adpater
                        } else {
                            binding.recyclerViewJobs.visibility = View.GONE
                            noDataVisible(View.VISIBLE)
                        }
                    } else {
                        showErrorMsgDialog(
                            mContext,
                            "Error in fetching employess detail, please check internet"
                        ) {
                        }
                        noDataVisible(View.VISIBLE)
                    }
                }

            } catch (e: Exception) {
                noDataVisible(View.VISIBLE)
                showErrorMsgDialog(
                    mContext,
                    "Technical Error in fetching employess detail, please check internet"
                ) {
                }
            }

        }
    }

    private fun callSaveBook(jobId: Int) {
        val saveUnsaveJobReq = SaveUnsaveJobReq(
            empId = userId.toInt(),
            jobId = jobId.toString(),
            server = otherVM.server
        )
        viewLifecycleOwner.lifecycleScope.launch {
            otherVM.saveUnsaveJob(saveUnsaveJobReq).let {
            }
        }
    }
    private fun noDataVisible(visible: Int) {
        binding.imageView8.visibility = visible
        binding.noDataTxt.visibility = visible
    }

}