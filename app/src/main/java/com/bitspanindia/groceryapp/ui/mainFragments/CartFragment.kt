package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.CartOutOfStockAdapter
import com.bitspanindia.groceryapp.adapter.CartProductAdapter
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.custom.CartUpdatedProdData
import com.bitspanindia.groceryapp.data.model.request.CartValidateData
import com.bitspanindia.groceryapp.data.model.request.CartValidateReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.PaymentReq
import com.bitspanindia.groceryapp.data.model.request.PaymentVerifyReq
import com.bitspanindia.groceryapp.data.model.response.CartProdBackendData
import com.bitspanindia.groceryapp.databinding.FragmentCartBinding
import com.bitspanindia.groceryapp.datalist.CustomList
import com.bitspanindia.groceryapp.presentation.adapter.AddressesAdapter
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.AddressViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.CartViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    private val pvm: ProfileViewModel by activityViewModels()
    private val addViewModel: AddressViewModel by activityViewModels()

    private val cartManageVM: CartManageViewModel by activityViewModels()
    private val cartVM: CartViewModel by viewModels()

    private lateinit var cartData: MutableList<ProductData>

    private lateinit var dialogHelper: DialogHelper

    private var cartTotal = 0.0
    private var delPartCharge = 20.0
    private var platFormCharge = 3.0
    private var tipDeliveryCharge = 0.0

    private lateinit var cartValidateDataList: MutableList<CartValidateData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()

        dialogHelper = DialogHelper(mContext, mActivity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.white)

        getAddressList()

        observeAddress()

        binding.tvDelCharge.paintFlags =
            binding.tvDelCharge.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        cartData = cartManageVM.getCartList()

        cartValidateDataList = mutableListOf()
        for (i in cartData) {
            cartValidateDataList.add(
                CartValidateData(
                    productId = i.id.toInt(),
                    qty = i.count,
                    sizeId = i.sizeId.toInt()
                )
            )
        }
        validateCart(CartValidateReq(cart = cartValidateDataList))




        binding.btnPay.setOnClickListener {

            doPayment()

//            val action = CartFragmentDirections.actionCartFragmentToOrderSuccessFragment()
//            findNavController().navigate(action)
        }

        binding.tvChangeAddress.setOnClickListener {
            showAddressDialog()
        }

        binding.btnAddAddress.setOnClickListener {
            val action = CartFragmentDirections.actionGlobalMapFragment("addAddress","0.0","0.0")
            findNavController().navigate(action)
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
                                    cartData[indexInCartList].let { prod ->
                                        outOfStockList.add(
                                            CartUpdatedProdData(
                                                id = prod.id,
                                                image = prod.image,
                                                productName = prod.productName,
                                                rating = prod.rating,
                                                weight = prod.weight,
                                                discountedPrice = prod.discountedPrice,
                                                stockChange = Pair(prod.count, i.qty ?: 0)
                                            )
                                        )
                                    }
                                    cartData[indexInCartList].apply {
                                        count = i.qty ?: 0
                                        discountedPrice = i.netprice
                                        discount = i.discount.toString()
                                        stock = i.stock
                                    }
                                    cartManageVM.updateProductInCart(cartData[indexInCartList])

                                    if (prevPrice != i.discountedPrice) {
                                        cartData[indexInCartList].priceChange =
                                            Pair(prevPrice ?: 0.0, i.discountedPrice ?: 0.00)
                                    }
                                }
                            }

                            if (outOfStockList.size > 0) {
                                binding.exceptionLay.visibility = View.VISIBLE
                                binding.messTxt.text = getString(
                                    R.string._d_items_in_your_cart_are_in_stock,
                                    outOfStockList.size
                                )
                                binding.defectRecView.adapter =
                                    CartOutOfStockAdapter(mContext, outOfStockList)
                            }
                            setCartData()
                        } else {
                            // TODO error dialog
                            Toast.makeText(mContext, "Some error: 3 ", Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        Toast.makeText(mContext, "Some error: ", Toast.LENGTH_SHORT).show()
                        // TODO error dialog
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(mContext, "Some error: 2", Toast.LENGTH_SHORT).show()

                // TODO error dialog
            }
        }
    }

    private fun setCartData() {

        cartTotal = 0.0
        for (prod in cartData) {
            cartTotal += (prod.discountedPrice ?: 0.0) * prod.count
        }
        cartManageVM.setCartTotalPrice(cartTotal)
        bindTotalPriceObserver()


        binding.rvCartItems.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.rvCartItems.adapter =
            ProductsAdapter(cartData, mContext, cartManageVM.countMap, 1) { prod, action ->

                val cartTotalItem = cartManageVM.cartTotalItem.value
                when (action) {
                    CartAction.Add -> {
                        cartManageVM.setCartTotal((cartTotalItem ?: 0) + 1)

                        cartManageVM.updateToTotalPrice(prod.discountedPrice ?: 0.0)
                        cartManageVM.addItemToCart(prod)
                    }

                    CartAction.Minus -> {
                        cartManageVM.setCartTotal((cartTotalItem ?: 0) - 1)
                        cartManageVM.updateToTotalPrice(-(prod.discountedPrice ?: 0.0))
                        cartManageVM.decreaseCountOfItem(prod)
                    }

                    CartAction.ItemClick -> {
                        val direction =
                            CartFragmentDirections.actionGlobalProductDetailsFragment(prod.id)
                        findNavController().navigate(direction)
                    }
                }
            }
    }

    private fun bindTotalPriceObserver() {
        cartManageVM.cartTotalPrice.observe(viewLifecycleOwner) {
            binding.tvItemPrice.text = it.toString()
            cartTotal = it
            setGrandTotal()
        }
    }

    private fun setGrandTotal() {
        val total = cartTotal + delPartCharge + tipDeliveryCharge + platFormCharge
        binding.tvGrandTotal.text = getString(R.string.rs_f, total)
        binding.tvTotalPayAmount.text = getString(R.string.rs_f, total)
    }


    private fun findIndex(cart: CartProdBackendData): Int {
        cartData.forEachIndexed { index, prod ->
            if (prod.id == cart.id && prod.sizeId == cart.sizeid) return index
        }
        return -1
    }

    private fun doPayment() {
        val paymentReq = PaymentReq(
            userId = Constant.userId.toInt(),
            addedByWeb = Constant.addedByWeb,
            paymenttype = "online", //online,cod  // Todo change after payment gateway
            totalPayble = binding.grandTotal.text.toString().toDouble(),
            convCharge = cartManageVM.convCharge,        // Todo change after payment gateway,
            cart = cartValidateDataList
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.doPayment(paymentReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            if (it.body()!!.isOutOfStock == 0) {
                                doPaymentVerif(
                                    orderNo = it.body()!!.orderId ?: "",
                                    txnAmount = it.body()!!.tXNAMOUNT ?: 0.0,
                                    transId = it.body()!!.mID ?: ""
                                )
                            }
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

    private fun doPaymentVerif(orderNo: String, txnAmount: Double, transId: String) {
        val payVerifyReq = PaymentVerifyReq(
            userId = Constant.userId.toInt(),
            convCharge = cartManageVM.convCharge,
            orderno = orderNo, //online,cod  // Todo change after payment gateway
            tXNAMOUNT = txnAmount,
            transId = transId

        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.doPaymentVerification(payVerifyReq).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.statusCode == 200) {
                        if (it.body()!!.paymentVerify) {
                            doConfirmOrder()
                        } else {
                            // TODO payment not verify
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

    private fun doConfirmOrder() {

    }

    private fun setCartProductList() {
        binding.rvCartItems.adapter = CartProductAdapter(CustomList(mContext).dataListProduct2)
    }


    private fun setInstructionData() {
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

    private fun getAddressList() {
        val getAddressReq = HomeDataReq(userId = Constant.userId)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.getAddressList(getAddressReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            val addressList = it.body()?.myAddress

                            if (addressList.isNullOrEmpty()){
                                binding.clPay.visibility = View.GONE
                                binding.btnAddAddress.visibility = View.VISIBLE
                            }else{
                                binding.clPay.visibility = View.VISIBLE
                                binding.btnAddAddress.visibility = View.GONE
                                if (addViewModel.myAddress.value?.latitude==null && addViewModel.myAddress.value?.longitude==null){
                                    addViewModel.myAddress.value = addressList[0]
                                    binding.tvDeliveryAddress.text = addressList[0].permanentAdd
                                    binding.tvDeliveringTo.text = getString(R.string.delivering_to,addressList[0].addressName)
                                }

                            }

                        }else{
                            dialogHelper.showErrorMsgDialog(it.body()?.message?:"Something went wrong"){
                                findNavController().popBackStack()
                            }
                        }
                    }
                    else{
                        dialogHelper.showErrorMsgDialog("Something went wrong"){
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                    dialogHelper.showErrorMsgDialog("Something went wrong"){
                        findNavController().popBackStack()
                    }
            }

        }

    }

    private fun showAddressDialog() {
        addViewModel.redirectFrom = "Cart"

        val modalBottomSheet by lazy {
            ChooseLocationBottomSheetFragment()
        }

        modalBottomSheet.show(childFragmentManager, ChooseLocationBottomSheetFragment.TAG)
        modalBottomSheet.isCancelable = true
    }

    private fun observeAddress(){
        addViewModel.myAddress.observe(viewLifecycleOwner){address->
            binding.tvDeliveryAddress.text = address.permanentAdd
            binding.tvDeliveringTo.text = getString(R.string.delivering_to,address.addressName)
        }
    }

}