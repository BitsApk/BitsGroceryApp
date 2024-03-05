package com.bitspan.bitsjobkaro.ui.mainFragments.recruiter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.createChip
import com.bitspan.bitsjobkaro.CommonUiFunctions.showErrorMsgDialog
import com.bitspan.bitsjobkaro.CommonUiFunctions.startShimmer
import com.bitspan.bitsjobkaro.CommonUiFunctions.stopShimmer
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.enums.RecFragInfoEnum
import com.bitspan.bitsjobkaro.data.models.ProfileImageReq
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.models.recruiter.ShortListData
import com.bitspan.bitsjobkaro.databinding.FragmentHomeRecruiterBinding
import com.bitspan.bitsjobkaro.presentation.adapters.ChatTypes
import com.bitspan.bitsjobkaro.presentation.adapters.EmpListAdapter
import com.bitspan.bitsjobkaro.presentation.adapters.PostedJobListAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.ChatViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecruiterHomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeRecruiterBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var empListAdapter: EmpListAdapter
    private lateinit var postJobAdapter: PostedJobListAdapter
    private val commonViewModel: CommonViewModel by viewModels()
    private val recruiterViewModel: RecruiterViewModel by viewModels()
    private val recPostViewModel: RecruiterManageViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val otherVM: OtherViewModel by viewModels()
    private lateinit var shortList: MutableList<ShortListData>
    private var shimmerAndDataFlag = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shimmerAndDataFlag = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentHomeRecruiterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commonViewModel.saveLogin(true)
//        commonViewModel.getUserId().observe(viewLifecycleOwner) { userId = it }

//        CommonUiFunctions.askNotifPermission(mActivity)
        Log.d(LOG_TAG, "Home userId: $userId")
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.VISIBLE)
        CommonUiFunctions.showHomeNav(mActivity, R.id.recruiterHomeFragment)
        CommonUiFunctions.changeStatusBarColor(mActivity, R.color.text_heading)
        if (shimmerAndDataFlag) {
            getAllShortlisted()
            getPostedJobs()
            getProfileImage()
        } else {

            binding.apply {
                rHomeJobPostedRecView.apply {
                    adapter = postJobAdapter
                    visibility = View.VISIBLE
                }
                rHomeCandidatesRecView.apply {
                    adapter = empListAdapter
                    visibility = View.VISIBLE
                }
                if (postJobAdapter.itemCount == 0) binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                if (empListAdapter.itemCount == 0) noDataVisible(View.VISIBLE)
            }
        }

        binding.rHomeSearch.setOnClickListener {
            val directions =
                RecruiterHomeFragmentDirections.actionRecruiterHomeFragmentToRecruiterSearchFragment()
            findNavController().navigate(directions)
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }

        binding.clearFilterTxt.setOnClickListener {
            getEmpData(null)
            binding.rHomeCurrJobTxt.text = getString(R.string.employee_data)
            binding.rHomeChipGroup.removeAllViews()
        }
    }

    private fun getProfileImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.getRecProfileImage(ProfileImageReq(employeeId = null, recId = userId.toInt())).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.status == 200) {
                        Glide.with(this@RecruiterHomeFragment)
                            .load(it.body()!!.logo)
                            .placeholder(R.drawable.avataar_placeholder) // Placeholder image while loading
                            .centerCrop() // Center-crop the image
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image in both memory and disk
                            .into(binding.homeProfImg)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun getAllShortlisted() {
        startShimmer(binding.rHomeEmpListShimmer, binding.rHomeCandidatesRecView)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recruiterViewModel.getAllShortlisted(userId.toInt()).let {
                    if (it.isSuccessful && it.body() != null){
                        shortList = mutableListOf()
                        if (it.body()!!.status == 200) {
                            shortList = it.body()!!.shortList!!
                        }
                        getEmpData(null)
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
                showErrorMsgDialog(
                    mContext,
                    "Technical Error in fetching employess detail, please check internet"
                ) {

                }
                noDataVisible(View.VISIBLE)
            }
        }
    }


    private fun setChipData(chipList: List<String>) {
        binding.rHomeChipGroup.removeAllViews()
        binding.rHomeCurrJobTxt.text = getString(R.string.current_job)
        for (text in chipList) {
            binding.rHomeChipGroup.addView(createChip(text, R.color.white, mContext))
        }
    }


    private fun getPostedJobs() {
        startShimmer(binding.rHomePostShimmer, binding.rHomeJobPostedRecView)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recPostViewModel.getPostedJobList(userId.toInt()).let {
                    stopShimmer(binding.rHomePostShimmer, binding.rHomeJobPostedRecView)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            val list = it.body()!!.data ?: listOf()
                            if (list.isEmpty()) {
                                binding.rHomeFirstLayout.visibility = View.GONE
                            } else {
                                postJobAdapter = PostedJobListAdapter(list) { jobId, chipList ->
                                    setChipData(chipList)
                                    getEmpData(jobId)
                                }
                                setPostJobAdapter()
                                binding.noDataLay.clNodataFound.visibility = View.GONE
                            }
                        } else {
                            postJobAdapter = PostedJobListAdapter(listOf()) {jobId, chipList ->  }
                            binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                        }
                    } else {
                        showErrorMsgDialog(
                            mContext, "Some error in getting posted jobs, Check your internet"
                        ) {
                        }
                        binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                    }
                }

            } catch (e: Exception) {
                stopShimmer(binding.rHomePostShimmer, binding.rHomeJobPostedRecView)
                showErrorMsgDialog(
                    mContext, "Some error in getting posted jobs, Check your internet"
                ) {
                }
                binding.noDataLay.clNodataFound.visibility = View.VISIBLE

            }
        }
    }


    private fun getEmpData(jobId: Int?) {
        startShimmer(binding.rHomeEmpListShimmer, binding.rHomeCandidatesRecView)
        noDataVisible(View.GONE)
        if (jobId == null) binding.clearFilterTxt.visibility = View.GONE
        else binding.clearFilterTxt.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recruiterViewModel.getEmpList(jobId).let {
                    if (it.isSuccessful && it.body() != null) {
                        val list = it.body()?.data ?: listOf()
                        if (it.body()!!.status == 200) {
                            empListAdapter = EmpListAdapter(
                                list,
                                RecFragInfoEnum.RecHome,
                                mContext,
                                shortList
                            ) { empId, type, pos ->
                                when (type) {
                                    1 -> callShortListApi(empId)
                                    2 -> reqResume(empId, list, pos)
                                }
                            }
                            setEmpListAdapter()
                            noDataVisible(View.GONE)
                            stopShimmer(binding.rHomeEmpListShimmer, binding.rHomeCandidatesRecView)
                        } else {
                            binding.rHomeCandidatesRecView.visibility = View.GONE
                            noDataVisible(View.VISIBLE)
                            empListAdapter = EmpListAdapter(listOf(), RecFragInfoEnum.RecHome, mContext, shortList) {
                                    empId, type, pos ->
                            }
                            stopShimmer(binding.rHomeEmpListShimmer, null)
                        }
                    } else {
                        showErrorMsgDialog(
                            mContext, "Some error in getting employee data, Check your internet"
                        ) {
                        }
                        stopShimmer(binding.rHomeEmpListShimmer, null)
                    }
                }

            } catch (e: Exception) {
                noDataVisible(View.VISIBLE)
                showErrorMsgDialog(
                    mContext, "Some technical error in getting employee data, Check your internet"
                ) {
                }
                stopShimmer(binding.rHomeEmpListShimmer, null)
            }
        }
    }

    private fun reqResume(empId: Int, list: List<RecruiterEmpData>, pos: Int) {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                chatViewModel.sendMess(
                    empId = empId,
                    mess = ChatTypes.recReqRes,
                    recId = userId.toInt(),
                    senderType = "recruiter"
                ).let {
                    list[pos].showProg = false
                    empListAdapter.notifyItemChanged(pos)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == "200") {
                            Toast.makeText(
                                mContext,
                                "Resume requested",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        showErrorMsgDialog(
                            mContext, "Some technical error in requesting resume"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            showErrorMsgDialog(
                mContext,
                "Some error in requesting resume, Check your internet"
            ) {
                findNavController().popBackStack()
            }
        }

    }


    fun callShortListApi(empId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recPostViewModel.callShortListApi(empId, userId.toInt())
            } catch (_: Exception) {}
        }
    }

    private fun noDataVisible(visible: Int) {
        binding.imageView8.visibility = visible
        binding.noDataTxt.visibility = visible
    }

    private fun setEmpListAdapter() {
        binding.rHomeCandidatesRecView.apply {
            adapter = empListAdapter
            shimmerAndDataFlag = false
        }
    }

    private fun setPostJobAdapter() {
        binding.rHomeJobPostedRecView.adapter = postJobAdapter
    }



}