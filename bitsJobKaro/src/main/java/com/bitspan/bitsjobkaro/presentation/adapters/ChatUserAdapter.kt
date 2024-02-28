package com.bitspan.bitsjobkaro.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.data.models.chat.EmpChatData
import com.bitspan.bitsjobkaro.data.models.chat.RecChatData
import com.bitspan.bitsjobkaro.databinding.ItemChatCompanyListBinding

class ChatUserAdapter(
    private val chatUserList: List<Any>,
    private val isEmployee: Boolean,
    private val callback: (id: String, name: String) -> Any
) :
    RecyclerView.Adapter<ChatUserAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemChatCompanyListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Any) {
            var id = ""
            var name = ""
            if (isEmployee) {
                val listItem = item as EmpChatData
                binding.apply {
                    itemChatName.text = listItem.cName
                    itemChatDesTxt.text = listItem.jobRole
                    itemChatCompTime.text = CommonUiFunctions.getDateMonthName(listItem.logInTime?.split(" ") ?.get(0))
                }
                id = listItem.id ?: ""
                name = listItem.cName ?: ""
            } else {
                val listItem = item as RecChatData
                binding.apply {
                    itemChatName.text = listItem.cName
                    itemChatDesTxt.visibility = View.GONE
                    itemChatCompTime.text = CommonUiFunctions.getDateMonthName(listItem.logInTime?.split(" ") ?.get(0))
                }
                id = listItem.id ?: ""
                name = listItem.cName ?: ""
            }
            binding.itemChatCompRootLayout.setOnClickListener {
                callback(id, name)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemChatCompanyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chatUserList[position]
        holder.bind(item)

    }
    override fun getItemCount(): Int = chatUserList.size


}