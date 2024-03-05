package com.bitspan.bitsjobkaro.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.NewPDetail
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.employee.EmpBasicData
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import com.bitspan.bitsjobkaro.data.repositories.EmpProffessionalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject
@HiltViewModel
class EmpProffessionalViewModel @Inject constructor(private val empProffessionalRepository: EmpProffessionalRepository): ViewModel() {

    var basicDetails: EmpBasicData = EmpBasicData()
    var professDetails: NewPDetail = NewPDetail()
    suspend fun getEmpProffData(empId: Int) = empProffessionalRepository.getEmpProffData(empId)
    suspend fun getEmpBasicData(empId: Int) = empProffessionalRepository.getEmpBasicData(empId)

    suspend fun getChatAppliedCount(recEmpIdRequest: RecEmpIdRequest): Response<EmpChatAppliedCountResponse> =
        empProffessionalRepository.getChatAppliedCount(recEmpIdRequest)

}