package com.bitspanindia.groceryapp.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bitspanindia.groceryapp.data.model.request.ProductDataReq
import com.bitspanindia.groceryapp.data.model.response.Order
import com.bitspanindia.groceryapp.data.services.ProfileApiService
import java.lang.Exception

class OrderListPagingSource(
    private val profileApiService: ProfileApiService,
    private val productDataReq: ProductDataReq
) : PagingSource<Int, Order>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val page = params.key ?: 1
        return try {
                productDataReq.pageno = page
                setOrders(page)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun setOrders(page: Int): LoadResult.Page<Int, Order> {
        val response =  profileApiService.getOrderList(productDataReq)
        if (response.body()?.statusCode == 200) {
            response.body()?.order.let {
                return LoadResult.Page(
                    it ?: listOf(),
                    if (page == 1) null else page - 1,
                    if (response.body()?.nextpage==0) null else response.body()?.nextpage
                )
            }
        } else {
            throw Exception()
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}