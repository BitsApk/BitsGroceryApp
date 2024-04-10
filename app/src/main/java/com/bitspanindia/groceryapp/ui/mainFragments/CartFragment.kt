package com.bitspanindia.groceryapp.ui.mainFragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.getTodayAndTomorrowDates
import com.bitspanindia.groceryapp.AppUtils.getVisibleTimeRanges
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.adapter.CartOutOfStockAdapter
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.custom.CartUpdatedProdData
import com.bitspanindia.groceryapp.data.model.request.AddressData
import com.bitspanindia.groceryapp.data.model.request.Cart
import com.bitspanindia.groceryapp.data.model.request.CartValidateData
import com.bitspanindia.groceryapp.data.model.request.CartValidateReq
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.ConfirmOrderReq
import com.bitspanindia.groceryapp.data.model.request.PaymentReq
import com.bitspanindia.groceryapp.data.model.request.PaymentVerifyReq
import com.bitspanindia.groceryapp.data.model.response.CartProdBackendData
import com.bitspanindia.groceryapp.databinding.FragmentCartBinding
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.AddressViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.CartViewModel
import com.bitspanindia.groceryapp.ui.bottomsheets.CouponBottomSheetFragment
import com.bitspanindia.groceryapp.PaymentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    //    private val pvm: ProfileViewModel by activityViewModels()
    private val addViewModel: AddressViewModel by activityViewModels()

    private val cartManageVM: CartManageViewModel by activityViewModels()
    private val cartVM: CartViewModel by viewModels()


    private lateinit var cartData: MutableList<ProductData>

    private lateinit var dialogHelper: DialogHelper
    private var paymentMode: String = "online"

    private var cartTotal = 0.0
    private var delPartCharge = 20.0
    private var couponAmount = 0.0
    private var convCharge: Double = 0.0
    private var grandTotal: Double = 0.0
    private var slotDate: String? = null

    private lateinit var cartValidateDataList: MutableList<CartValidateData>
    private lateinit var cartValidateListResponse: List<CartProdBackendData>

    private val addressDataList: MutableList<AddressData> = mutableListOf()
    private var addressId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()

        dialogHelper = DialogHelper(mContext, mActivity)

        Log.d("Rishabh", "User id at cart: ${Constant.userId} name: ${Constant.name}")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.white)
        startShimmer()
        observeAddress()
        setAddress()


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
        validateCart(
            CartValidateReq(
                sellerId = Constant.sellerId,
                sellerAutoId = Constant.sellerAutoId,
                cart = cartValidateDataList
            )
        )

        binding.apply {
            btnPay.setOnClickListener {
                if (checkFields()) {
                    if (Constant.userId == "0") {
                        navigateToLogin()
                    } else doPayment()
                } else {
                    cartScrollView.post {
                        var targetViewTop = dayRadGr.top
                        var parent = dayRadGr.parent
                        while (parent is ViewGroup && parent != cartScrollView) {
                            targetViewTop += (parent as View).top
                            parent = parent.parent
                        }
                        cartScrollView.smoothScrollTo(0, targetViewTop)
                    }
                    Toast.makeText(
                        mContext,
                        "Please provide delivery date \uD83D\uDE05",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            tvChangeAddress.setOnClickListener {
                showAddressDialog()
            }
            deliveryEdTxt.setOnClickListener {
                AppUtils.showCalendar(mContext, false) {
                    slotDate = it
                    deliveryEdTxt.setText(it)
                }
            }

            val visList = getVisibleTimeRanges()
            setTodayTime(visList)
            dayRadGr.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.todayRadB -> {
                        setSlotTimeVisible()
                        setTodayTime(visList)
                    }

                    R.id.tommorowRadB -> {
                        setSlotTimeVisible()
                        setRadTimeEnable()
                        time710RB.isChecked = true
                    }

                    R.id.customizeRadB -> {
                        setSlotTimeVisible(View.GONE, View.VISIBLE)
                    }
                }
            }

            tvCouponDetails.setOnClickListener {
                val couponSheet = CouponBottomSheetFragment() { amt, name, disc ->
                    tvCouponTitle.text = getString(R.string.applied_s, name)
                    subCouponTxtView.text = getString(R.string.s_discount, disc, amt)
                    couponAmount = amt
                    appCouponTxt.text = getString(R.string.minus_rs_f, amt)
                    setGrandTotal()
                }
                couponSheet.show(childFragmentManager, CouponBottomSheetFragment.TAG)
            }

        }
    }

    private fun navigateToLogin() {
        val direction = CartFragmentDirections.actionCartFragmentToLoginFragment(true)
        findNavController().navigate(direction)
    }

    private fun setSlotTimeVisible(slotTime: Int = View.VISIBLE, date: Int = View.GONE) {
        binding.slotTimeRadGr.visibility = slotTime
        binding.deliveryEdInput.visibility = date
    }

    private fun checkFields(): Boolean {
        if (binding.dayRadGr.checkedRadioButtonId == R.id.customizeRadB) {
            return !slotDate.isNullOrEmpty()
        }
        return true

    }

    private fun setTodayTime(visList: List<Boolean>) {
        setRadTimeEnable(visList[0], visList[1], visList[2])
        binding.apply {
            if (time710RB.isEnabled) setRadTimeCheck(true)
            else if (time123RB.isEnabled) setRadTimeCheck(b2 = true)
            else if (time59RB.isEnabled) setRadTimeCheck(b3 = true)
            else setRadTimeCheck()
        }
    }


    private fun setRadTimeEnable(b1: Boolean = true, b2: Boolean = true, b3: Boolean = true) {
        binding.apply {
            time710RB.isEnabled = b1
            time123RB.isEnabled = b2
            time59RB.isEnabled = b3
        }
    }


    private fun setRadTimeCheck(b1: Boolean = false, b2: Boolean = false, b3: Boolean = false) {
        binding.apply {
            time710RB.isChecked = b1
            time123RB.isChecked = b2
            time59RB.isChecked = b3
        }
    }

    private fun validateCart(cartValidateReq: CartValidateReq) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.validateCart(cartValidateReq).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.statusCode == 200) {
                        val outOfStockList = mutableListOf<CartUpdatedProdData>()
                        cartValidateListResponse = it.body()!!.list ?: listOf()
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
                        AppUtils.showErrorMsgDialog(
                            mContext,
                            getString(R.string.validate_cart_error)
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                AppUtils.showErrorMsgDialog(
                    mContext,
                    getString(R.string.validate_cart_error_tech)
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setCartData() {

        cartTotal = 0.0
        for (prod in cartData) {
            cartTotal += (prod.discountedPrice ?: 0.0) * prod.count
        }
        cartManageVM.setCartTotalPrice(cartTotal)

        stopShimmer() // At this point cart is validated and cart item are loaded

        bindCartItemObserver()
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

    private fun bindCartItemObserver() {
        cartManageVM.cartTotalItem.observe(viewLifecycleOwner) {
            binding.numItemTxt.text = getString(R.string.d_items, it)
        }
    }

    private fun getDeliveryCharges(cartTotal: Double) {
        binding.progBar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
//            try {
            cartVM.getDeliveryCharges(CommonDataReq(totalCartAmount = cartTotal)).let {
                binding.progBar.visibility = View.GONE
                if (it.isSuccessful && it.body() != null && it.body()!!.statusCode == 200) {
                    delPartCharge = it.body()!!.deliveryCharge ?: 0.0
                    convCharge = it.body()!!.convCharge ?: 0.0
                    binding.tvDelCharge.text = if (delPartCharge == 0.0) "Free" else getString(
                        R.string.rs_f,
                        delPartCharge
                    )
                    binding.convChargeTxt.text =
                        if (convCharge == 0.0) "Free" else getString(R.string.rs_f, convCharge)
                    setGrandTotal()
                } else {
                    AppUtils.showErrorMsgDialog(
                        mContext,
                        getString(R.string.delivery_charges_error)
                    ) {
                        findNavController().popBackStack()
                    }
                }
            }
//            } catch (e: Exception) {
//                binding.progBar.visibility = View.GONE
//                AppUtils.showErrorMsgDialog(
//                    mContext,
//                    getString(R.string.delivery_charges_error_tech)
//                ) {
//                    findNavController().popBackStack()
//                }
//            }
        }
    }

    private fun bindTotalPriceObserver() {
        cartManageVM.cartTotalPrice.observe(viewLifecycleOwner) {
            binding.tvItemPrice.text = it.toString()
            cartTotal = it
            if (it != 0.0) getDeliveryCharges(it)
        }
    }

    private fun setGrandTotal() {
        grandTotal =
            cartTotal + delPartCharge - couponAmount + (if (paymentMode == "online") convCharge else 0.0)
        binding.tvGrandTotal.text = getString(R.string.rs_f, grandTotal)
        binding.tvTotalPayAmount.text = getString(R.string.rs_f, grandTotal)
    }


    private fun findIndex(cart: CartProdBackendData): Int {
        cartData.forEachIndexed { index, prod ->
            if (prod.id == cart.id && prod.sizeId == cart.sizeid) return index
        }
        return -1
    }

    private val secondActivityLauncherContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                when (data?.getStringExtra("status")) {
                    "SUCCESS" -> {
                        doPaymentVerif(
                            data.getStringExtra("orderNo").toString(),
                            data.getStringExtra("txnAmount").toString().toDouble(),
                            data.getStringExtra("transId").toString()
                        )
                    }
                    "REJECTED" -> {
                        Toast.makeText(mContext, "Payment Status Rejected", Toast.LENGTH_SHORT).show()
                    }
                    "FAILED" -> {
                        Toast.makeText(mContext, "Payment Status Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    private fun doPayment() {
        val paymentReq = PaymentReq(
            userId = Constant.userId.toInt(),
            addedByWeb = Constant.addedByWeb,
            paymenttype = paymentMode, //online,cod  // Todo change after payment gateway
            totalPayble = grandTotal.roundToInt(),
            convCharge = convCharge.roundToInt(),        // Todo change after payment gateway,
            cart = cartValidateDataList,
            sellerId = Constant.sellerId,
            sellerAutoId = Constant.sellerAutoId
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.doPayment(paymentReq).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.statusCode == 200) {
                        if (it.body()!!.isOutOfStock == 0) {

                            val orderId = it.body()?.orderId.toString()
                            val txnAmount = it.body()?.tXNAMOUNT.toString()

//                        bookTicket()
                            if (paymentMode == it.body()?.paymentType) {
                                val intent = Intent(mContext, PaymentActivity::class.java)
                                    .putExtra("ORDER_ID", orderId)
                                    .putExtra("MID", it.body()?.mID)
                                    .putExtra("CONV_CHARGE", it.body()!!.cONVCHARGE.toString())
                                    .putExtra("MAIN_AMOUNT", it.body()!!.mAINAMOUNT.toString())
                                    .putExtra("TXN_AMOUNT", txnAmount)
                                    .putExtra("CUST_ID", it.body()?.cUSTID)
                                secondActivityLauncherContracts.launch(intent)
                            }
                        }

                    } else {
                        AppUtils.showErrorMsgDialog(
                            mContext,
                            getString(R.string.payment_not_done)
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                AppUtils.showErrorMsgDialog(mContext, getString(R.string.payment_not_done_tech)) {
//                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun doPaymentVerif(orderNo: String, txnAmount: Double, transId: String) {
        val payVerifyReq = PaymentVerifyReq(
            userId = Constant.userId.toInt(),
            convCharge = convCharge,
            orderno = orderNo,
            tXNAMOUNT = txnAmount,
            transId = transId
        )
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.doPaymentVerification(payVerifyReq).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.statusCode == 200) {
                        if (true) { // it.body()!!.paymentVerify) {
                            doConfirmOrder(orderNo, txnAmount, transId)
                        } else {
                            AppUtils.showErrorMsgDialog(
                                mContext,
                                getString(R.string.payment_not_verified)
                            ) {
                                findNavController().popBackStack()
                            }
                        }
                    } else {
                        AppUtils.showErrorMsgDialog(
                            mContext,
                            getString(R.string.payment_verification_problem)
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                AppUtils.showErrorMsgDialog(
                    mContext,
                    getString(R.string.payment_technical_problem)
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun doConfirmOrder(orderNo: String, txnAmount: Double, transId: String) {

        val lastCartProductList = cartManageVM.getCartList()
        val confirmCart = mutableListOf<Cart>()

        for (prod in lastCartProductList) {
            for (validateProd in cartValidateListResponse) {
                if (prod.id == validateProd.id && prod.sizeId == validateProd.sizeid) {
                    confirmCart.add(
                        Cart(
                            discount = prod.discount?.toDouble() ?: 0.0,
                            netprice = prod.discountedPrice,
                            price = prod.price?.toDouble() ?: 0.0,
                            productId = prod.id.toIntOrNull(),
                            productImage = prod.image,
                            productName = prod.productName,
                            qty = prod.count,
                            sizeId = prod.sizeId.toInt(),
                            totalAmount = prod.discountedPrice?.times(prod.count),
                            weight = prod.weight,
                            sellerAutoId = validateProd.sellerAutoId,
                            sellerCreditAmount = validateProd.sellerCreditAmount,
                            sellerId = validateProd.sellerId,
                            adminProfit = validateProd.adminProfit,
                        )
                    )
                    break
                }
            }
        }

        val confirmOrderReq = ConfirmOrderReq(
            addedByWeb = Constant.addedByWeb,
            addressData = addressDataList,
            addressId = addressId,
            cart = confirmCart,
            convCharge = convCharge,
            deliveryCharge = delPartCharge,
            email = Constant.email,
            grandTotal = grandTotal,
            orderId = orderNo,
            paymentMode = paymentMode,
            phone = Constant.phoneNo,
            totalAmount = cartTotal,
            transId = transId,
            sellerAutoId = Constant.sellerAutoId.toInt(),
            sellerId = Constant.sellerId,
            txnAmount = txnAmount,
            slotdeliveryDate = when (binding.dayRadGr.checkedRadioButtonId) {
                R.id.todayRadB -> getTodayAndTomorrowDates().first
                R.id.tommorowRadB -> getTodayAndTomorrowDates().second
                else -> slotDate
            },
            slotdeliveryTime = when (binding.slotTimeRadGr.checkedRadioButtonId) {
                R.id.time7_10RB -> binding.time710RB.text.toString()
                R.id.time12_3RB -> binding.time123RB.text.toString()
                else -> if (slotDate.isNullOrEmpty()) binding.time59RB.text.toString() else ""
            },
            userId = Constant.userId.toInt()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.doConfirmOrder(confirmOrderReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            cartManageVM.clearCart()
                            cartManageVM.countMap.clear()

                            navigateToOrderDetail(it.body()!!.orderId)

                            Toast.makeText(mContext, "Order placed", Toast.LENGTH_SHORT).show()
                        } else {
                            AppUtils.showErrorMsgDialog(
                                mContext,
                                getString(R.string.payment_technical_problem)
                            ) {
                                findNavController().popBackStack()
                            }
                        }
                    } else {
                        AppUtils.showErrorMsgDialog(
                            mContext,
                            getString(R.string.payment_technical_problem)
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                AppUtils.showErrorMsgDialog(
                    mContext,
                    getString(R.string.payment_technical_problem)
                ) {
                    findNavController().popBackStack()
                }
            }

        }

    }

    private fun navigateToOrderDetail(orderId: String?) {
        if (orderId.isNullOrEmpty()) {
            Toast.makeText(mContext, "Order Id not found", Toast.LENGTH_SHORT).show()
        } else {
            val direction = CartFragmentDirections.actionCartFragmentToOrderDetailsFragment(orderId)
            findNavController().navigate(direction)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        cartManageVM.cartTotalPrice.removeObservers(viewLifecycleOwner)
        cartManageVM.cartTotalItem.removeObservers(viewLifecycleOwner)
    }

    private fun setAddress() {
        binding.tvDeliveryAddress.text = addViewModel.myAddress.value?.permanentAdd
        binding.tvDeliveringTo.text =
            getString(R.string.delivering_to, addViewModel.myAddress.value?.addressName)
        checkAddress()
    }

    private fun checkAddress() {
        if (addViewModel.myAddress.value?.id.isNullOrEmpty()) {
            addViewModel.myAddress.value?.apply {
                if (addressDataList.size != 0) {
                    addressDataList.clear()
                }
                addressDataList.add(
                    AddressData(
                        addressName = addressName,
                        city = city,
                        country = country,
                        landMark = landMark,
                        locality = locality,
                        perAdd = permanentAdd,
                        phone = phone,
                        state = state,
                        userId = Constant.userId.toInt(),
                        zipcode = zipcode,
                        longitude = longitude,
                        latitude = latitude
                    )
                )
            }
            addressId = 0
        } else {
            addressId = (addViewModel.myAddress.value?.id ?: "0").toInt()
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

    private fun observeAddress() {
        addViewModel.myAddress.observe(viewLifecycleOwner) {
            setAddress()
        }
    }

    private fun startShimmer() {
        binding.apply {
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            clCoupon.visibility = View.GONE
            rvCartItems.visibility = View.GONE
            clTaxPrices.visibility = View.GONE
            clDeliveryInstructions.visibility = View.GONE
            clPay.visibility = View.GONE
        }
    }

    private fun stopShimmer() {
        binding.apply {
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
            clCoupon.visibility = View.VISIBLE
            rvCartItems.visibility = View.VISIBLE
            clTaxPrices.visibility = View.VISIBLE
            clDeliveryInstructions.visibility = View.VISIBLE
            clPay.visibility = View.VISIBLE
        }
    }

}