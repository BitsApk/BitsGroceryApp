package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.DummyData
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.Viewtype
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.databinding.FragmentHomeBinding
import com.bitspanindia.groceryapp.databinding.LocationEnableBottomSheetBinding
import com.bitspanindia.groceryapp.presentation.adapter.HomeRecyclerAdapter
import com.bitspanindia.groceryapp.presentation.adapter.HomeTopListAdapter
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.HomeViewModel
import com.bitspanindia.groceryapp.storage.SharedPreferenceUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    private val homeVM: HomeViewModel by activityViewModels()
    private val cartVM: CartManageViewModel by activityViewModels()

    @Inject
    lateinit var pref: SharedPreferenceUtil


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()
        Constant.name = pref.getString(Constant.USER_NAME,"").toString()
        Constant.phoneNo = pref.getString(Constant.PHONE_NUMBER,"").toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        showLocationDialog()
//        setProducts()
        binding.profImage.setOnClickListener {
//            cartVM.clearCart()
//            val action = HomeFragmentDirections.actionHomeFragmentToFaceUnlockFragment()
//            val action = HomeFragmentDirections.actionHomeFragmentToSubCategoryFragment("11", "Fruits & Vegetables")
//            findNavController().navigate(action)
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(action)
        }
        getSavedCart()

        binding.rHomeSearch.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalSearchProductFragment()
            )
        }

        binding.markImg.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMapFragment("home")
            )

//            val action = HomeFragmentDirections.actionHomeFragmentToChooseLocationFragment()
//            findNavController().navigate(action)
        }

        getHomData()

        bindCartTotal()

//        val images = listOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4,  R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_1)
//
//        binding.banner.bannerRecView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
//        binding.banner.bannerRecView.adapter = BannerImageAdapter(images)
//
//        val snapHelper = PagerSnapHelper()
//        snapHelper.attachToRecyclerView(binding.banner.bannerRecView)

//        binding.tvProductDetails.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment()
//            findNavController().navigate(action)
//        }

        binding.otherAppList.adapter = HomeTopListAdapter(mContext, DummyData.homeTopDataList)


    }


    private fun bindCartTotal() {
        cartVM.cartTotalItem.observe(viewLifecycleOwner) {

            if (cartVM.isCartVisible) {
                val viewHolder = binding.homeRecView.findViewHolderForAdapterPosition(4)  // TODO
                if (viewHolder is RecyclerView.ViewHolder) {
                    val childRecyclerView = viewHolder.itemView.findViewById<RecyclerView>(R.id.selectedRecView)

                    val adapter = childRecyclerView.adapter as? ProductsAdapter

                    val layoutManager = childRecyclerView.layoutManager
                    if (layoutManager is GridLayoutManager) {
                        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                        adapter?.notifyItemRangeChanged(firstVisiblePosition - 2, 8)
                    }
                }
            }

        }
    }

    private fun getSavedCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            cartVM.getSavedCart().let {
                Log.d("Rishabh", "HF Cart Found before setted ${it.cartItemsMap.size} ${it.cartItemsMap}")
                var total = 0;
                for (i in it.cartItemsMap) {
                    var count = 0;
                    for (j in it.cartItemsMap[i.key] ?: listOf()) {
                        count += j.count
                    }
                    total += count
                    cartVM.countMap[i.key] = count
                }
                getHomData()
                if (total > 0) {
                    cartVM.setCartTotal(total)
                }
                cartVM.setCart(it)


            }
        }
    }

    private fun getHomData() {
        val homeDataReq = HomeDataReq("56testing.club",Constant.userId)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                homeVM.getHomeData(homeDataReq).let {
                    if (it.isSuccessful && it.body() != null) {
                        setHomeAdapter(it.body()!!.viewtypeList)
                    } else {
                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(mContext, "Error two", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setHomeAdapter(viewList: List<Viewtype>?) {
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                mContext,
                R.drawable.recy_item_decorator
            )!!
        )
        binding.homeRecView.addItemDecoration(itemDecorator)
        binding.homeRecView.adapter = HomeRecyclerAdapter(viewList ?: listOf(), mContext, cartVM.countMap, {prod, action ->

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
        },{catId, catName ->
            val action = HomeFragmentDirections.actionHomeFragmentToSubCategoryFragment(catId,catName)
            findNavController().navigate(action)
        })
    }

    private fun setProducts() {

//        binding.homeRecView.adapter = HomeRecyclerAdapter(
//            listOf(
//                HomeData("twoRowProduct", "Recommended Products", DummyData.data),
//                HomeData("mainCategoryGrid", "Famous Category", DummyData.mainCategory),
//                HomeData("oneRowProduct", "How about Snacks", DummyData.data),
//                HomeData("bannerRecView", "Banner", DummyData.bannerData),
//                HomeData("oneRowProduct", "How about Snacks", DummyData.data),
//                HomeData("bannerRecView", "Banner", DummyData.bannerData),
//                HomeData("mainCategoryGrid", "Famous Category", DummyData.mainCategory),
//            ),
//            mContext
//        )

    }

    private fun showLocationDialog() {
        val dialog = BottomSheetDialog(mContext)
//        val view = layoutInflater.inflate(R.layout.location_enable_bottom_sheet, null)
        val bindingDialog = LocationEnableBottomSheetBinding.inflate(layoutInflater)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)

        bindingDialog.clSearchLocation.setOnClickListener {
            dialog.dismiss()
            val action = HomeFragmentDirections.actionHomeFragmentToChooseLocationFragment()
            findNavController().navigate(action)
        }

        dialog.show()
    }

}