package com.bitspan.bitsjobkaro.ui.subFragment.jobInfo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.enums.JobFragInfoEnum
import com.bitspan.bitsjobkaro.data.models.AllJobData
import com.bitspan.bitsjobkaro.databinding.FragmentJobSavedBinding
import com.bitspan.bitsjobkaro.presentation.adapters.AllJobAdapter
import com.bitspan.bitsjobkaro.presentation.adapters.RecJobListAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.AllJobViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JobSavedFragment : Fragment() {


    private lateinit var binding: FragmentJobSavedBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val savedJobViewModel: AllJobViewModel by viewModels()

    private lateinit var savedJobList: ArrayList<AllJobData>
    private lateinit var allJobAdapter: AllJobAdapter
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
        binding = FragmentJobSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (shimmerAndDataFlag) {
            getSavedJob()
            CommonUiFunctions.startShimmer(binding.shimmer, binding.jobSavRecyView)

        } else {

            binding.apply {
                jobSavRecyView.apply {
                    adapter = allJobAdapter
                    visibility = View.VISIBLE
                }

            }
        }

    }

    private fun getSavedJob(){
            viewLifecycleOwner.lifecycleScope.launch {
                savedJobViewModel.getSavedJobs(userId.toInt()).let {
                    if (it.isSuccessful && it.body()!=null){
                        CommonUiFunctions.stopShimmer(binding.shimmer,binding.jobSavRecyView)
                        if (it.body()?.status==200){
                                savedJobList = (it.body()?.data?: listOf()) as ArrayList<AllJobData>
                                val adapter = AllJobAdapter(savedJobList) {}
                                binding.jobSavRecyView.adapter = adapter
                        }else{
                            binding.jobSavRecyView.visibility = View.GONE
                            binding.noData.clNodataFound.visibility = View.VISIBLE
                        }
                    }else{
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            "Something went wrong"
                        ) {
                        }
                        binding.noData.clNodataFound.visibility = View.VISIBLE
                    }
                }
            }

    }

}