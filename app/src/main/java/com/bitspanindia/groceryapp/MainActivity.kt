package com.bitspanindia.groceryapp

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.databinding.ActivityMainBinding
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
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



        val mBottomSheetBehaviour = BottomSheetBehavior.from(binding.modalBottomSheet)
        Log.d("Rishabh", "sheet behaviour: ${mBottomSheetBehaviour.state}")

        binding.arrowImg.setOnClickListener {

            if (mBottomSheetBehaviour.state != BottomSheetBehavior.STATE_EXPANDED) {
                binding.progBar.visibility = View.VISIBLE
                val cartData = getCartList()
                Log.d("Rishabh", "Cart data: ${cartData}")
                binding.cartRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.cartRecView.adapter = ProductsAdapter(cartData, this, cartVM.countMap, 1) {prod, action ->
                    when (action) {
                        CartAction.Add -> {
                            Log.d("Rishabh", "Cart action add clicked")
                            cartVM.addItemToCart(prod)
                        }
                        CartAction.Minus -> cartVM.decreaseCountOfItem(prod)
                    }
                }
                binding.progBar.visibility = View.GONE
                binding.modalBottomSheet.visibility = View.VISIBLE
                mBottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                mBottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED;
            }
        }



    }

    fun getCartList(): MutableList<ProductData> {
        val list = mutableListOf<ProductData>()
        val cart = cartVM.getCart()
        for (item in cart.cartItemsMap) {
            list.addAll(cart.cartItemsMap[item.key] ?: listOf())
        }
        return list
    }

    fun showCart(totalCount: Int) {
        binding.countTxt.text = getString(R.string.d_items, totalCount)
        binding.cartLay.visibility = View.VISIBLE
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_cart_bottom)
        binding.cartLay.startAnimation(animation)

    }
}