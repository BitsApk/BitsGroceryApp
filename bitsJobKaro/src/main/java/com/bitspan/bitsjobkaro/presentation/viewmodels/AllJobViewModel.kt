package com.bitspan.bitsjobkaro.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.AllJobData
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.employee.EmpApplyJobReq
import com.bitspan.bitsjobkaro.data.models.employee.EmpApplyJobResponse
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import com.bitspan.bitsjobkaro.data.repositories.EmployeeJobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class AllJobViewModel @Inject constructor(private val allJobRepository: EmployeeJobRepository) :
    ViewModel() {

    suspend fun getAllJobs(): Response<GeneralDataAndMessModel<List<AllJobData>>> = allJobRepository.getAllJobs()
    suspend fun getAppliedJob(empId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>> = allJobRepository.getAppliedJob(empId)

    suspend fun getSavedJobs(empId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>>  = allJobRepository.getSavedJob(empId)

    suspend fun getSearchedJob(title: String, address: String): Response<List<AllJobData>> =
        allJobRepository.getSearchedJobDetails(title, address)

    suspend fun empApplyJob(empApplyJobReq: EmpApplyJobReq): Response<EmpApplyJobResponse> =
        allJobRepository.empApplyJob(empApplyJobReq)


}