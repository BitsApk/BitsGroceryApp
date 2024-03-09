package com.bitspan.bitsjobkaro.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.employee.EmpAppViewedData
import com.bitspan.bitsjobkaro.data.repositories.EmpAppViewedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EmpViewedByViewModel @Inject constructor(private val empAppViewedRepository: EmpAppViewedRepository): ViewModel() {
    suspend fun getViewedByList(empId: Int): Response<GeneralDataAndMessModel<List<EmpAppViewedData>>> = empAppViewedRepository.getViewedByList(empId)
}