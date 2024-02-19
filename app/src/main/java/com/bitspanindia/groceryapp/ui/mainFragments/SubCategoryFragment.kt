package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.data.model.request.CommonDataReq
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.databinding.FragmentSubCategoryBinding
import com.bitspanindia.groceryapp.presentation.adapter.MainCategoryImageAdapter
import com.bitspanindia.groceryapp.presentation.adapter.ProductPagingAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.HomeViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class SubCategoryFragment : Fragment() {
    private lateinit var binding:FragmentSubCategoryBinding
    private lateinit var mContext:Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var adapter: ProductPagingAdapter
    private val args:SubCategoryFragmentArgs by navArgs()
    private var subCatId = ""

    private val homeVM: HomeViewModel by activityViewModels()
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

    }

    private fun getSubCatData() {
        startShimmer(binding.shimmer2,binding.rvProducts)
        startShimmer(binding.shimmer,binding.rvCategory)
        val commonDataReq = CommonDataReq(categoryId = args.catId)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                homeVM.getSubCatList(commonDataReq).let {
                    stopShimmer(binding.shimmer,binding.rvProducts)
                    stopShimmer(binding.shimmer2,binding.rvCategory)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()?.statusCode==200){
                            val data = it.body()?.subCategory
                            subCatId = data?.get(0)?.id?:""
                            setProducts()
                            binding.rvCategory.adapter = MainCategoryImageAdapter(data?: listOf(),mContext,"subCatPage"){id,name->
                                subCatId = id
                                setProducts()
                            }
                        }else{
                            findNavController().popBackStack()
                            Toast.makeText(mContext,"Error",Toast.LENGTH_SHORT).show()
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
        setProductAdapter()
        getProductList()

        adapter.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading) {
//                binding.noData.clNoDataFound.visibility = View.VISIBLE
                stopShimmer(binding.shimmer,binding.rvProducts)
            } else if (it.source.refresh is LoadState.Error) {
//                binding.noData.clNoDataFound.setBackgroundColor(mContext.getColor(R.color.white))
//                binding.noData.clNoDataFound.visibility = View.VISIBLE
                stopShimmer(binding.shimmer,binding.rvProducts)
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

    private fun getProductList() {
        val productDataReq = ProductDataReq()
        startShimmer(binding.shimmer,binding.rvProducts)
        binding.rvProducts.visibility = View.VISIBLE
//        binding.noData.clNoDataFound.visibility = View.GONE
        productDataReq.userId = Constant.userId
        productDataReq.pageno = 1
        productDataReq.subcategoryId = subCatId

        viewLifecycleOwner.lifecycleScope.launch {
            homeVM.getSubCatProducts(
                productDataReq
            ).collect {
                binding.tvProductsCount.text = getString(R.string.two_str,adapter.itemCount.toString()," Items")
                adapter.submitData(it)
            }
        }
    }

    private fun setProductAdapter() {
        adapter = ProductPagingAdapter(requireContext(), ElementType.Grid.type)
        binding.rvProducts.adapter = adapter
    }

}