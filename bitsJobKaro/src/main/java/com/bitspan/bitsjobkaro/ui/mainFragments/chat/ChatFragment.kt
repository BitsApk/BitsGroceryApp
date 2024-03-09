package com.bitspan.bitsjobkaro.ui.mainFragments.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.data.constants.enums.ChatType
import com.bitspan.bitsjobkaro.data.models.chat.AllMsgData
import com.bitspan.bitsjobkaro.databinding.FragmentChatBinding
import com.bitspan.bitsjobkaro.presentation.adapters.ChatAdapter
import com.bitspan.bitsjobkaro.presentation.adapters.ChatTypes
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var mActivity: FragmentActivity
    private lateinit var mContext: Context
    private val chatViewModel: ChatViewModel by viewModels()
    private val chatArgs: ChatFragmentArgs by navArgs()
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: MutableList<AllMsgData>
    private var recId: Int = 0
    private var empId: Int = 0
    private lateinit var senderType: String
    private val employee = "employee"
    val recruiter = "recruiter"
    private val reqResume = "get_resume"
    private val reqContact = "get_number"
    var recSendMess: Boolean = false
    private var firstTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstTime = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mActivity = requireActivity()
        mContext = requireContext()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (firstTime) {
            senderType = if (userType) employee else recruiter
            if (userType) {
                empId = userId.toInt()
                recId = chatArgs.userToChatId
            } else {
                recId = userId.toInt()
                empId = chatArgs.userToChatId
            }
            getAllMess()
            firstTime = false

        } else {
            val linearLayoutMang = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
            linearLayoutMang.stackFromEnd = true
            binding.chatRecView.layoutManager = linearLayoutMang
            binding.chatRecView.adapter = chatAdapter
        }

        binding.chatNameTxt.text = chatArgs.name

        if (userType) binding.sendResumeTxt.text = "Send Resume"
        else binding.sendResumeTxt.text = "Req Resume"


        binding.sendImg.setOnClickListener {
            if (checkField()) {
                sendMessage(binding.chatBoxEdTxt.text.toString())
            }
        }

        binding.sendResumeTxt.setOnClickListener {
            if (userType) {
                if (recSendMess) sendMessage(ChatTypes.empSendRes)
                else Toast.makeText(mContext, "Can't send resume unless recruiter message you", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(ChatTypes.recReqRes)
            }
        }

        binding.sendContactTxt.setOnClickListener {
            if (userType) {
                if (recSendMess) sendMessage(ChatTypes.empSendCont)
                else Toast.makeText(mContext, "Can't send Contact unless recruiter message you", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(ChatTypes.recSendCont)
            }
        }

        binding.reqContactTxt.setOnClickListener {
            if (userType) {
                if (recSendMess) sendMessage(ChatTypes.empReqCont)
                else Toast.makeText(mContext, "Can't send Contact unless recruiter message you", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(ChatTypes.recReqCont)
            }
        }

        binding.chatBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun getAllMess() {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                chatViewModel.getAllChatMess(recId, 0, empId).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            chatList = it.body()!!.allMsg ?: mutableListOf()
                            for (chat in chatList) {
                                if (chat.createdByRec != "0") {
                                    recSendMess = true
                                    break
                                }
                            }
                            chatAdapter = ChatAdapter(chatList, userType) {type, pos ->
                                when (type) {
                                    ChatType.ViewResume -> {
                                        val directions = ChatFragmentDirections.actionChatFragmentToViewResumeFragment()
                                        findNavController().navigate(directions)
                                    }
                                    ChatType.ViewContact -> {
                                        Log.d("Rishabh", "position out $pos")
                                        getEmpContact(pos)
                                    }
                                    ChatType.SendContact -> {}
                                    ChatType.SendResume -> {}
                                }
                            }
                            val linearLayoutMang = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
                            linearLayoutMang.stackFromEnd = true
                            binding.chatRecView.layoutManager = linearLayoutMang
                            binding.chatRecView.adapter = chatAdapter
                        } else {
                            binding.apply {
                                chatRecView.visibility = View.GONE
                                noLayout.clNodataFound.visibility = View.VISIBLE
                            }
                        }
                    }  else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext, "Some technical error in getting chats"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            CommonUiFunctions.showErrorMsgDialog(
                mContext,
                "Some error in getting chats, Check your internet"
            ) {
                findNavController().popBackStack()
            }
        }
    }

    private fun getEmpContact(pos: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            chatViewModel.getEmpData(empId, reqContact).let {
                if (it.isSuccessful && it.body() != null && it.body()!!.status == 200) {
                    chatAdapter.contactNumEmp = it.body()!!.data?.get(0)?.cContact ?: ""
                    chatAdapter.notifyItemChanged(pos)
                } else {

                }
            }
        }
    }

    private fun sendMessage(message: String) {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                chatViewModel.sendMess(
                    empId = empId,
                    mess = message,
                    recId = recId,
                    senderType = senderType
                ).let {
                    if (it.isSuccessful && it.body() != null) {
                        binding.chatBoxEdTxt.setText("")
                        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        chatList.add(AllMsgData(
                            createdAt = format.format(Calendar.getInstance().time),
                            createdByEm = if (userType) empId.toString() else "0",
                            createdByRec = if (userType) "0" else recId.toString(),
                            msg = message,
                            eeId = empId.toString(), // no need to all below fields
                            recId = recId.toString(),
                            id = it.body()!!.lastMsgId!!.toString(),
                            readByEmp = "1",
                            readByRec = "1",
                            status = "0"
                        ))
                        chatAdapter.notifyItemChanged(chatList.size - 1)
                        binding.chatRecView.smoothScrollToPosition(chatList.size - 1)
                        if (it.body()!!.status != "200") {
                            Toast.makeText(mContext, "Error in sending message, Message not sent", Toast.LENGTH_SHORT).show()
                        }
                    }  else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext, "Some technical error in getting chats"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            CommonUiFunctions.showErrorMsgDialog(
                mContext,
                "Some error in sending mess, Check your internet"
            ) {
                findNavController().popBackStack()
            }
        }
    }

    private fun checkField(): Boolean {
        return if (binding.chatBoxEdTxt.text.toString().isBlank()) {
            binding.chatBoxEdTxt.error = "Mess not valid"
            false
        } else {
            true
        }
    }


}