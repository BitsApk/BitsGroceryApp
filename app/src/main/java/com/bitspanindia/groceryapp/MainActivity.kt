package com.bitspanindia.groceryapp

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.bitspanindia.groceryapp.databinding.ActivityMainBinding
import com.bitspanindia.groceryapp.presentation.viewmodel.CartViewModel
import com.bitspanindia.groceryapp.ui.bottomsheets.CartBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController

    private val cartVM: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navController) as NavHostFragment
        navController = navHostFragment.navController

        val navBuilder = NavOptions.Builder().setEnterAnim(android.R.anim.fade_in)
            .setExitAnim(android.R.anim.fade_out).setPopExitAnim(
                android.R.anim.fade_out
            ).build()

//        val cartBottom = CartBottomSheetFragment()
//        val mBottomSheetBehaviour = (CartBottomSheetFragment().dialog as BottomSheetDialog).behavior
//        Log.d("Rishabh", "sheet behaviour: ${mBottomSheetBehaviour.state}")

        binding.arrowImg.setOnClickListener {

            val modalBottomSheet = CartBottomSheetFragment()
            modalBottomSheet.show(supportFragmentManager, CartBottomSheetFragment.TAG)
//                binding.progBar.visibility = View.VISIBLE
//
//                binding.progBar.visibility = View.GONE
//                binding.modalBottomSheet.visibility = View.VISIBLE
        }

        bindCartTotal()


    }

    private fun bindCartTotal() {
        cartVM.cartTotalItem.observe(this) {
            binding.countTxt.text = getString(R.string.d_items, it)
            if (it == 0) {
                cartVisibility(View.GONE)
            } else if (!binding.cartLay.isVisible) {
                cartVisibility(View.VISIBLE)
            }
        }
    }

    fun cartVisibility(visible: Int) {
        if (visible == View.VISIBLE) {
            val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_cart_bottom_start)
            binding.cartLay.startAnimation(animation)
            binding.cartLay.visibility = visible
        }  else {
            val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_cart_bottom_end)
            binding.cartLay.layoutAnimationListener = object : AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationEnd(p0: Animation?) {
                    binding.cartLay.visibility = visible
                    binding.cartLay.layoutAnimationListener = null
                }

                override fun onAnimationRepeat(p0: Animation?) {
                    TODO("Not yet implemented")
                }
            }
            binding.cartLay.startAnimation(animation)
        }

    }

}