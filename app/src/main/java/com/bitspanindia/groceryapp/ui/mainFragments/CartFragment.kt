package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.CartProductAdapter
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.request.PaymentReq
import com.bitspanindia.groceryapp.databinding.FragmentCartBinding
import com.bitspanindia.groceryapp.datalist.CustomList
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.CartViewModel
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    private lateinit var mContext:Context
    private lateinit var mActivity:FragmentActivity

    private val cartManageVM: CartManageViewModel by activityViewModels()
    private val cartVM: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.white)
        binding.tvDelCharge.paintFlags = binding.tvDelCharge.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        val cartData = cartManageVM.getCartList()
        Log.d("Rishabh", "Cart data: ${cartData}")
        binding.rvCartItems.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.rvCartItems.adapter = ProductsAdapter(cartData, mContext, cartManageVM.countMap, 1) {prod, action ->

            val cartTotalItem = cartManageVM.cartTotalItem.value
            when (action) {
                CartAction.Add -> {
                    cartManageVM.setCartTotal((cartTotalItem ?: 0) + 1)
                    cartManageVM.addItemToCart(prod)
                }
                CartAction.Minus -> {
                    cartManageVM.setCartTotal((cartTotalItem ?: 0) - 1)
                    cartManageVM.decreaseCountOfItem(prod)
                }
                CartAction.ItemClick -> {
                    val action = CartFragmentDirections.actionGlobalProductDetailsFragment(prod.id)
                    findNavController().navigate(action)
                }
            }
        }


        binding.btnPay.setOnClickListener {

            doPayment()

//            val action = CartFragmentDirections.actionCartFragmentToOrderSuccessFragment()
//            findNavController().navigate(action)
        }

        setInstructionData()

    }

    private fun doPayment() {
        val paymentReq = PaymentReq (
                userId = 2,
                addedByWeb = "56testing.club",
                paymenttype = "online", //online,cod
                totalPayble = 500.0,
                convCharge =  10.0
        )
        viewLifecycleOwner.lifecycleScope.launch {

        }
    }

    private fun setCartProductList() {
        binding.rvCartItems.adapter = CartProductAdapter(CustomList(mContext).dataListProduct2)
    }


    private fun setInstructionData(){
        binding.apply {
            avoidCalling.tvInstruction.text = "Avoid calling"
            avoidCalling.ivDontCall.setImageResource(R.drawable.icon_no_call)
            dnotRingBell.tvInstruction.text = "Don't ring the bell"
            dnotRingBell.ivDontCall.setImageResource(R.drawable.icon_notification_off)
            leaveWithGuard.tvInstruction.text = "Leave with guard"
            leaveWithGuard.ivDontCall.setImageResource(R.drawable.icon_guard)
            leaveAtDoor.tvInstruction.text = "Leave at door"
            leaveAtDoor.ivDontCall.setImageResource(R.drawable.icon_door)
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green_500)
//    }

}