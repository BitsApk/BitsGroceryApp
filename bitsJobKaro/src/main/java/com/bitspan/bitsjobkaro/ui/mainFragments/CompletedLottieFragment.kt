package com.bitspan.bitsjobkaro.ui.mainFragments

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.databinding.FragmentCompletedLottieBinding

// It needs pageId where to navigate it after end
class CompletedLottieFragment : Fragment() {

    private lateinit var binding: FragmentCompletedLottieBinding
    private val lottieArgs: CompletedLottieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCompletedLottieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pageId = lottieArgs.pageId
        binding.doneLottie.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                findNavController().popBackStack(R.id.completedLottieFragment, true)
                findNavController().navigate(pageId)
            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }
        })
    }

}