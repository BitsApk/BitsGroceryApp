package com.bitspan.bitsjobkaro.ui.mainFragments.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions.showErrorMsgDialog
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.databinding.FragmentChatCompanyBinding
import com.bitspan.bitsjobkaro.presentation.adapters.ChatUserAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatCompanyFragment : Fragment() {

    private lateinit var mActivity: FragmentActivity
    private lateinit var mContext: Context
    private lateinit var binding: FragmentChatCompanyBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatUserList: List<Any>
    private lateinit var chatUserAdapter: ChatUserAdapter
    private var firstTime: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstTime = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mActivity = requireActivity()
        mContext = requireContext()

        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.VISIBLE)
        binding = FragmentChatCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (firstTime) {
            if (userType) {
                getRecruitersList()
            } else {
                getEmployeeList()
            }
            firstTime = false
        } else {
            binding.chatUserRecView.adapter = chatUserAdapter
        }

    }

    private fun getEmployeeList() {
        Log.d("Rishabh", "User Id: ${userId.toInt()} and userType: $userType")
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                chatViewModel.getRecToEmpChatList(userId.toInt()).let {

                    Log.d("Rishabh", "User Id: sdfd $userId and userType: $userType")
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Log.d("Rishabh", "User Id sdfd : $userId and userType: $userType")
                            chatUserList = it.body()!!.data ?: listOf()
                            chatUserAdapter = ChatUserAdapter(chatUserList, true) {id, name ->
                                navigateToChatFrag(id.toInt(), name)
                            }
                            binding.chatUserRecView.adapter = chatUserAdapter
                        } else {
                            binding.apply {
                                chatUserRecView.visibility = View.GONE
                                chatTxt.visibility = View.GONE
                                noLayout.clNodataFound.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        showErrorMsgDialog(mContext, it.body()?.mess ?: "Some technical error in getting list") {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            showErrorMsgDialog(mContext, "Some error in getting list, Check your internet") {
                findNavController().popBackStack()
            }
        }
    }

    private fun navigateToChatFrag(id: Int, name: String) {
        val direction =
            ChatCompanyFragmentDirections.actionChatCompanyFragmentToChatFragment(id, name)
        findNavController().navigate(direction)
    }

    private fun getRecruitersList() {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                chatViewModel.getEmpToRecChatList(userId.toInt()).let {
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            chatUserList = it.body()!!.data ?: listOf()
                            chatUserAdapter = ChatUserAdapter(chatUserList, false) {id, name ->
                                navigateToChatFrag(id.toInt(), name)
                            }
                            binding.chatUserRecView.adapter = chatUserAdapter
                        } else {
                            binding.apply {
                                chatUserRecView.visibility = View.GONE
                                chatTxt.visibility = View.GONE
                                noLayout.clNodataFound.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        showErrorMsgDialog(mContext, it.body()?.mess ?: "Some technical error in getting list") {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            showErrorMsgDialog(mContext, "Some error in getting list, Check your internet") {
                findNavController().popBackStack()
            }
        }
    }
}