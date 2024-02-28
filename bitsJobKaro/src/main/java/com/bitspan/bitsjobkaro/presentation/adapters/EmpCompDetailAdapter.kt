package com.bitspan.bitsjobkaro.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.models.employee.AllPreCom
import com.bitspan.bitsjobkaro.databinding.ItemEmpCompanyListBinding

class EmpCompDetailAdapter(val prevCompList: List<AllPreCom>, val context: Context): RecyclerView.Adapter<EmpCompDetailAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemEmpCompanyListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: AllPreCom) {
            binding.apply {
                empCurrCompNameTxt.text = listItem.cName
                empCurrDesignTxt.text = listItem.pDesig
                empCurrJobDateTxt.text = context.getString(R.string.date_format, listItem.dateFrom, listItem.dateTo)
                empCurrNoticePeriod.visibility = View.GONE
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmpCompanyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = prevCompList[position] ?: AllPreCom()
        holder.bind(listItem)
    }

    override fun getItemCount(): Int = prevCompList.size
}