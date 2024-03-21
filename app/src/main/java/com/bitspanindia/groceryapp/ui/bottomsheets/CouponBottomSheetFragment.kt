package com.bitspanindia.groceryapp.ui.bottomsheets

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.startShimmer
import com.bitspanindia.groceryapp.AppUtils.stopShimmer
import com.bitspanindia.groceryapp.AppUtils.toDp
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.ApplyCouponReq
import com.bitspanindia.groceryapp.data.model.request.CartValidateData
import com.bitspanindia.groceryapp.data.model.response.CouponCode
import com.bitspanindia.groceryapp.databinding.FragmentCouponBottomSheetBinding
import com.bitspanindia.groceryapp.presentation.adapter.CouponImageAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CouponBottomSheetFragment(val callBack: (amount: Double, name: String, disc: String) -> Unit) : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "CouponBottomSheet"
    }

    private lateinit var binding: FragmentCouponBottomSheetBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    private val cartVM: CartViewModel by viewModels()
    private val cartManageVM: CartManageViewModel by activityViewModels()
    private lateinit var mBehave: BottomSheetBehavior<FrameLayout>

    private var cartValidateDataList: MutableList<CartValidateData>? = null
    private lateinit var couponList: List<CouponCode>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCouponBottomSheetBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            mBehave = this.behavior
            mBehave.maxHeight = 600.toDp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startShimmer(binding.shimmer, binding.couponRecView)
        getCouponList()


    }

    private fun getCouponList() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.getCouponList().let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200 && !it.body()!!.couponCode.isNullOrEmpty()) {
                            couponList = it.body()!!.couponCode!!
                            binding.couponRecView.layoutManager =
                                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                            binding.couponRecView.adapter =
                                CouponImageAdapter(couponList, mContext) { code, pos ->
                                    checkValidity(code, pos)
                                }
                            stopShimmer(binding.shimmer, binding.couponRecView)
                        } else {
                            stopShimmer(binding.shimmer, binding.couponRecView)
                            binding.noCouponFound.tvNotFound.text =
                                getString(R.string.no_coupon_found)
                            binding.noCouponFound.clNoProduct.visibility = View.VISIBLE
                        }
                    }
                }
            } catch (e: Exception) {
                stopShimmer(binding.shimmer, binding.couponRecView)
                mBehave.state = BottomSheetBehavior.STATE_HIDDEN
                AppUtils.showErrorMsgDialog(mContext, getString(R.string.coupon_error_tech)) {

                }
            }
        }
    }

    private fun checkValidity(code: String, pos: Int) {
        binding.progBar.visibility = View.VISIBLE

        if (cartValidateDataList == null) {
            val cartData = cartManageVM.getCartList()
            cartValidateDataList = mutableListOf()
            for (i in cartData) {
                cartValidateDataList!!.add(
                    CartValidateData(
                        productId = i.id.toInt(),
                        qty = i.count,
                        sizeId = i.sizeId.toInt()
                    )
                )
            }
        }

        val applyCouponReq = ApplyCouponReq(
            cart = cartValidateDataList,
            couponcode = code,
            sellerAutoId = Constant.sellerAutoId.toInt(),
            sellerId = Constant.sellerId
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cartVM.checkCoupon(applyCouponReq).let {
                    if (it.isSuccessful && it.body() != null && it.body()!!.statusCode == 200) {
                        binding.progBar.visibility = View.GONE
                        mBehave.state = BottomSheetBehavior.STATE_HIDDEN
                        callBack(it.body()!!.couponAmt ?: 0.0, couponList[pos].promoCodeName ?: "", couponList[pos].promoCodeDiscount ?: "")
                    } else {
                        binding.progBar.visibility = View.GONE
                        Toast.makeText(mContext, getString(R.string.can_t_apply_this_coupon), Toast.LENGTH_SHORT).show()
                        couponList[pos].canApply = false
                        couponList[pos].mess = it.body()!!.mess ?: getString(R.string.can_t_apply_this_coupon)
                        binding.couponRecView.adapter?.notifyItemChanged(pos)
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                mBehave.state = BottomSheetBehavior.STATE_HIDDEN
                AppUtils.showErrorMsgDialog(mContext, getString(R.string.apply_coupon_tech)) {}
            }
        }
    }

}