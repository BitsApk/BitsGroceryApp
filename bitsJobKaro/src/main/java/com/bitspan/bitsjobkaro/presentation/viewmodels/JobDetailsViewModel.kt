package com.bitspan.bitsjobkaro.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.AllJobData
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.repositories.EmployeeJobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class JobDetailsViewModel @Inject constructor(private val allJobRepository: EmployeeJobRepository): ViewModel() {

    suspend fun getJobDetails(jobId: Int): Response<GeneralDataAndMessModel<List<AllJobData>>> {
        return withContext(Dispatchers.IO){
            allJobRepository.getJobDetails(jobId)
        }
    }
}