package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.*
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.AllShortlistedEmpResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.PostJobBody
import com.bitspan.bitsjobkaro.data.models.recruiter.RecProfileData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface RecruiterApiService {

    @GET("employee_details")
    suspend fun getEmpList(@Query("job_id") jobId: Int?): Response<GeneralDataModel<List<RecruiterEmpData>>>

    @GET("aboutcompany")
    suspend fun getRecProfileData(@Query("rec_id") recId: Int): Response<GeneralDataAndMessModel<List<RecProfileData>>>

    @GET("career_pref_emp")
    suspend fun getEmpCareerPref(@Query("emp_id") empId: Int): Response<GeneralDataAndMessModel<List<EmpCareerPrefeData>>>

    @GET("p_details")
    suspend fun getEmpExpData(@Query("emp_id") empId: Int): Response<GeneralDataAndMessModel<EmpExpData>>

    @GET("employee_details")
    suspend fun getEmpBasicData(@Query("emp_id") empId: Int): Response<GeneralDataModel<List<RecruiterEmpData>>>

    @GET("all_db_states")
    suspend fun getStateList(): Response<List<StateData>>

    @GET("all_db_district")
    suspend fun getDistrictList(): Response<List<DistrictData>>

    @POST("post_job")
    suspend fun postJob(@Body postJobBody: PostJobBody): Response<GeneralMessModel>

    @FormUrlEncoded
    @POST("all_shortlisted")
    suspend fun getAllShortlisted(
        @Field  ("rec_id") recId: Int
    ): Response<AllShortlistedEmpResponse>


    @POST("rec_highlight")
    suspend fun getRecProfileCount(@Body recEmpIdRequest: RecEmpIdRequest): Response<EmpChatAppliedCountResponse>





}