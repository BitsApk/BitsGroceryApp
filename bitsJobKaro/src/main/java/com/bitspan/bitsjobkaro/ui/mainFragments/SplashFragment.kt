package com.bitspan.bitsjobkaro.ui.mainFragments

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.Constant.userLogin
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.data.models.AuthReq
import com.bitspan.bitsjobkaro.data.models.AuthResponse
import com.bitspan.bitsjobkaro.databinding.FragmentSplashBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.AuthViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val viewModel: CommonViewModel by viewModels()
    private val authVM: AuthViewModel by viewModels()
    private var userTypePresent = false
    private var userLoginPresent = false
    private var userIdPresent = false
    private var authFound = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.apply {
            bottomNavBarVisibility(mActivity, View.GONE)
            changeStatusBarColor(mActivity, R.color.text_heading)
        }
        viewModel.getUserType().observe(viewLifecycleOwner) {
            userType = it
            CommonUiFunctions.setUpBottomNavMenu(mActivity)
            userTypePresent = true
            authVM.setSplashUseInfo(userTypePresent && userLoginPresent && userIdPresent && authFound)
        }
        viewModel.getLogin().observe(viewLifecycleOwner) {
            userLogin = it
            userLoginPresent = true
            authVM.setSplashUseInfo(userTypePresent && userLoginPresent && userIdPresent && authFound)
        }
        viewModel.getUserId().observe(viewLifecycleOwner) {
            userId = it
            userIdPresent = true
            authVM.setSplashUseInfo(userTypePresent && userLoginPresent && userIdPresent && authFound)
        }
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAuthDetails()
        startTimer()


        binding.dashLottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                observeInfo()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })
    }

    private fun startTimer() {
        handler.postDelayed({
            Log.d("Rishabh", "timer end")
            authVM.splashUserInfo.removeObservers(viewLifecycleOwner)
            CommonUiFunctions.showErrorMsgDialog(mContext, "Error in getting authentication credentials, please try again later") {
                mActivity.finish()
            }
        }, 10000)
    }
    fun observeInfo() {
        authVM.splashUserInfo.observe(viewLifecycleOwner) {
            if (it) {
                val direction = if (userLogin) {
                    if (userType) SplashFragmentDirections.actionSplashFragmentToHomeFragmentSeeker()
                    else SplashFragmentDirections.actionSplashFragmentToRecruiterHomeFragment()
                } else if (userId.isNotEmpty()) {
                    SplashFragmentDirections.actionSplashFragmentToUserSelectFragment()
                } else SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
                handler.removeCallbacksAndMessages(null)
                findNavController().navigate(direction)
            }
        }
    }

    private fun getAuthDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            authVM.getAuthDetails(AuthReq(auth = true)).let {
                if (it.isSuccessful && it.body() != null) {
                    val jsonString = decodeHex(it.body()!!)
                    val response = Gson().fromJson(jsonString, AuthResponse::class.java)
                    if (response.statusCode == 200) {
                        Constant.AAU = response.username!!
                        Constant.AAP = response.key!!
                        authFound = true
                        authVM.setSplashUseInfo(userTypePresent && userLoginPresent && userIdPresent && authFound)
                    } else {
                        authFound = false
                    }
                } else {
                    authFound = false
                }
            }
        }
    }


    private fun decodeHex(hex: String): String {
        val result = ByteArray(hex.length / 2)
        for (i in hex.indices step 2) {
            val firstDigit = Character.digit(hex[i], 16)
            val secondDigit = Character.digit(hex[i + 1], 16)
            if (firstDigit == -1 || secondDigit == -1) {
                throw IllegalArgumentException("Invalid hex string")
            }
            val byteValue = (firstDigit shl 4) + secondDigit
            result[i / 2] = byteValue.toByte()
        }
        return String(result, Charsets.UTF_8)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.getUserType().removeObservers(viewLifecycleOwner)
        viewModel.getLogin().removeObservers(viewLifecycleOwner)
        viewModel.getUserId().removeObservers(viewLifecycleOwner)
    }


}
