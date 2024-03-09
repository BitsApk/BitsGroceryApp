package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.GeneralDataModel
import com.bitspan.bitsjobkaro.data.models.GeneralMessModel
import com.bitspan.bitsjobkaro.data.models.recruiter.PostedJobData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecViewedData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecruitManageApiService {

    @GET("previousjobdetail")
    suspend fun getPostedJobList(@Query("rec_id") recId: Int): Response<GeneralDataAndMessModel<List<PostedJobData>>>

    @GET("shortlisted_by_rec")
    suspend fun getSavedCandidates(
        @Query("rec_id") recId: Int,
        @Query("job_id") jobId: Int?
    ): Response<GeneralDataModel<List<RecruiterEmpData>>>

    @GET("rec_viewed_by_detail")
    suspend fun getViewedData(
        @Query("rec_id") recId: Int
    ): Response<RecViewedData>

    @GET("rec_shortList_emp")
    suspend fun callShortListApi(
        @Query("emp_id") empId: Int,
        @Query("rec_id") recId: Int
    ): Response<GeneralMessModel>

    @GET("active_deactive_job")
    suspend fun callPostJobStatusApi(
        @Query("job_id") jobId: Int
    ): Response<GeneralMessModel>

    @GET("applied_candidate/{job_id}")
    suspend fun getAppliedEmpData(
        @Path("job_id") jobId: Int
    ): Response<GeneralDataAndMessModel<List<RecruiterEmpData>>>
}