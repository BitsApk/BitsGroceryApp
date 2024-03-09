package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.databinding.FragmentUserSelectBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel

class UserSelectFragment : Fragment() {

    private lateinit var binding: FragmentUserSelectBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val viewModel: CommonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        // Inflate the layout for this fragment
        binding =  FragmentUserSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUiFunctions.changeStatusBarColor(mActivity, R.color.text_heading)

        binding.userSelectSeekerBtn.setOnClickListener {
            saveUserTypeToDataStore(true)
            navigateToSignInFragment()
        }

        binding.userSelectRecruiterBtn.setOnClickListener {
            saveUserTypeToDataStore(false)
            navigateToSignInFragment()
        }
    }

    private fun saveUserTypeToDataStore(isEmployee: Boolean) {
        Constant.userType = isEmployee
        viewModel.saveUserType(isEmployee)
//        CommonUiFunctions.setUpBottomNavMenu(mActivity, isEmployee)
    }

    private fun navigateToSignInFragment() {
        val direction = UserSelectFragmentDirections.actionUserSelectFragmentToSignInFragment()
        findNavController().navigate(direction)
    }
}