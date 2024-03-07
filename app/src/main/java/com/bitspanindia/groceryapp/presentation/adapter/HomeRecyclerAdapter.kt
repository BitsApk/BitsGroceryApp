package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bitspanindia.groceryapp.data.enums.CartAction
import com.bitspanindia.groceryapp.data.enums.ElementType
import com.bitspanindia.groceryapp.data.enums.ViewDesign
import com.bitspanindia.groceryapp.data.model.BannerData
import com.bitspanindia.groceryapp.data.model.Category
import com.bitspanindia.groceryapp.data.model.CategoryData
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.Viewtype
import com.bitspanindia.groceryapp.databinding.ItemBannerBinding
import com.bitspanindia.groceryapp.databinding.ItemHomeTextRecviewLayoutBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class HomeRecyclerAdapter(
    private val sectionList: List<Viewtype>,
    private val context: Context,
    private val countMap: MutableMap<String, MutableMap<String, Int>>,
    private val prodCallback: (prod: ProductData, action: CartAction) -> Any,
    private val callBackCat: (catId:String,catName:String) -> Any
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TextRecViewHolder(val binding: ItemHomeTextRecviewLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(homeData: Viewtype, elementType: ElementType) {
            val designType = when (homeData.designType) {
                "MainCategoryGrid" -> ViewDesign.MainCategoryGrid
                "OneRowProductGrid" -> ViewDesign.OneRowProductGrid
                "BannerRecView" -> ViewDesign.BannerRecView
                "TwoRowProductGrid" -> ViewDesign.TwoRowProductGrid
                else -> ViewDesign.MainCategoryGrid
            }
            when (elementType) {

                ElementType.Category -> {
                    val item = homeData.getDataAs<CategoryData>()
                    binding.selectedField.text = homeData.title
                    binding.selectedRecView.layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
                    binding.selectedRecView.adapter = MainCategoryImageAdapter(item?.get(0)?.category ?: listOf(), context,"homeCat"){catId,catName->
                        callBackCat(catId,catName)
                    }
                }

                ElementType.Products -> {
                    val data = homeData.getDataAs<ProductData>()
                    binding.selectedField.text = homeData.title
                    binding.selectedRecView.layoutManager = when (designType) {
                        ViewDesign.TwoRowProductGrid -> GridLayoutManager(
                            context,
                            2,
                            GridLayoutManager.HORIZONTAL,
                            false
                        )
                        else -> {
                            GridLayoutManager(
                                context,
                                1,
                                GridLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                    binding.selectedRecView.adapter = ProductsAdapter(data ?: mutableListOf(), context, countMap, 0, prodCallback)
                    }
                }
                else -> {}


            }

        }
    }

    inline fun <reified T> Viewtype.getDataAs(): MutableList<T>? {
        val gson = Gson()
        return when (viewtype) {
            "category" -> data.mapNotNull { gson.fromJson(gson.toJsonTree(it), CategoryData::class.java) as? T}.toMutableList()
            "Products" -> data.mapNotNull { gson.fromJson(gson.toJsonTree(it), ProductData::class.java) as? T}.toMutableList()
            "Banner" -> data.mapNotNull { gson.fromJson(gson.toJsonTree(it), BannerData::class.java) as? T}.toMutableList()
            else -> null
        }
    }


    inner class RecyclerViewHolder(val binding: ItemBannerBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(homeData: Viewtype, elementType: ElementType) {

            when (elementType) {
                ElementType.Banner -> {
                    val data = homeData.getDataAs<BannerData>()
                    val snapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(binding.bannerRecView)
                    binding.bannerRecView.adapter = BannerImageAdapter(data ?: listOf(), context)
                }
                ElementType.Category->{

                }
                else -> {}
            }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ElementType.Category.type -> TextRecViewHolder(ItemHomeTextRecviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ElementType.Products.type -> TextRecViewHolder(ItemHomeTextRecviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ElementType.Banner.type -> RecyclerViewHolder(ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//            ElementType.Video.type -> TextRecViewHolder(ItemHomeTextRecviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> TextRecViewHolder(ItemHomeTextRecviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = sectionList[position]
        when (getItemViewType(position)) {
            ElementType.Category.type -> (holder as TextRecViewHolder).bind(item, ElementType.Category)
            ElementType.Products.type -> (holder as TextRecViewHolder).bind(item, ElementType.Products)
            ElementType.Banner.type -> (holder as RecyclerViewHolder).bind(item, ElementType.Banner)
//            ElementType.Video.type -> (holder as TextRecViewHolder).bind(item, ElementType.Video)
        }

    }
    override fun getItemCount(): Int = sectionList.size

    override fun getItemViewType(position: Int): Int {
        return when (sectionList[position].viewtype) {
            "category" -> ElementType.Category.type
            "Products" -> ElementType.Products.type
            "Banner" -> ElementType.Banner.type
            "video_type" -> ElementType.Video.type
            else -> 0
        }

    }
}