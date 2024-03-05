package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList
import com.bitspan.bitsjobkaro.data.models.OnBoardData
import com.bitspan.bitsjobkaro.databinding.FragmentOnboardingBinding
import com.bitspan.bitsjobkaro.presentation.adapters.OnBoardAdapter
import java.util.*


class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var btnAnimation: Animation

    lateinit var viewPagerAdapter: OnBoardAdapter
    lateinit var imageList: List<OnBoardData>


    private val timer = Timer()
    var position = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Adding image in list-----------------
        imageList = PreDefinedList.imageList

        viewPagerAdapter = OnBoardAdapter(imageList)
        binding.onboardViewPager.adapter = viewPagerAdapter

        btnAnimation = AnimationUtils.loadAnimation(mContext,R.anim.button_animation)

        binding.indicator.attachToPager(binding.onboardViewPager)


        //signUp button click---------------------
        binding.onboardGetStartedBtn.setOnClickListener {
            val direction = OnBoardingFragmentDirections.actionOnBoardingFragmentToUserSelectFragment()
            findNavController().navigate(direction)
        }


        // skip textView click
        binding.onboardSkip.setOnClickListener{
            val direction = OnBoardingFragmentDirections.actionOnBoardingFragmentToUserSelectFragment()
            findNavController().navigate(direction)
        }


        //next button click
        binding.onboardNextBtn.setOnClickListener{
            position = binding.onboardViewPager.currentItem
            if (position < imageList.size) {
                position++
                binding.onboardViewPager.currentItem = position
            }
            if (position == imageList.size - 1) {
                stopBannerSlidShow()
                loadLastScreen()
            }
        }

        startBannerSlideShow()
    }

    //start onBoarding screen automatic
    private fun startBannerSlideShow() {

        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (position == imageList.size - 1) {
                stopBannerSlidShow()
                loadLastScreen()
            }
            binding.onboardViewPager.setCurrentItem(position++, true)
        }
        timer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(update)
                }
            }, Constant.ONBOARD_DELAY_TIME, Constant.ONBOARD_PERIOD_TIME)

    }


    //stop onBoarding screen
    private fun stopBannerSlidShow() {
        timer.cancel()
    }

    private fun loadLastScreen() {
        binding.onboardNextBtn.visibility = View.INVISIBLE
        binding.onboardGetStartedBtn.visibility = View.VISIBLE
        binding.onboardGetStartedBtn.animation = btnAnimation
        binding.onboardSkip.visibility = View.GONE

    }

}