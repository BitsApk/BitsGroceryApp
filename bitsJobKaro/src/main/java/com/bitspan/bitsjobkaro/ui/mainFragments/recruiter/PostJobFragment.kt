package com.bitspan.bitsjobkaro.ui.mainFragments.recruiter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.constants.enums.PostJobProgressEnum
import com.bitspan.bitsjobkaro.data.models.recruiter.PostJobBody
import com.bitspan.bitsjobkaro.databinding.FragmentPostJobBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterPostJobViewModel
import com.bitspan.bitsjobkaro.ui.subFragment.postJob.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostJobFragment : Fragment() {

    private lateinit var binding: FragmentPostJobBinding
    private lateinit var mContext: Context
    private lateinit var navController: NavController
    private lateinit var mActivity: FragmentActivity
    private val viewModel: RecruiterPostJobViewModel by activityViewModels()
    private val postArgs: PostJobFragmentArgs by navArgs()
    private lateinit var navHostFragment: NavHostFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentPostJobBinding.inflate(inflater, container, false)
        navHostFragment =
            childFragmentManager.findFragmentById(R.id.postJobNavFragment) as NavHostFragment
        navController = navHostFragment.navController
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postJobTxt.text = if (postArgs.fromProfile) getString(R.string.post_your_job_here) else getString(R.string.first_post)
        binding.postNextTxt.setOnClickListener {
            val currFragment = navController.currentDestination?.id

            val direction = when(currFragment) {
                R.id.postJobFirstFragment -> {
                    val frag = navHostFragment.childFragmentManager.fragments[0] as PostJobFirstFragment
                    if (frag.checkField()) PostJobFirstFragmentDirections.actionPostJobFirstFragmentToPostJobSecFragment()
                    else null
                }
                else -> {
                    val frag = navHostFragment.childFragmentManager.fragments[0] as PostJobSecFragment
                    if (frag.checkField()) {
                        binding.postJobBtn.animation = AnimationUtils.loadAnimation(mContext, R.anim.button_animation)
                        PostJobSecFragmentDirections.actionPostJobSecFragmentToPostJobThirdFragment()
                    } else null
                }
            }
            if (direction != null) {
                navController.navigate(direction)
            }
        }

        binding.postJobBtn.setOnClickListener {
            val frag = navHostFragment.childFragmentManager.fragments[0] as PostJobThirdFragment
            if (frag.checkField()) postJob(viewModel.postJobBody)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postJobFirstFragment -> {
                    setBtnVisibility()
                    setProgBar(PostJobProgressEnum.START.value)
                }
                R.id.postJobSecFragment -> {
                    setBtnVisibility()
                    setProgBar(PostJobProgressEnum.HALF_COMPLETED.value, R.drawable.icon_post_checkpoint)
                }
                else -> {
                    setBtnVisibility(View.INVISIBLE, View.VISIBLE)
                    setProgBar(PostJobProgressEnum.COMPLETED.value, R.drawable.icon_post_checkpoint, R.drawable.icon_post_checkpoint)
                }
            }
        }
    }

    private fun setBtnVisibility(nextVisible: Int = View.VISIBLE, postVisible: Int = View.INVISIBLE) {
        binding.postNextTxt.visibility = nextVisible
        binding.postJobBtn.visibility = postVisible
    }

    private fun setProgBar(lenCovered: Int, secImg: Int = R.drawable.ic_post_uncheck, thirdImg: Int = R.drawable.ic_post_uncheck) {
        binding.apply {
            postJobProgress.progress = lenCovered
            postProgSecImg.setImageResource(secImg)
            postProgThirdImg.setImageResource(thirdImg)
        }
    }

    private fun postJob(postJobBody: PostJobBody) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postJob(postJobBody).let {
                if (it.isSuccessful) {
                    Toast.makeText(mContext, it.body()?.message, Toast.LENGTH_SHORT).show()
                    if (postArgs.fromProfile) {
                        Log.d(LOG_TAG, "Post job from Profile")
                        findNavController().popBackStack(R.id.recruiterHomeFragment, true)
                        findNavController().navigate(R.id.recruiterHomeFragment)
                    } else {
                            findNavController().popBackStack(R.id.postJobFragment, true)
                            findNavController().navigate(R.id.recruiterHomeFragment)

                    }
                } else {
                    Toast.makeText(mContext, it.body()?.status.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateAfterPost() {
        findNavController().popBackStack(R.id.postJobFragment
            , true)
    // if (recFirstRegister)
//        else findNavController().navigate(R.id.recruiterProfileFragment)
    }


}