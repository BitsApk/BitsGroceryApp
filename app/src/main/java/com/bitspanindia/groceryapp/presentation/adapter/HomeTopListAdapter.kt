package com.bitspanindia.groceryapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.data.model.custom.HomeTopData
import com.bitspanindia.groceryapp.databinding.ItemHomeTopListBinding

class HomeTopListAdapter(
   private val context: Context,
   private val dataList: List<HomeTopData>
) : ArrayAdapter<HomeTopData>(context, R.layout.item_home_top_list) {

   override fun getCount(): Int {
      return dataList.size
   }
   override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      var binding = ItemHomeTopListBinding.inflate(LayoutInflater.from(context), parent, false)
      binding.headingTxt.text = dataList[position].text
      return binding.root
   }
}