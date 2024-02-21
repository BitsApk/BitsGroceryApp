package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.databinding.FragmentSearchProductBinding
import com.bitspanindia.groceryapp.presentation.adapter.ProductPagingAdapter
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.HomeViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchProductFragment : Fragment() {
    private lateinit var binding:FragmentSearchProductBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var adapter: ProductPagingAdapter
    private val homeVM: HomeViewModel by activityViewModels()
    private val cartVM: CartManageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchProductBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! >2){
                    setProducts(p0.toString())
                }else{
                    binding.rvProducts.visibility = View.GONE
                    binding.noProduct.clNoProduct.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

//    private fun getSearchProduct(searchValue: String) {
////        startShimmer(binding.shimmer2,binding.rvProducts)
//        val commonDataReq = CommonDataReq()
//        commonDataReq.userId = Constant.userId
//        commonDataReq.productName = searchValue
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                homeVM.searchProduct(commonDataReq).let {
////                    stopShimmer(binding.shimmer,binding.rvProducts)
//                    if (it.isSuccessful && it.body() != null) {
//                        if (it.body()?.statusCode==200){
//                            val data = it.body()?.searchProduct
//                            binding.rvProducts.adapter = ProductsAdapter(data?: mutableListOf(),mContext, cartVM.countMap, 0) {prod, action ->
//
//                            }
//                        }else{
////                            findNavController().popBackStack()
//                            Toast.makeText(mContext,"Something went wrong", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
////                        findNavController().popBackStack()
//                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: Exception) {
//
//            }
//
//        }
//
//    }

    private fun setProducts(productName: String) {
        setProductAdapter()
        getProductList(productName)

        adapter.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading) {
                binding.noProduct.clNoProduct.visibility = View.GONE
                stopShimmer(binding.shimmer2,binding.rvProducts)
            } else if (it.source.refresh is LoadState.Error) {
                binding.noProduct.clNoProduct.visibility = View.VISIBLE
                stopShimmer(binding.shimmer2,binding.rvProducts)
            }
        }
    }

    private fun stopShimmer(shimmer: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun startShimmer(shimmer: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun getProductList(productName:String) {
        val productDataReq = ProductDataReq()
        startShimmer(binding.shimmer2,binding.rvProducts)
        binding.noProduct.clNoProduct.visibility = View.GONE
        binding.rvProducts.visibility = View.VISIBLE
        productDataReq.userId = Constant.userId
        productDataReq.pageno = 1
        productDataReq.productName = productName

        viewLifecycleOwner.lifecycleScope.launch {
            homeVM.getSearchProduct(
                productDataReq
            ).collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setProductAdapter() {
        adapter = ProductPagingAdapter(requireContext(), ElementType.Grid.type)
        binding.rvProducts.adapter = adapter
    }

}