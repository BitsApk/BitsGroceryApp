package com.bitspan.bitsjobkaro.data.repositories.recruiter

import com.bitspan.bitsjobkaro.data.models.chat.*
import com.bitspan.bitsjobkaro.data.services.ChatApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Field
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatApiService: ChatApiService) {

    suspend fun sendMess(empId: Int, mess: String, recId: Int, senderType: String): Response<SendMessResponse>{
        return withContext(Dispatchers.IO){
            chatApiService.sendMessage(empId, mess, recId, senderType)
        }
    }

    suspend fun getAllChatMess(recId: Int, lastMessId: Int, empId: Int): Response<AllChatResponse> {
        return withContext(Dispatchers.IO){
            chatApiService.getAllChatMess(recId, lastMessId, empId)
        }
    }

    suspend fun getRecToEmpChatList(recId: Int): Response<RecToEmpChatResp> {
        return withContext(Dispatchers.IO){
            chatApiService.getRecToEmpChatList(recId)
        }
    }

    suspend fun getEmpToRecChatList(empId: Int): Response<EmpToRecChatResp> {
        return withContext(Dispatchers.IO){
            chatApiService.getEmpToRecChatList(empId)
        }
    }

    suspend fun getEmpData(userId: Int, reqType: String,
    ): Response<EmpDataResponse> {
        return withContext(Dispatchers.IO){
            chatApiService.getEmpData(userId, reqType)
        }
    }


}