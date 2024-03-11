package com.bitspanindia.groceryapp.ui.mainFragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.DialogHelper
import com.bitspanindia.groceryapp.AppUtils
import com.bitspanindia.groceryapp.AppUtils.showShortToast
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.Constant
import com.bitspanindia.groceryapp.data.DummyData
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.model.Viewtype
import com.bitspanindia.groceryapp.data.model.request.CheckLocalityReq
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.response.MyAddress
import com.bitspanindia.groceryapp.databinding.FragmentHomeBinding
import com.bitspanindia.groceryapp.databinding.LocationEnableBottomSheetBinding
import com.bitspanindia.groceryapp.presentation.adapter.HomeRecyclerAdapter
import com.bitspanindia.groceryapp.presentation.adapter.HomeTopListAdapter
import com.bitspanindia.groceryapp.presentation.adapter.ProductsAdapter
import com.bitspanindia.groceryapp.presentation.viewmodel.AddressViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.CartManageViewModel
import com.bitspanindia.groceryapp.presentation.viewmodel.HomeViewModel
import com.bitspanindia.groceryapp.storage.SharedPreferenceUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var dialogHelper: DialogHelper

    private val homeVM: HomeViewModel by activityViewModels()
    private val cartVM: CartManageViewModel by activityViewModels()
    private val addViewModel: AddressViewModel by activityViewModels()

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lDialog : BottomSheetDialog
    private var isLocationUpdatesStarted = false

    @Inject
    lateinit var pref: SharedPreferenceUtil


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()
        dialogHelper = DialogHelper(mContext, mActivity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        Constant.name = pref.getString(Constant.USER_NAME,"").toString()
        Constant.phoneNo = pref.getString(Constant.PHONE_NUMBER,"").toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (AppUtils.checkGpsStatus(mActivity)&&Constant.userLocation.isEmpty()){
            requestLocationUpdates(false)
        }else{
           if (Constant.userLocation.isEmpty()) showLocationDialog()
        }

        observeAddress()

//        checkLocality()

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
            showManualLocationDialog()
        }

        binding.btnChangeLoc.setOnClickListener {
            showManualLocationDialog()
        }

//        getHomData()

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

        //TODO abhi tempery esko comment kr dea hai bad me enable kr sakte hai
//        binding.otherAppList.adapter = HomeTopListAdapter(mContext, DummyData.homeTopDataList)
//
//        binding.otherAppList.setOnItemClickListener(){adapterView, view, position, id ->
//
//            Toast.makeText(mContext, "Click on item at $position its item id $id", Toast.LENGTH_LONG).show()
////            val intent = Intent(mActivity, JobMainActivity::class.java)
////            startActivity(intent)
//        }


    }


    private fun bindCartTotal() {
        cartVM.cartTotalItem.observe(viewLifecycleOwner) {

            if (cartVM.isCartVisible) {
                val viewHolder = binding.homeRecView.findViewHolderForAdapterPosition(4)  // TODO remove static
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
                Log.d("Rishabh", "cart map ${it.cartItemsMap}")
                var total = 0;
                for (i in it.cartItemsMap) {
                    var count = 0;
                    for (j in it.cartItemsMap[i.key] ?: listOf()) {
                        if (count == 0) {
                            cartVM.countMap[i.key] = mutableMapOf()
                        }
                        count += j.count
                        cartVM.countMap[i.key]!![j.sizeId] =  j.count
                    }
                    total += count
                    cartVM.countMap[i.key]!!["-1"] = count
                }

                Log.d("Rishabh", "count map HF ${cartVM.countMap}")
                getHomData()
                if (total > 0) {
                    cartVM.setCartTotal(total)
                }
                cartVM.setCart(it)


            }
        }
    }

    private fun getHomData(sellerAutoId:String= "",sellerId:String="") {
        val homeDataReq = HomeDataReq("56testing.club",sellerId,sellerAutoId)
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
                    Log.d("Rishabh", "count map HF after minus ${cartVM.countMap}")

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


    private fun checkLocality() {
        dialogHelper.showProgressDialog()

        val checkLocalityReq = CheckLocalityReq()
        checkLocalityReq.latitude = Constant.latitude
        checkLocalityReq.longitude = Constant.longitude

        viewLifecycleOwner.lifecycleScope.launch {
            homeVM.checkLocality(checkLocalityReq).let {
                dialogHelper.hideProgressDialog()
                try {
                    if (it.body()!=null){
                        val data = it.body()

                        if (data?.statusCode==200){
                            Constant.sellerAutoId = data.matchSELLER?.id?:""
                            Constant.sellerId = data.matchSELLER?.sellerid?:""

                            locationVisibility(visibility = View.VISIBLE, visibility2 = View.GONE)
                            getHomData(Constant.sellerAutoId,Constant.sellerId)
                        }else{
                            locationVisibility(visibility = View.GONE, visibility2 = View.VISIBLE)
                        }
                    }else{
                        dialogHelper.showErrorMsgDialog("Something went wrong"){
                            findNavController().popBackStack()
                        }
                    }
                } catch (e: Exception) {
                    dialogHelper.hideProgressDialog()
                    dialogHelper.showErrorMsgDialog("Something went wrong"){
                        findNavController().popBackStack()
                    }
                }
            }
        }
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
        lDialog = BottomSheetDialog(mContext)
        val bindingDialog = LocationEnableBottomSheetBinding.inflate(layoutInflater)
        lDialog.setCancelable(false)
        lDialog.setContentView(bindingDialog.root)

        bindingDialog.btnContinue.setOnClickListener {
            AppUtils.gpsPermission(requireContext(), locationSettingsResultLauncher) {
                requestLocationUpdates(true)
            }
        }

        bindingDialog.clSearchLocation.setOnClickListener {
            lDialog.dismiss()
            showManualLocationDialog()
        }

        lDialog.show()
    }

    private fun showManualLocationDialog() {
        val modalBottomSheet by lazy {
            ChooseLocationBottomSheetFragment()
        }

        modalBottomSheet.show(childFragmentManager, ChooseLocationBottomSheetFragment.TAG)
        modalBottomSheet.isCancelable = false
    }

    private fun requestLocationUpdates(dismissDialog:Boolean) {
        if (isLocationUpdatesStarted) return
        dialogHelper.showProgressDialog()

        locationRequest = LocationRequest.Builder(1000L).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (dismissDialog) lDialog.dismiss()
                locationResult ?: return
                dialogHelper.hideProgressDialog()
                for (location in locationResult.locations) {

                    val address = AppUtils.getAddressFromLocation(mContext,location.latitude,location.longitude)
                    Constant.latitude = location.latitude
                    Constant.longitude = location.longitude
                    Constant.userLocation = address.getAddressLine(0)
                    binding.locAddressTxt.text = address.getAddressLine(0)

                    addViewModel.myAddress.value = MyAddress(latitude = Constant.latitude.toString(), longitude = Constant.longitude.toString(), permanentAdd = address.getAddressLine(0), city = address.locality, country = address.countryName, zipcode = address.postalCode)

                    AppUtils.stopLocationUpdates(fusedLocationClient, locationCallback)
                    isLocationUpdatesStarted = false

                    return
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AppUtils.requestLocationPermissions(mActivity, 1)
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        isLocationUpdatesStarted = true
    }


    private val locationSettingsResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            requestLocationUpdates(false)
        } else {
            showShortToast(requireContext(),"Error for getting current location")
        }
    }

    private fun observeAddress(){
        addViewModel.myAddress.observe(viewLifecycleOwner){address->
            Constant.latitude = address.latitude?.toDouble() ?: 0.0
            Constant.longitude = address.longitude?.toDouble() ?: 0.0
            Constant.userLocation = address.permanentAdd?:""
            binding.locAddressTxt.text = address.permanentAdd
            Log.e("TAG", "observeAddress: ${address.latitude} ${address.longitude}", )
            checkLocality()
        }
    }

    private fun locationVisibility(visibility:Int,visibility2: Int){
        binding.apply {
            otherAppList.visibility = visibility
            rHomeSearch.visibility = visibility
            viewLineOne.visibility = visibility
            homeRecView.visibility = visibility
            AppUtils.cartLayoutVisibility(mActivity,visibility)
            clLocAvailability.visibility = visibility2
        }
    }

}