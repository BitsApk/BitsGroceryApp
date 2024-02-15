package com.bitspanindia.groceryapp.ui.bottomsheets

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitspanindia.groceryapp.AppUtils.toDp
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.databinding.FragmentCartBottomSheetBinding
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CartBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "ModalBottomSheet"
    }
    private lateinit var binding: FragmentCartBottomSheetBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    private val cartVM: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBottomSheetBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            val mBehave = this.behavior
            mBehave.maxHeight = 600.toDp()
//            mBehave.state = BottomSheetBehavior.STATE_HALF_EXPANDED

            Log.d("Rishabh", "Behaviour state ${mBehave.state}, peek height ${mBehave.peekHeight}, max height ${mBehave.maxHeight}," +
                    "draggable ${mBehave.isDraggable}, fit to content ${mBehave.isFitToContents},collapsed ${mBehave.skipCollapsed}, hideable ${mBehave.isHideable}")

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartData = getCartList()
        Log.d("Rishabh", "Cart data: ${cartData}")
        binding.cartRecView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.cartRecView.adapter = ProductsAdapter(cartData, mContext, cartVM.countMap, 1) {prod, action ->

            val cartTotalItem = cartVM.cartTotalItem.value
            when (action) {
                CartAction.Add -> {
                    cartVM.setCartTotal((cartTotalItem ?: 0) + 1)
                    Log.d("Rishabh", "Cart action add clicked")
                    cartVM.addItemToCart(prod)
                }
                CartAction.Minus -> {
                    cartVM.setCartTotal((cartTotalItem ?: 0) - 1)
                    cartVM.decreaseCountOfItem(prod)
                }
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

}