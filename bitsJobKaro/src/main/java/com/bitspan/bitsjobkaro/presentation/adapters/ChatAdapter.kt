package com.bitspan.bitsjobkaro.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.data.constants.enums.ChatType
import com.bitspan.bitsjobkaro.data.models.chat.AllMsgData
import com.bitspan.bitsjobkaro.databinding.ItemChatRecieverBinding
import com.bitspan.bitsjobkaro.databinding.ItemChatSenderBinding
import java.text.SimpleDateFormat
import java.util.*

object ChatTypes {
    const val empSendRes = "emp~send~resume~to~recr"
    const val empSendCont = "emp~send~contact~no~to~recr"
    const val empReqCont = "emp~request~contact~no~from~recr"
    const val recReqRes = "recruiter~request~resume"
    const val recReqCont = "recruiter~request~contact~number"
    const val recSendCont = "recr~send~contact~to~emp"
}

class ChatAdapter(
    private val chatList: List<AllMsgData>,
    private val userType: Boolean,
    private val callBack: (type: ChatType, pos: Int) -> Any,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contactNumEmp: String = ""
    var contactNumRec: String = ""

    inner class SenderViewHolder(val sendBinding: ItemChatSenderBinding) :
        RecyclerView.ViewHolder(sendBinding.root) {
        fun bind(chatData: AllMsgData) {

            when (chatData.msg) {
                // Case if employee is sender
                ChatTypes.empSendRes -> {
                    // Always an employee
                    setMess("Your resume has been sent ")
                    sendVisibility(View.GONE, View.VISIBLE)
                }

                ChatTypes.empSendCont -> {
                    // Always an employee
                    setMess("Your contact details has been sent ")
                    sendVisibility(View.GONE, View.VISIBLE)
                }

                ChatTypes.empReqCont -> {
                    // Always an employee
                    setMess("You requested for Contact ")
                    sendVisibility(View.GONE, View.VISIBLE)
                }
                // Case if recruiter is sender
                ChatTypes.recSendCont -> {
                    setMess("Your contact details has been sent ")
                    sendVisibility(View.GONE, View.VISIBLE)
                }

                ChatTypes.recReqRes -> {
                    setMess("You requested for Resume ")
                    sendVisibility(View.GONE, View.VISIBLE)
                }

                ChatTypes.recReqCont -> {
                    setMess("You requested for Contact ")
                    sendVisibility(View.GONE, View.VISIBLE)
                }

                else -> {
                    sendBinding.itemChatMessage.text = chatData.msg
                    sendBinding.itemChatTime.text =
                        formatTime(chatData.createdAt?.split(" ")?.get(1))
                    sendVisibility(View.VISIBLE, View.GONE)
                }
            }
        }

        private fun sendVisibility(messVis: Int, specialMessVis: Int) {
            sendBinding.itemChatMessage.visibility = messVis
            sendBinding.itemChatTime.visibility = messVis
            sendBinding.specialChatReqTxt.visibility = specialMessVis
        }

        private fun setMess(mess: String) {
            sendBinding.specialChatReqTxt.text = mess
        }

        private fun formatTime(inputTime: String?): String {
            val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

            val date = inputFormat.parse(inputTime ?: "")
            return outputFormat.format(date ?: "")
        }
    }

    inner class ReceiverViewHolder(val recBinding: ItemChatRecieverBinding) :
        RecyclerView.ViewHolder(recBinding.root) {
        fun bind(chatData: AllMsgData) {

            recBinding.reqResumeLay.visibility = View.GONE
            recBinding.progressBar.visibility = View.GONE

            when (chatData.msg) {
                ChatTypes.empSendRes -> {
                    // Always for recruiter
                    recVisibility(View.GONE, View.GONE)
                    recBinding.reqResumeLay.visibility = View.VISIBLE
                    setDataForRec("Resume has been received ", "Open")
                    recBinding.acceptTxt.setOnClickListener {
                        callBack(ChatType.ViewResume, -1)
                    }
                }

                ChatTypes.empSendCont -> {
                    // Always for recruiter
                    recVisibility(View.GONE, View.VISIBLE)
                    recBinding.specialChatReqTxt.text =
                        if (contactNumEmp.isEmpty()) "Employee contact received: View? "
                        else "Employee contact received: $contactNumEmp "
                    recBinding.specialChatReqTxt.setOnClickListener {
                        if (contactNumEmp.isEmpty()) {
                            recBinding.specialChatReqTxt.text =
                                "Employee contact received: View?     "
                            Log.d("Rishabh", "Pos inside adapter: $absoluteAdapterPosition")
                            callBack(ChatType.ViewContact, absoluteAdapterPosition)
                            recBinding.progressBar.visibility = View.VISIBLE
                        } else {
                            recBinding.specialChatReqTxt.text =
                                "Employee contact received: $contactNumEmp "
                        }
                    }
                }

                ChatTypes.empReqCont -> {
                    // Always for recruiter
                    recVisibility(View.GONE, View.GONE)
                    recBinding.reqResumeLay.visibility = View.VISIBLE
                    setDataForRec("Employee ask for contact ", "Send")
                    recBinding.acceptTxt.setOnClickListener {
                        callBack(ChatType.ViewContact, -1)
                    }
                }

                ChatTypes.recSendCont -> {
                    // Always for employee
                    recVisibility(View.GONE, View.VISIBLE)
                    recBinding.specialChatReqTxt.text = "Recruiter contact received: View? "
                    recBinding.specialChatReqTxt.setOnClickListener {
                        if (contactNumRec.isEmpty()) {
                            Log.d("Rishabh", "Pos inside rec adapter: $absoluteAdapterPosition")
                            callBack(ChatType.ViewContact, absoluteAdapterPosition)
                            recBinding.specialChatReqTxt.text =
                                "Recruiter contact received: View?     "
                            recBinding.progressBar.visibility = View.VISIBLE
                        } else {
                            recBinding.specialChatReqTxt.text =
                                "Employee contact received: $contactNumRec "
                        }
                    }
                }

                ChatTypes.recReqRes -> {
                    // Always for employee
                    recVisibility(View.GONE, View.GONE)
                    recBinding.reqResumeLay.visibility = View.VISIBLE
                    setDataForRec("Recruiter asked for resume ", "Send")
                    recBinding.acceptTxt.setOnClickListener {
                        callBack(ChatType.SendResume, -1)
                    }
                }

                ChatTypes.recReqCont -> {
                    // Always for employee
                    recVisibility(View.GONE, View.GONE)
                    recBinding.reqResumeLay.visibility = View.VISIBLE
                    setDataForRec("Employee ask for contact ", "Send")
                    recBinding.acceptTxt.setOnClickListener {
                        callBack(ChatType.SendContact, -1)
                    }
                }

                else -> {
                    recVisibility(View.VISIBLE, View.GONE)
                    recBinding.itemChatMessage.text = chatData.msg
                    recBinding.itemChatTime.text =
                        formatTime(chatData.createdAt?.split(" ")?.get(1))
                }
            }
        }

        private fun setDataForRec(mess: String, acceptTxt: String) {
            recBinding.resumeReqTxt.text = mess
            recBinding.acceptTxt.text = acceptTxt
        }


        private fun recVisibility(messVis: Int, specialMessVis: Int) {
            recBinding.itemChatMessage.visibility = messVis
            recBinding.itemChatTime.visibility = messVis
            recBinding.specialChatReqTxt.visibility = specialMessVis
        }


        private fun setMess(mess: String) {
            recBinding.specialChatReqTxt.text = mess
        }

        private fun formatTime(inputTime: String?): String {
            val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

            val date = inputFormat.parse(inputTime ?: "")
            return outputFormat.format(date ?: "")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == 0) {
            val sendBinding =
                ItemChatSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SenderViewHolder(sendBinding)
        } else {
            val recBinding =
                ItemChatRecieverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceiverViewHolder(recBinding)
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatData = chatList[position]
        if (getItemViewType(position) == 0) {
            (holder as SenderViewHolder).bind(chatData)
        } else {
            (holder as ReceiverViewHolder).bind(chatData)
        }

    }

    override fun getItemCount(): Int = chatList.size


    override fun getItemViewType(position: Int): Int {
        val chatItem = chatList[position]
        return if (userType) {
            if (chatItem.createdByEm != "0") {
                0   // 0 is for sender
            } else {
                1
            }
        } else {
            if (chatItem.createdByEm != "0") {
                1   // 1 is for sender
            } else {
                0
            }
        }
    }
}