package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.*
import com.bitspan.bitsjobkaro.data.models.employee.EmpAppViewedData
import com.bitspan.bitsjobkaro.data.models.employee.EmpApplyJobReq
import com.bitspan.bitsjobkaro.data.models.employee.EmpApplyJobResponse
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReferenceApiService {
    @GET("all_jobs")
    suspend fun getAllJobList(): Response<GeneralDataAndMessModel<List<AllJobData>>>

    @GET("all_jobs")
    suspend fun getJobDetails(@Query("job_id") jobId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>>

    @GET("all_jobs")
    suspend fun getSearchedJobDetails(
        @Query("title") title: String, @Query("adress") address: String
    ): Response<List<AllJobData>>

    @GET("appliedjob")
    suspend fun getAppliedJobList(@Query("emp_id") empId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>>

    @GET("saved_jobs")
    suspend fun getSavedJobList(@Query("emp_id") empId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>>

    @GET("saved_jobs")
    suspend fun getViewedByList(@Query("emp_id") empId: Int): Response<GeneralDataAndMessModel<List<EmpAppViewedData>>>

    @POST("apply_job")
    suspend fun empApplyJob(@Body empApplyJobReq: EmpApplyJobReq): Response<EmpApplyJobResponse>

}