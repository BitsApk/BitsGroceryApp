package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.pagingSource.ProductPagingSource
import com.bitspanindia.groceryapp.databinding.FragmentSubCategoryBinding
import com.bitspanindia.groceryapp.presentation.adapter.MainCategoryImageAdapter
import com.bitspanindia.groceryapp.presentation.adapter.ProductPagingAdapter
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.HomeViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class SubCategoryFragment : Fragment() {
    private lateinit var binding: FragmentSubCategoryBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var adapter: ProductPagingAdapter
    private val args: SubCategoryFragmentArgs by navArgs()
    private var subCatId = ""

    private val homeVM: HomeViewModel by activityViewModels()
    private val cartVM: CartManageViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubCategoryBinding.inflate(inflater, container, false)
        mContext = requireContext()
        mActivity = requireActivity()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSubCatData()

        binding.tvText.text = args.catName

        binding.ivSearch.setOnClickListener {
            findNavController().navigate(
                SubCategoryFragmentDirections.actionGlobalSearchProductFragment()
            )
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        observeProduct()

    }

    private fun observeProduct() {
        cartVM.cartTotalItem.observe(viewLifecycleOwner) {

            if (cartVM.isCartVisible) {
                val layMang = binding.rvProducts.layoutManager
                if (layMang is GridLayoutManager) {
                    val firstVisiblePosition = layMang.findFirstVisibleItemPosition()
                    Log.d("Rishabh", "First visible pos: $firstVisiblePosition, last: ${layMang.findLastVisibleItemPosition()}")
                    adapter.notifyItemRangeChanged(firstVisiblePosition - 4, 14)
                }
            }
        }
    }


    private fun getSubCatData() {
        AppUtils.startShimmer(binding.shimmer2, binding.rvProducts)
        AppUtils.startShimmer(binding.shimmer, binding.rvCategory)
        val commonDataReq = CommonDataReq(categoryId = args.catId)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                homeVM.getSubCatList(commonDataReq).let {
                    AppUtils.stopShimmer(binding.shimmer, binding.rvProducts)
                    AppUtils.stopShimmer(binding.shimmer2, binding.rvCategory)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode == 200) {
                            val data = it.body()?.subCategory
                            subCatId = data?.get(0)?.id ?: ""
                            setProducts()
                            binding.rvCategory.adapter = MainCategoryImageAdapter(
                                data ?: listOf(),
                                mContext,
                                "subCatPage"
                            ) { id, name ->
                                subCatId = id
                                setProducts()
                            }
                        } else {
                            findNavController().popBackStack()
                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        findNavController().popBackStack()
                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                findNavController().popBackStack()
                Toast.makeText(mContext, "Error two", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setProducts() {
        var productCount = 0
        setProductAdapter()
        getProductList()

        adapter.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading) {
                binding.noProduct.clNoProduct.visibility = View.GONE
                AppUtils.stopShimmer(binding.shimmer, binding.rvProducts)
                productCount = ProductPagingSource.productCount ?: 0
                binding.tvProductsCount.text =
                    getString(R.string.two_str, productCount.toString(), " Items")
            } else if (it.source.refresh is LoadState.Error) {
                binding.noProduct.clNoProduct.visibility = View.VISIBLE
                AppUtils.stopShimmer(binding.shimmer, binding.rvProducts)
            }
        }


    }

    private fun getProductList() {
        val productDataReq = ProductDataReq()
        AppUtils.startShimmer(binding.shimmer, binding.rvProducts)
        binding.rvProducts.visibility = View.VISIBLE
        binding.noProduct.clNoProduct.visibility = View.GONE
        productDataReq.userId = Constant.userId
        productDataReq.pageno = 1
        productDataReq.subcategoryId = subCatId
        productDataReq.addedByWeb = Constant.addedByWeb
        productDataReq.sellerId = Constant.sellerId
        productDataReq.sellerAutoId = Constant.sellerAutoId

        viewLifecycleOwner.lifecycleScope.launch {
            homeVM.getSubCatProducts(
                productDataReq
            ).collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setProductAdapter() {
        adapter =
            ProductPagingAdapter(mContext, ElementType.Grid.type, cartVM.countMap) { prod, action ->

                val cartTotalItem = cartVM.cartTotalItem.value
                when (action) {
                    CartAction.Add -> {
                        cartVM.setCartTotal((cartTotalItem ?: 0) + 1)

                        cartVM.updateToTotalPrice(prod.discountedPrice ?: 0.0)
                        cartVM.addItemToCart(prod)
                    }

                    CartAction.Minus -> {
                        cartVM.setCartTotal((cartTotalItem ?: 0) - 1)
                        cartVM.updateToTotalPrice(-(prod.discountedPrice ?: 0.0))
                        cartVM.decreaseCountOfItem(prod)
                    }

                    CartAction.ItemClick -> {
                        val direction =
                            SubCategoryFragmentDirections.actionGlobalProductDetailsFragment(prod.id)
                        findNavController().navigate(direction)
                    }
                }
            }
        binding.rvProducts.adapter = adapter
    }

}