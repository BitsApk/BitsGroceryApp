package com.bitspanindia.groceryapp.datalist

import android.content.Context
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.model.SliderModel
import com.bitspanindia.groceryapp.data.model.request.HomeDataReq
import com.bitspanindia.groceryapp.data.model.request.UpdateProfileReq
import com.bitspanindia.groceryapp.data.model.response.CommonResponse
import com.bitspanindia.groceryapp.data.model.response.GetProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

class CustomList(mContext:Context) {

    val dataListProduct = listOf(
        SliderModel(
            image= R.drawable.lays1
        ),
        SliderModel(
            image= R.drawable.lays2
        ),
        SliderModel(
            image= R.drawable.lays3
        ),
        SliderModel(
            image=  R.drawable.lays4
        ),
    ) // Replace this with your data

    val dataListProduct2 = listOf(
        SliderModel(
            image= R.drawable.lays_american
        ),
        SliderModel(
            image= R.drawable.lays1
        ),
        SliderModel(
            image= R.drawable.tedhe_medhe
        ),
        SliderModel(
            image=  R.drawable.kukure
        ))

}