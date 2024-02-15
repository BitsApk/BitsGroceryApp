package com.bitspanindia.groceryapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
//import com.bitspanindia.groceryapp.data.enums.ViewTypes
//import com.bitspanindia.groceryapp.data.model.HomeData
import com.bitspanindia.groceryapp.databinding.ItemBannerBinding
import com.bitspanindia.groceryapp.databinding.ItemHomeTextRecviewLayoutBinding

//class HomeRecyclerAdapter(
//    private val sectionList: List<HomeData>,
//    private val context: Context,
//    private val callBack:()->Any
//): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//
//
//    inner class ViewHolder(val binding: ItemHomeTextRecviewLayoutBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(homeData: HomeData, viewType: ViewTypes) {
//
//            when (viewType) {
//                ViewTypes.TwoRowProductGrid -> {
//                    val data = (homeData.data as List<*>)
//                    binding.selectedField.text = homeData.fieldHeading
//                    binding.selectedRecView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
//                    binding.selectedRecView.adapter = ProductsAdapter(data)
//                }
//
//                ViewTypes.OneRowProductGrid -> {
//                    val data = (homeData.data as List<*>)
//                    binding.selectedField.text = homeData.fieldHeading
//                    binding.selectedRecView.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
//                    binding.selectedRecView.adapter = ProductsAdapter(data)
//                }
//
//                ViewTypes.MainCategoryGrid -> {
//                    val data = (homeData.data as List<*>)
//                    binding.selectedField.text = homeData.fieldHeading
//                    binding.selectedRecView.layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
//                    binding.selectedRecView.adapter = MainCategoryImageAdapter(data)
//                }
//                else -> {}
//
//
//            }
//
//        }
//    }
//
//    inner class RecyclerViewHolder(val binding: ItemBannerBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(homeData: HomeData, viewType: ViewTypes) {
//
//            when (viewType) {
//                ViewTypes.BannerRecView -> {
//                    val data = (homeData.data as List<*>)
//                    val snapHelper = PagerSnapHelper()
//                    snapHelper.attachToRecyclerView(binding.bannerRecView)
//                    binding.bannerRecView.adapter = BannerImageAdapter(data)
//                }
//                else -> {}
//            }
//
//        }
//    }
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            ViewTypes.TwoRowProductGrid.type -> ViewHolder(ItemHomeTextRecviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//            ViewTypes.MainCategoryGrid.type -> ViewHolder(ItemHomeTextRecviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//            ViewTypes.BannerRecView.type -> RecyclerViewHolder(ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//            ViewTypes.OneRowProductGrid.type -> ViewHolder(ItemHomeTextRecviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//            else -> ViewHolder(ItemHomeTextRecviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//        }
//
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val item = sectionList[position]
//        when (getItemViewType(position)) {
//            ViewTypes.TwoRowProductGrid.type -> (holder as ViewHolder).bind(item, ViewTypes.TwoRowProductGrid)
//            ViewTypes.MainCategoryGrid.type -> (holder as ViewHolder).bind(item, ViewTypes.MainCategoryGrid)
//            ViewTypes.BannerRecView.type -> (holder as RecyclerViewHolder).bind(item, ViewTypes.BannerRecView)
//            ViewTypes.OneRowProductGrid.type -> (holder as ViewHolder).bind(item, ViewTypes.OneRowProductGrid)
//        }
//
//    }
//    override fun getItemCount(): Int = sectionList.size
//
//    override fun getItemViewType(position: Int): Int {
//        return when (sectionList[position].viewType) {
//            "twoRowProduct" -> ViewTypes.TwoRowProductGrid.type
//            "mainCategoryGrid" -> ViewTypes.MainCategoryGrid.type
//            "bannerRecView" -> ViewTypes.BannerRecView.type
//            "oneRowProduct" -> ViewTypes.OneRowProductGrid.type
//            else -> 0
//        }
//
//    }
//}