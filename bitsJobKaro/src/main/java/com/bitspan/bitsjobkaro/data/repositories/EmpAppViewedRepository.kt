package com.bitspan.bitsjobkaro.data.repositories

import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.employee.EmpAppViewedData
import com.bitspan.bitsjobkaro.data.services.ReferenceApiService
import retrofit2.Response
import javax.inject.Inject

class EmpAppViewedRepository @Inject constructor(private val referenceApiService: ReferenceApiService) {
    suspend fun getViewedByList(empId: Int): Response<GeneralDataAndMessModel<List<EmpAppViewedData>>>{
        return referenceApiService.getViewedByList(empId)
    }

}