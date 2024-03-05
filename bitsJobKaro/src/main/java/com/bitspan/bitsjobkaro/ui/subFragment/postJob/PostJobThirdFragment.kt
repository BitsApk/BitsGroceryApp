package com.bitspan.bitsjobkaro.ui.subFragment.postJob

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.models.DistrictData
import com.bitspan.bitsjobkaro.databinding.FragmentPostJobThirdBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterPostJobViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostJobThirdFragment : Fragment() {

    private lateinit var binding: FragmentPostJobThirdBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val viewModel: RecruiterPostJobViewModel by activityViewModels()
    private val recViewModel: RecruiterViewModel by viewModels()
    private lateinit var stateIdList: List<String>
    private lateinit var districtIdList: List<String>
    private lateinit var districtList: List<DistrictData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentPostJobThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, viewModel.postJobBody.toString())
        getStateData()
        getDistrictData()

        binding.rPostArrowImg.setOnClickListener {
            val visible = binding.rPostAddLayout.visibility
            binding.rPostAddLayout.visibility = if (visible == View.GONE) {
                binding.rPostArrowImg.setImageResource(R.drawable.icon_up_arrow)
                binding.rPostAddLayout.animation =
                    AnimationUtils.loadAnimation(context, R.anim.layout_motion_down)
                View.VISIBLE
            } else {
                binding.rPostArrowImg.setImageResource(R.drawable.icon_down_arrow)
                View.GONE
            }
        }

        (binding.rPostStateEdTxt.editText as? AutoCompleteTextView)?.setOnItemClickListener { p0, p1, p2, p3 ->
            getDistrictListWithId(stateIdList[p2])
            binding.rPostDistEdTxt.editText?.setText("")
            viewModel.postJobBody.state = stateIdList[p2]
        }

        (binding.rPostDistEdTxt.editText as? AutoCompleteTextView)?.setOnItemClickListener { p0, p1, p2, p3 ->
            viewModel.postJobBody.district = districtIdList[p2]
        }
    }

    private fun getStateData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val list = recViewModel.getStatesPair()
            stateIdList = list.second
            setStateAdapter(list.first)
            Log.d(LOG_TAG, "Error in fetching state list")
        }
    }

    private fun setStateAdapter(list: List<String>) {
        val stateAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, list)
        (binding.rPostStateEdTxt.editText as? AutoCompleteTextView)?.setAdapter(stateAdapter)
    }

    private fun getDistrictListWithId(stateId: String) {
        val distList = recViewModel.getDistrictWithId(stateId, districtList)
        setDistrictAdapter(distList.first)
        districtIdList = distList.second
    }

    private fun getDistrictData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val list = recViewModel.getDistrict()
            districtList = list

        }
    }

    private fun setDistrictAdapter(list: List<String>) {
        val districtAdapter = ArrayAdapter(mContext, R.layout.item_drop_down, list)
        (binding.rPostDistEdTxt.editText as? AutoCompleteTextView)?.setAdapter(districtAdapter)
    }


    fun checkField(): Boolean {
        val check = if (binding.postJobDesEdTxt.editText?.text?.isBlank() == true) {
            binding.postJobDesEdTxt.error = "Job description can't be empty"
            false
        } else true
        viewModel.postJobBody.apply {
            adress = binding.rPostAddressEdTxt.editText?.text.toString()
            city = binding.rPostCityEdTxt.editText?.text.toString()
            zip = binding.rEditZipPostalEdTxt.editText?.text.toString()
        }
        viewModel.postJobBody.jobDescription = binding.postJobDesEdTxt.editText?.text.toString()
        viewModel.postJobBody.recId = userId
        return check
    }

}