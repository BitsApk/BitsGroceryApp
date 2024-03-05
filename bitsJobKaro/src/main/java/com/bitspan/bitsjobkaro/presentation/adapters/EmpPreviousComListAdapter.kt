package com.bitspan.bitsjobkaro.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.data.models.employee.AllPreCom
import com.bitspan.bitsjobkaro.databinding.ItemEmpCompanyListBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EmpPreviousComListAdapter(
    private val empList: List<AllPreCom>,
    val callback: ( id:String,pos:Int) -> Any
) : RecyclerView.Adapter<EmpPreviousComListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemEmpCompanyListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AllPreCom, position: Int) {
            binding.empCurrNoticePeriod.visibility = View.GONE
            binding.ivDelete.visibility = View.VISIBLE
            binding.empCurrCompNameTxt.text = item.cName
            binding.empCurrDesignTxt.text = item.pDesig
            binding.empCurrJobDateTxt.text = "${formatedDate(item.dateFrom?:"")} - ${formatedDate(item.dateTo?:"")}"

            binding.ivDelete.setOnClickListener {
                callback(item.id?:"",absoluteAdapterPosition)
            }
        }

        fun formatedDate(inputDate:String):String{

            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date = inputFormat.parse(inputDate)

            // Create a SimpleDateFormat object for formatting the output date
            val outputFormat = SimpleDateFormat("MMM yyyy",Locale.getDefault())

            // Format the Date object to a string in the desired format
            return outputFormat.format(date)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmpCompanyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = empList[position]
        holder.bind(item,position)

    }

    override fun getItemCount(): Int = empList.size
}