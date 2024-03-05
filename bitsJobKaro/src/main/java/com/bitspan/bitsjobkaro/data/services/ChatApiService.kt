package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.*
import com.bitspan.bitsjobkaro.data.models.chat.*
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChatApiService {

    @FormUrlEncoded
    @POST("sendMsg")
    suspend fun sendMessage(
        @Field ("emp_id") empId: Int,
        @Field ("msg") mess: String,
        @Field ("rec_id") recId: Int,
        @Field ("sender_type") senderType: String
        ): Response<SendMessResponse>

    @FormUrlEncoded
    @POST("allNewMsg")
    suspend fun getAllChatMess(
        @Field ("rec_id_clicked") recId: Int,
        @Field ("last_msg_id") lastMessId: Int,
        @Field ("emp_id") empId: Int
        ): Response<AllChatResponse>

    @FormUrlEncoded
    @POST("rec_chat_emplyee")
    suspend fun getRecToEmpChatList(
        @Field ("rec_id") recId: Int,
        ): Response<RecToEmpChatResp>

    @FormUrlEncoded
    @POST("emp_chat_rec")
    suspend fun getEmpToRecChatList(
        @Field ("emp_id") empId: Int,
        ): Response<EmpToRecChatResp>

    @FormUrlEncoded
    @POST("get_emp_data")
    suspend fun getEmpData(
        @Field ("user_id") userId: Int,
        @Field ("req_type") reqType: String,
        ): Response<EmpDataResponse>
}