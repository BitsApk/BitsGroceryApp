package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

    @POST("employee_login")
    suspend fun getEmpLoginData(@Query("email") email: String, @Query("pass") pass: String): Response<LoginData>

    @POST("emp_Register")
    suspend fun getEmpRegisterData(@Body empRegisterBody: RegisterBody): Response<GeneralDataAndMessModel<RegisterResponseBody>>

    @POST("recruiter_login")
    suspend fun doRecruitLogin(@Query("email") email: String, @Query("pass") pass: String): Response<LoginData>

    @POST("rec_register")
    suspend fun doRecruitRegister(@Body recRegisterBody: RegisterBody): Response<GeneralDataAndMessModel<RegisterResponseBody>>




}