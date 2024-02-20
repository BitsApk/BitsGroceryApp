package com.bitspanindia.groceryapp.data.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bitspanindia.groceryapp.data.model.ProductData
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.services.HomeApiService
import java.lang.Exception

class ProductPagingSource(
    private val homeApiService: HomeApiService,
    private val productDataReq: ProductDataReq,
   private val type: String
) : PagingSource<Int, ProductData>() {

    companion object {
        var productCount : Int? = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductData> {
        val page = params.key ?: 1
        return try {
                productDataReq.pageno = page
            if (type=="subCatProduct"){
                setSubCatProduct(page)
            }else{
                setSearchProduct(page)
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun setSubCatProduct(page: Int): LoadResult.Page<Int, ProductData> {
        val response =  homeApiService.getSubCategoryProducts(productDataReq)
        if (response.body()?.statusCode == 200) {
            productCount = response.body()?.totalProductFound
            Log.e("TAG", "setSubCatProduct: productfound ${productCount}")

            response.body()?.products.let {
                return LoadResult.Page(
                    it ?: listOf(),
                    if (page == 1) null else page - 1,
                    if (response.body()?.nextPage==0) null else response.body()?.nextPage
                )
            }
        } else {
            throw Exception()
        }
    }

    private suspend fun setSearchProduct(page: Int): LoadResult.Page<Int, ProductData> {
        val response =  homeApiService.getSearchProduct(productDataReq)
        if (response.body()?.statusCode == 200) {
            response.body()?.searchProduct.let {
                return LoadResult.Page(
                    it ?: listOf(),
                    if (page == 1) null else page - 1,
                    if (response.body()?.nextPage==0) null else response.body()?.nextPage
                )
            }
        } else {
            throw Exception()
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}