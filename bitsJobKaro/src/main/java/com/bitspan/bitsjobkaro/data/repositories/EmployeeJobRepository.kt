package com.bitspan.bitsjobkaro.data.repositories

import com.bitspan.bitsjobkaro.data.models.AllJobData
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.employee.EmpApplyJobReq
import com.bitspan.bitsjobkaro.data.models.employee.EmpApplyJobResponse
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import com.bitspan.bitsjobkaro.data.services.ReferenceApiService
import retrofit2.Response
import javax.inject.Inject

class EmployeeJobRepository  @Inject constructor(private val referenceApiService: ReferenceApiService){
    suspend fun getAllJobs(): Response<GeneralDataAndMessModel<List<AllJobData>>> {
        return referenceApiService.getAllJobList()
    }

    suspend fun getAppliedJob(empId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>> {
        return referenceApiService.getAppliedJobList(empId)
    }

    suspend fun getSavedJob(empId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>> {
        return referenceApiService.getSavedJobList(empId)
    }


    suspend fun getJobDetails(jobId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>>{
        return referenceApiService.getJobDetails(jobId)
    }

    suspend fun getSearchedJobDetails(title: String, address: String): Response<List<AllJobData>>{
        return referenceApiService.getSearchedJobDetails(title, address)
    }

    suspend fun empApplyJob(empApplyJobReq: EmpApplyJobReq): Response<EmpApplyJobResponse>{
        return referenceApiService.empApplyJob(empApplyJobReq)
    }


}