package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
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
import com.bitspanindia.groceryapp.adapter.CartOutOfStockAdapter
import com.bitspanindia.groceryapp.adapter.CartProductAdapter
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.custom.CartUpdatedProdData
import com.bitspanindia.groceryapp.data.model.request.CartValidateData
import com.bitspanindia.groceryapp.data.model.request.CartValidateReq
import com.bitspanindia.groceryapp.data.model.request.PaymentReq
import com.bitspanindia.groceryapp.data.model.response.CartProdBackendData
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

    private lateinit var cartData: MutableList<ProductData>

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

        cartData = cartManageVM.getCartList()

        val cartValidateDataList = mutableListOf<CartValidateData>()
        for (i in cartData) {
            cartValidateDataList.add(CartValidateData(
                productId = i.id.toInt(),
                qty = i.count,
                sizeId = i.sizeId.toInt()
            ))
        }
        validateCart(CartValidateReq(cart = cartValidateDataList))




        binding.btnPay.setOnClickListener {

            doPayment()

//            val action = CartFragmentDirections.actionCartFragmentToOrderSuccessFragment()
//            findNavController().navigate(action)
        }

        setInstructionData()

    }

    private fun validateCart(cartValidateReq: CartValidateReq) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.validateCart(cartValidateReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            val outOfStockList = mutableListOf<CartUpdatedProdData>()
                            for (i in it.body()!!.list ?: listOf()) {
                                if (i.isOutofstock == 1) {
                                    val indexInCartList = findIndex(i)
                                    val prevPrice = cartData[indexInCartList].discountedPrice
                                    cartData[indexInCartList].let {prod ->
                                        outOfStockList.add(CartUpdatedProdData(
                                            id = prod.id,
                                            image = prod.image,
                                            productName = prod.productName,
                                            rating = prod.rating,
                                            weight  = prod.weight,
                                            discountedPrice = prod.discountedPrice,
                                            stockChange = Pair(prod.count, i.qty ?: 0)
                                        ))
                                    }
                                    cartData[indexInCartList].apply {
                                        count = i.qty ?: 0
                                        discountedPrice = i.netprice
                                        discount = i.discount.toString()
                                        stock = i.stock
                                    }
                                    cartManageVM.updateProductInCart(cartData[indexInCartList])

                                    if (prevPrice != i.discountedPrice) {
                                        cartData[indexInCartList].priceChange = Pair(prevPrice ?: 0.0, i.discountedPrice ?: 0.00)
                                    }
                                }
                            }

                            if (outOfStockList.size > 0) {
                                binding.exceptionLay.visibility = View.VISIBLE
                                binding.messTxt.text = getString(R.string._d_items_in_your_cart_are_in_stock, outOfStockList.size)
                                binding.defectRecView.adapter = CartOutOfStockAdapter(mContext, outOfStockList)
                            }
                            setCartData()
                        } else {
                            // TODO error dialog
                        }
                    } else {

                        // TODO error dialog
                    }
                }
            } catch (e: Exception) {

                // TODO error dialog
            }
        }
    }

    private fun setCartData() {
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
    }

    private fun findIndex(cart: CartProdBackendData): Int {
        cartData.forEachIndexed { index, prod ->
            if (prod.id == cart.id && prod.sizeId == cart.sizeid) return index
        }
        return -1
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