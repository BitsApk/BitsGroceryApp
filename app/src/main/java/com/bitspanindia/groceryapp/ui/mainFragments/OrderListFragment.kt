package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.bitspanindia.groceryapp.AppUtils.startShimmer
import com.bitspanindia.groceryapp.AppUtils.stopShimmer
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.model.response.Order
import com.bitspanindia.groceryapp.databinding.FragmentOrderListBinding
import com.bitspanindia.groceryapp.presentation.adapter.OrderListPagingAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class OrderListFragment : Fragment() {
    private lateinit var binding: FragmentOrderListBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var adapter: OrderListPagingAdapter
    private val pvm: ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderListBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noProduct.tvNotFound.text = getString(R.string.one_str, "Not Orders Found")

        setOrders()

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setOrders() {
        binding.noProduct.tvNotFound.text = getString(R.string.one_str,"No Order Found")
        setOrderAdapter()
        getOrderList()

        adapter.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading) {
                binding.noProduct.clNoProduct.visibility = View.GONE
                stopShimmer(binding.shimmer2, binding.rvOrders)
            } else if (it.source.refresh is LoadState.Error) {
                binding.noProduct.clNoProduct.visibility = View.VISIBLE
                stopShimmer(binding.shimmer2, binding.rvOrders)
            }
        }
    }

    private fun getOrderList() {
        val productDataReq = ProductDataReq()
        startShimmer(binding.shimmer2, binding.rvOrders)
        binding.noProduct.clNoProduct.visibility = View.GONE
        binding.rvOrders.visibility = View.VISIBLE
        productDataReq.userId = Constant.userId
        productDataReq.pageno = 1

        viewLifecycleOwner.lifecycleScope.launch {
            pvm.getOrderList(
                productDataReq
            ).collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setOrderAdapter() {
        adapter = OrderListPagingAdapter(requireContext()){data->

            when(data.orderStatus){
                "P"->{
                    navigateToTracking(data)
                }
                "PR"->{
                    navigateToTracking(data)
                }
                "S"->{
                    navigateToTracking(data)
                }
                "D"->{
                    navigateToOrderDetails(data.orderId?:"")
                }
                "C","DC"->{
                    navigateToOrderDetails(data.orderId?:"")
                }

                else -> {}
            }

        }
        binding.rvOrders.adapter = adapter
    }

    private fun navigateToTracking(data: Order) {
        findNavController().navigate(OrderListFragmentDirections.actionOrderListFragmentToOrderTrackingFragment(data.latitude?:"",data.longitude?:"",data.orderId?:""))
    }

    private fun navigateToOrderDetails(orderId:String) {
          findNavController().navigate(OrderListFragmentDirections.actionOrderListFragmentToOrderDetailsFragment(orderId))
    }

}