package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.models.chat.AllMsgData
import com.bitspan.bitsjobkaro.databinding.FragmentChatBinding
import com.bitspan.bitsjobkaro.presentation.adapters.ChatAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.ChatViewModel

class PdfFragment : Fragment() {

    private lateinit var mActivity: FragmentActivity
    private lateinit var mContext: Context
    private val chatViewModel: ChatViewModel by viewModels()
//    private val chatArgs: ChatFragmentArgs by navArgs()
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: List<AllMsgData>
    private var recId: Int = 0
    private var empId: Int = 0
    private lateinit var senderType: String
    private val employee = "Employee"
    private val recruiter = "Recruiter"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mActivity = requireActivity()
        mContext = requireContext()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}