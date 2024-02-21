package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.response.MultiImg
import com.bitspanindia.groceryapp.data.model.response.MultiWeight
import com.bitspanindia.groceryapp.databinding.FragmentProductDetailsBinding
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.adapter.UnitAdapter
import com.bitspanindia.groceryapp.presentation.adapter.slider.SliderAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val pvm: ProductViewModel by activityViewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()
    private val cartVM: CartManageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProductDetails()

        binding.ivSearch.setOnClickListener {
            findNavController().navigate(
                ProductDetailsFragmentDirections.actionGlobalSearchProductFragment()
            )
        }

        binding.clPDetails.setOnClickListener {
            binding.webView.visibility = if (binding.webView.isVisible) View.GONE else View.VISIBLE
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun getProductDetails() {
        val commonDataReq = CommonDataReq()
        commonDataReq.productId = args.proId
        commonDataReq.userId = Constant.userId

        startProgress()

        viewLifecycleOwner.lifecycleScope.launch {
            pvm.getProductDetails(
                commonDataReq
            ).let {
                try {
                    if (it.body() != null) {
                        stopProgress()
                        val data = it.body()
                        if (data?.statusCode == 200) {
                            binding.apply {
                                tvProductName.text = data.productName
                                pvm.prodImageList = data.multiimg ?: listOf<MultiImg>()
                                setProductPrice(data.multiweight?.get(0) ?: MultiWeight())
                                setProducts(data.related ?: mutableListOf())
                                setViewpagerSlider(data.multiimg)
                                setProductDetails(data.description ?: "")
                                rvUnit.adapter = UnitAdapter(mContext, data.multiweight ?: listOf()) { setProductPrice(it) }
                            }
                        } else {
                            findNavController().popBackStack()
                        }
                    } else {
                        findNavController().popBackStack()
                    }
                } catch (e: Exception) {
                    findNavController().popBackStack()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setViewpagerSlider(multiImg: List<MultiImg>?) {
        val adapter = SliderAdapter(mContext, multiImg ?: listOf()) { pos ->
            val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToProductImagesFragment(pos)
            findNavController().navigate(action)
        }
        binding.viewPager.adapter = adapter

        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }
    private fun setProductDetails(description: String) {
        binding.webView.loadDataWithBaseURL(
            null,
            description ?: "",
            "text/html",
            "utf-8",
            null
        )
    }

    private fun startProgress() {
        binding.apply {
            progress.visibility = View.VISIBLE
            clHeader.visibility = View.GONE
            nestedScrollView.visibility = View.GONE
        }
    }

    private fun stopProgress() {
        binding.apply {
            progress.visibility = View.GONE
            clHeader.visibility = View.VISIBLE
            nestedScrollView.visibility = View.VISIBLE
        }
    }

    private fun setProductPrice(data: MultiWeight) {
        binding.apply {
            tvWeight.text = data.weight
            tvPrice.text = getString(R.string.rupee, data.discountedPrice.toString())
            tvDisPrice.visibility = if (data.price.isNullOrEmpty()) View.GONE else View.VISIBLE
            tvDisPrice.text = getString(R.string.rupee, data.price.toString())
            tvDisPrice.paintFlags = binding.tvDisPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setProducts(productData: MutableList<ProductData>) {
//            cartVM.cartTotalItem.observe(viewLifecycleOwner) {

        binding.rvProducts.adapter = ProductsAdapter(
            productData,
            mContext,
            cartVM.countMap,
            5 /* 5 for grid item */
        ) { prod, action ->
            val cartTotalItem = cartVM.cartTotalItem.value
            when (action) {
                CartAction.Add -> {
                    Log.d("Rishabh", "Cart action add clicked")
                    cartVM.setCartTotal((cartTotalItem ?: 0) + 1)
                    cartVM.addItemToCart(prod)
                }

                CartAction.Minus -> {
                    cartVM.setCartTotal((cartTotalItem ?: 0) - 1)
                    cartVM.decreaseCountOfItem(prod)
                }

                CartAction.ItemClick -> {
                    val action = HomeFragmentDirections.actionGlobalProductDetailsFragment(prod.id)
                    findNavController().navigate(action)
                }

            }
//            }

        }
    }

}