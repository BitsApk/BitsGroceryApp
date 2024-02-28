package com.bitspan.bitsjobkaro.ui.subFragment.jobInfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.models.employee.EmpAppViewedData
import com.bitspan.bitsjobkaro.databinding.FragmentJobViewedByBinding
import com.bitspan.bitsjobkaro.presentation.adapters.EmpAppViewedAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpViewedByViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class JobViewedByFragment : Fragment() {

    private lateinit var binding: FragmentJobViewedByBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    private val viewedByViewModel: EmpViewedByViewModel by viewModels()
    private lateinit var empViewedList: List<EmpAppViewedData>
    private lateinit var adapter: EmpAppViewedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentJobViewedByBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewedJob()

    }

    private fun getViewedJob() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewedByViewModel.getViewedByList(userId.toInt()).let {
                if (it.isSuccessful && it.body()!=null){
                    if (it.body()?.status==200){
                        empViewedList = it.body()?.data?: listOf()
                        adapter = EmpAppViewedAdapter(empViewedList)
                        binding.jobViewedRecyView.adapter = adapter
                    }else{
                        binding.jobViewedRecyView.visibility = View.GONE
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