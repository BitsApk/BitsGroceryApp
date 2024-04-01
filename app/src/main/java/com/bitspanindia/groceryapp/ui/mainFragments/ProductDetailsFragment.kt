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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.response.GetProductDetailsResponse
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
    private lateinit var dialogHelper: DialogHelper
    private val pvm: ProductViewModel by activityViewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()
    private val cartVM: CartManageViewModel by activityViewModels()

    private lateinit var selectedItem : MultiWeight
    private lateinit var prodData: GetProductDetailsResponse


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()
        dialogHelper = DialogHelper(mContext, mActivity)

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

        binding.btnAdd.setOnClickListener {
            if (cartVM.countMap[args.proId].isNullOrEmpty()) {
                cartVM.countMap[args.proId] = mutableMapOf(Pair(selectedItem.sizeId ?: "-1", 1), Pair("-1", 1))
            } else {
                cartVM.countMap[args.proId]!![selectedItem.sizeId ?: "-1"] = 1
                cartVM.countMap[args.proId]!!["-1"] = cartVM.countMap[args.proId]!!["-1"]!! + 1
            }
            val cartTotalItem = cartVM.cartTotalItem.value
            cartVM.setCartTotal((cartTotalItem ?: 0) + 1)
            val product = ProductData(
                discount = selectedItem.discount,
                discountedPrice = selectedItem.discountedPrice,
                id = args.proId,
                image = prodData.multiimg?.get(0)?.image,
                price = selectedItem.price,
                productName = prodData.productName,
                rating = prodData.rating,
                stock = prodData.stock,
                weight = selectedItem.weight,
                sizeId = selectedItem.sizeId ?: "-1",
                returnable = "No",  // Todo ask for parameter in api response
            )
            cartVM.addItemToCart(product)
            checkCart(selectedItem)
        }

        refreshOnCartChange()

    }


    private fun refreshOnCartChange() {
        cartVM.cartTotalItem.observe(viewLifecycleOwner) {
            if (cartVM.isCartVisible) {
                checkCart(selectedItem)
                binding.rvProducts.adapter!!.notifyDataSetChanged()
            }

        }
    }


    private fun getProductDetails() {
        val commonDataReq = CommonDataReq()
        commonDataReq.productId = args.proId
        commonDataReq.addedByWeb = Constant.addedByWeb

        startProgress()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                pvm.getProductDetails(commonDataReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        stopProgress()
                        prodData = it.body() ?: GetProductDetailsResponse()
                        if (prodData.statusCode == 200) {
                            binding.apply {
                                tvProductName.text = prodData.productName
                                pvm.prodImageList = prodData.multiimg ?: listOf<MultiImg>()

                                var selectedPos = -1
                                for (index in 0 until prodData.multiweight?.size!!) {
                                    val weight = prodData.multiweight!![index]
                                    if (weight.stock > 0) {
                                        selectedPos = index
                                        selectedItem = weight
                                        setProductPrice(weight)
                                        checkCart(weight)
                                        break
                                    }
                                }

                                if (selectedPos == -1) setAddBtn(getString(R.string.no_stock), false)
                                setProducts(prodData.related ?: mutableListOf())
                                setViewpagerSlider(prodData.multiimg)
                                setProductDetails(prodData.description ?: "")
                                rvUnit.adapter = UnitAdapter(
                                    mContext,
                                    selectedPos,
                                    prodData.multiweight ?: listOf()
                                ) { item ->
                                    selectedItem = item
                                    checkCart(item)
                                }
                            }
                        } else {
                            dialogHelper.showErrorMsgDialog(
                                it.body()?.message ?: "Something went wrong"
                            ) { findNavController().popBackStack() }
                        }
                    } else {
                        dialogHelper.showErrorMsgDialog(
                            getString(R.string.prod_page_error)
                        ) { findNavController().popBackStack() }
                    }
                }
            } catch (e: Exception) {
                dialogHelper.showErrorMsgDialog(
                    getString(R.string.prod_page_error_tech)
                ) { findNavController().popBackStack() }
            }
        }

    }

    private fun checkCart(item: MultiWeight) {
        setProductPrice(item)
        if (item.stock <= 0) {
            setAddBtn(getString(R.string.no_stock), false)
        } else if (!cartVM.countMap[args.proId].isNullOrEmpty() && cartVM.countMap[args.proId]!![item.sizeId] != null) {
            setAddBtn(getString(R.string.added), false)
        } else {
            setAddBtn(getString(R.string.add), true)
        }
    }

    private fun setAddBtn(textt: String, enable: Boolean) {
        binding.btnAdd.apply {
            text = textt
            isEnabled = enable
        }
    }

    private fun setViewpagerSlider(multiImg: List<MultiImg>?) {
        val adapter = SliderAdapter(mContext, multiImg ?: listOf()) { pos ->
            val action =
                ProductDetailsFragmentDirections.actionProductDetailsFragmentToProductImagesFragment(
                    pos
                )
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
                    val action = GroceryHomeFragmentDirections.actionGlobalProductDetailsFragment(prod.id)
                    findNavController().navigate(action)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cartVM.cartTotalItem.removeObservers(viewLifecycleOwner)
    }

}