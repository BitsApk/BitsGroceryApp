package com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.chat.*
import com.bitspan.bitsjobkaro.data.repositories.recruiter.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) :
    ViewModel() {

    suspend fun sendMess(empId: Int, mess: String, recId: Int, senderType: String): Response<SendMessResponse> {
        return chatRepository.sendMess(empId, mess, recId, senderType)
    }

    suspend fun getAllChatMess(recId: Int, lastMessId: Int, empId: Int): Response<AllChatResponse> {
        return chatRepository.getAllChatMess(recId, lastMessId, empId)
    }

    suspend fun getRecToEmpChatList(recId: Int): Response<RecToEmpChatResp> {
        return chatRepository.getRecToEmpChatList(recId)
    }

    suspend fun getEmpToRecChatList(empId: Int): Response<EmpToRecChatResp> {
        return chatRepository.getEmpToRecChatList(empId)
    }

    suspend fun getEmpData(userId: Int, reqType: String): Response<EmpDataResponse> {
        return chatRepository.getEmpData(userId, reqType)
    }


}