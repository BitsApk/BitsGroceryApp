package com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.GeneralDataModel
import com.bitspan.bitsjobkaro.data.models.GeneralMessModel
import com.bitspan.bitsjobkaro.data.models.recruiter.PostedJobData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecViewedData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.repositories.recruiter.RecruiterManageJobRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

@HiltViewModel
class RecruiterManageViewModel @Inject constructor(private val recruiterManageJobRepo: RecruiterManageJobRepo): ViewModel() {

    suspend fun getPostedJobList(recId: Int): Response<GeneralDataAndMessModel<List<PostedJobData>>> {
        return withContext(Dispatchers.IO) {
            recruiterManageJobRepo.getPostedJobList(recId)
        }
    }

    suspend fun getSavedCandidates(recId: Int, jobId: Int?): Response<GeneralDataModel<List<RecruiterEmpData>>> {
        return withContext(Dispatchers.IO) {
            recruiterManageJobRepo.getSavedCandidates(recId, jobId)
        }
    }

    suspend fun getViewedData(recId: Int): Response<RecViewedData> {
        return withContext(Dispatchers.IO) {
            recruiterManageJobRepo.getViewedData(recId)
        }
    }

    suspend fun callShortListApi(empId: Int, recId: Int): Response<GeneralMessModel> {
        return recruiterManageJobRepo.callShortListApi(empId, recId)
    }

    suspend fun callPostJobStatusApi(jobId: Int): Response<GeneralMessModel> {
        return withContext(Dispatchers.IO) {
            recruiterManageJobRepo.callPostJobStatusApi(jobId)
        }
    }

    suspend fun getAppliedEmpData(jobId: Int): Response<GeneralDataAndMessModel<List<RecruiterEmpData>>> {
        return withContext(Dispatchers.IO) {
            recruiterManageJobRepo.getAppliedEmpData(jobId)
        }
    }
}