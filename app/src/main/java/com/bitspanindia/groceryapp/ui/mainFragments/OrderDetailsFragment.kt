package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.response.OrderDetailsResponse
import com.bitspanindia.groceryapp.databinding.FragmentOrderDetailsBinding
import com.bitspanindia.groceryapp.presentation.adapter.OrderItemAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailsBinding
    private val pvm: ProfileViewModel by activityViewModels()
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var dialogHelper: DialogHelper
    private val args: OrderDetailsFragmentArgs by navArgs()
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var apiCallCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()
        dialogHelper = DialogHelper(mContext, mActivity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callOrderDetailsApi()

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopOrderDetailsApiCall()
    }

    private fun callOrderDetailsApi() {
        runnable = object : Runnable {
            override fun run() {
                handler.post {
                    apiCallCount++
                    getOrderDetails()
                }
                handler.postDelayed(this, 15000)
            }
        }
        handler.post(runnable!!)
    }

    private fun stopOrderDetailsApiCall() {
        handler.removeCallbacks(runnable!!)
    }

    private fun getOrderDetails() {
        val orderDetailReq = CommonDataReq(userId = Constant.userId, orderId = args.orderId)
        if (apiCallCount <= 1) showProgress()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.getOrderDetails(orderDetailReq).let {
                    if (apiCallCount <= 1) hideProgress()
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            val data = it.body()
                            setOrderDetailsData(data)
                            binding.rvItems.adapter =
                                OrderItemAdapter(mContext, data?.productOrder ?: listOf())
                        } else {
                            dialogHelper.showErrorMsgDialog(
                                it.body()?.message ?: "Something went wrong"
                            ) {
                                findNavController().popBackStack()
                            }
                        }
                    } else {
                        dialogHelper.showErrorMsgDialog("Something went wrong") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                dialogHelper.showErrorMsgDialog("Something went wrong") {
                    findNavController().popBackStack()
                }
            }

        }

    }

    private fun setOrderDetailsData(data: OrderDetailsResponse?) {
        binding.apply {

            tvItemTotal.text = getString(R.string.rupee, data?.preamount)
            tvDelChar.text = getString(R.string.rupee, data?.delevaryCharge)
            tvConvChar.text = getString(R.string.rupee, data?.convCharge)
            tvDiscount.text = getString(R.string.discount_price, data?.couponamt)
            tvTolAmt.text = getString(R.string.rupee, data?.amount)
            tvOrdId.text = data?.orderId
            tvPayMode.text = data?.payMode
            tvDeliveryAdd.text = data?.address
            tvOrdDate.text = data?.createddate

            if (!data?.couponamt.isNullOrEmpty() && data?.couponamt != "0") {
                tvDiscountTitle.visibility = View.VISIBLE
                tvDiscount.visibility = View.VISIBLE
            }

            when (data?.orderStatus) {
                "P" -> {
                    tvOrderStatus.text = getString(R.string.one_str, "Order Placed")
                    setLottieAnim("order_placed.json")
                }

                "PR", "PCK" -> {
                    tvOrderStatus.text = getString(R.string.one_str, "Order Packed")
                    setLottieAnim("order_packed.json")
                }

                "S" -> {
                    tvOrderStatus.text = getString(R.string.one_str, "Order Shipped")
                    setLottieAnim("order_shipped.json")
                    navigateToOrderTracking(
                        data.latitude ?: "",
                        data.longitude ?: "",
                        data.orderId ?: ""
                    )
                }

                "D" -> {
                    tvOrderStatus.text = getString(R.string.one_str, "Order Delivered")
                    setLottieAnim("order_delivered.json")
                }

                "C" -> {
                    tvOrderStatus.text = getString(R.string.one_str, "Order Cancelled")
                    setLottieAnim("order_cancel.json")
                }

                "DC" -> {
                    tvOrderStatus.text = getString(R.string.one_str, data.orderStatusMess)
                    setLottieAnim("order_cancel.json")
                }

            }
        }
    }

    private fun navigateToOrderTracking(latitude: String, longitude: String, orderId: String) {
        val action = OrderDetailsFragmentDirections.actionOrderDetailsFragmentToOrderTrackingFragment(latitude, longitude, orderId)
        findNavController().navigate(action)
    }

    private fun setLottieAnim(lottieName: String) {
        binding.orderStatusLottie.setAnimation(lottieName)
        binding.orderStatusLottie.playAnimation()
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
        binding.nestedScrollView.visibility = View.GONE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
        binding.nestedScrollView.visibility = View.VISIBLE
    }

}