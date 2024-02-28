package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.EmpExpData
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.employee.AddUpdCareerPrefResp
import com.bitspan.bitsjobkaro.data.models.employee.CareerEmpData
import com.bitspan.bitsjobkaro.data.models.employee.EmpBasicData
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface EmpProffService {

    @GET("p_details")
    suspend fun getEmpProffData(@Query("emp_id") empId: Int): Response<GeneralDataAndMessModel<EmpExpData>>

    @GET("career_pref_emp")
    suspend fun getEmpCarPrefData(@Query("emp_id") empId: Int): Response<GeneralDataAndMessModel<List<CareerEmpData>>>

    @GET("basic_detail_emp")
    suspend fun getEmpBasicDetails(@Query("emp_id") empId: Int): Response<GeneralDataAndMessModel<List<EmpBasicData>>>


    @Multipart
    @POST("emp_career_pref")
    suspend fun saveCareerPreference(
        @Part resume: MultipartBody.Part?,
        @Part jobRole: List<MultipartBody.Part>,
        @Part cities: List<MultipartBody.Part>,
        @Part skills: List<MultipartBody.Part>,
        @Part("emp_id") empId: RequestBody,
        @Part("formId") formId: RequestBody,
        @Part("t_exp_type") expLevel: RequestBody,
        @Part("t_exp_yr") expYr: RequestBody,
        @Part("t_exp_month") expMonth: RequestBody,
        @Part("eng") engLevel: RequestBody,
        @Part("state") state: RequestBody,
        @Part("s_lacs") minSalary: RequestBody,
        @Part("s_thousand") maxSalary: RequestBody,
        @Part("shift_type") prefShift: RequestBody,
        @Part("emp_type") empType: RequestBody,
        @Part("eTYpe") eType: RequestBody,
        @Part("Objective") objective: RequestBody
    ): Response<AddUpdCareerPrefResp>

    @POST("emp_highlight")
    suspend fun getChatAppliedCount(@Body recEmpIdRequest: RecEmpIdRequest): Response<EmpChatAppliedCountResponse>


}