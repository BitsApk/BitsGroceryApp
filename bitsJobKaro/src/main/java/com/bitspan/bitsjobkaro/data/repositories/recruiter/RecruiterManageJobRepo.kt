package com.bitspan.bitsjobkaro.data.repositories.recruiter

import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.GeneralDataModel
import com.bitspan.bitsjobkaro.data.models.GeneralMessModel
import com.bitspan.bitsjobkaro.data.models.recruiter.PostedJobData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecViewedData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.services.RecruitManageApiService
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

class RecruiterManageJobRepo @Inject constructor(private val recruitManageApiService: RecruitManageApiService) {

    suspend fun getPostedJobList(recId: Int): Response<GeneralDataAndMessModel<List<PostedJobData>>> =
        recruitManageApiService.getPostedJobList(recId)

    suspend fun getSavedCandidates(
        recId: Int,
        jobId: Int?
    ): Response<GeneralDataModel<List<RecruiterEmpData>>> {
        return recruitManageApiService.getSavedCandidates(recId, jobId)
    }

    suspend fun getViewedData(recId: Int): Response<RecViewedData> {
        return recruitManageApiService.getViewedData(recId)
    }

    suspend fun callShortListApi(empId: Int, recId: Int): Response<GeneralMessModel> {
        return recruitManageApiService.callShortListApi(empId, recId)
    }

    suspend fun callPostJobStatusApi(jobId: Int): Response<GeneralMessModel> {
        return recruitManageApiService.callPostJobStatusApi(jobId)
    }

    suspend fun getAppliedEmpData(jobId: Int): Response<GeneralDataAndMessModel<List<RecruiterEmpData>>> {
        return recruitManageApiService.getAppliedEmpData(jobId)
    }
}