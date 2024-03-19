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
    private var lottieComp = false

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
        Constant.userId = pref.getString(Constant.USER_ID, "0") ?: "0"
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAuthDetails()

        binding.dashLottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {

                if (authFound && isLogin) {
                    navigateToHome()
                } else if (authFound) {
                    navigateToLogin()
                }
                lottieComp = true
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

    }

    private fun navigateToHome() {
        val direction = SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
        findNavController().navigate(directions = direction)
    }

    private fun navigateToLogin() {
        val direction = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
        findNavController().navigate(direction)
    }


    private fun getAuthDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                authVM.getAuthDetails().let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.statusCode == 200) {
                        Constant.AAU = it.body()!!.apiAuth!![0].username!!
                        Constant.AAP = it.body()!!.apiAuth!![0].password!!
                        authFound = true
                        if (lottieComp && isLogin) {
                            navigateToHome()
                        } else if (lottieComp) {
                            navigateToLogin()
                        }
                    } else {
                        authFound = false
                    }
                }
            } catch (e: Exception) {
                authFound = false
                AppUtils.showErrorMsgDialog(
                    mContext,
                    "Authentication not found, sorry for the inconvienence"
                ) {
                    mActivity.finish()
                }
            }
        }
    }
}