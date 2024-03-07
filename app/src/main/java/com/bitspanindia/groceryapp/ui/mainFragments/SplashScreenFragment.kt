package com.bitspanindia.groceryapp.ui.mainFragments

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.databinding.FragmentSplashScreenBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.AuthViewModel
import com.bitspanindia.groceryapp.storage.SharedPreferenceUtil
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity


    private val authVM: AuthViewModel by viewModels()
    private var isLogin = false
    private var authFound = false
    private val handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var pref: SharedPreferenceUtil



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mContext = requireContext()
        mActivity = requireActivity()
        AppUtils.cartLayoutVisibility(mActivity, View.GONE)

        isLogin = pref.getBoolean(Constant.IS_LOGIN, false)
        Constant.userId = pref.getString(Constant.USER_ID, "-1") ?: "-1"
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        Handler(Looper.getMainLooper()).postDelayed({

        }, 3500)

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

                val direction = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                findNavController().navigate(directions = direction)
//                observeInfo()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

    }




    private fun startTimer() {
//        handler.postDelayed({
//            Log.d("Rishabh", "timer end")
//            authVM.splashUserInfo.removeObservers(viewLifecycleOwner)
//            CommonUiFunctions.showErrorMsgDialog(mContext, "Error in getting authentication credentials, please try again later") {
//                mActivity.finish()
//            }
//        }, 10000)
    }
    fun observeInfo() {
//        authVM.splashUserInfo.observe(viewLifecycleOwner) {
//            if (it) {
//                val direction = if (userLogin) {
//                    if (userType) SplashFragmentDirections.actionSplashFragmentToHomeFragmentSeeker()
//                    else SplashFragmentDirections.actionSplashFragmentToRecruiterHomeFragment()
//                } else if (userId.isNotEmpty()) {
//                    SplashFragmentDirections.actionSplashFragmentToUserSelectFragment()
//                } else SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
//                handler.removeCallbacksAndMessages(null)
//                findNavController().navigate(direction)
//            }
//        }
    }

    private fun getAuthDetails() {
        viewLifecycleOwner.lifecycleScope.launch {

            authVM.getAuthDetails().let {
                if (it.isSuccessful && it.body() != null) {
                    if (it.body()!!.statusCode == 200) {
                        Constant.AAU = it.body()!!.apiAuth!![0].username!!
                        Constant.AAP = it.body()!!.apiAuth!![0].password!!
                        authFound = true
                        authVM.setSplashUseInfo(authFound)


                    } else {
                        authFound = false
                    }
                } else {
                    authFound = false
                }
            }
        }
    }

}