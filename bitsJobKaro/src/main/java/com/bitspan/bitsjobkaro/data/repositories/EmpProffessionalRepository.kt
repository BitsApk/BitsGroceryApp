package com.bitspan.bitsjobkaro.data.repositories

import com.bitspan.bitsjobkaro.data.models.EmpExpData
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.employee.AddUpdCareerPrefResp
import com.bitspan.bitsjobkaro.data.models.employee.CareerEmpData
import com.bitspan.bitsjobkaro.data.models.employee.EmpBasicData
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import com.bitspan.bitsjobkaro.data.services.EmpProffService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


class EmpProffessionalRepository @Inject constructor(private val empProffService: EmpProffService) {

    suspend fun getEmpProffData(empId: Int): Response<GeneralDataAndMessModel<EmpExpData>> {
        return withContext(Dispatchers.IO) {
            empProffService.getEmpProffData(empId)
        }
    }

    suspend fun getEmpCarPrefData(empId: Int): Response<GeneralDataAndMessModel<List<CareerEmpData>>> {
        return withContext(Dispatchers.IO) {
            empProffService.getEmpCarPrefData(empId)
        }
    }

    suspend fun getEmpBasicData(empId: Int): Response<GeneralDataAndMessModel<List<EmpBasicData>>> {
        return withContext(Dispatchers.IO) {
            empProffService.getEmpBasicDetails(empId)
        }
    }

    suspend fun saveCareerPreference(
        resume: MultipartBody.Part?, jobRole: List<MultipartBody.Part>, cities: List<MultipartBody.Part>, skills: List<MultipartBody.Part>,
        empId: RequestBody, formId: RequestBody, expLevel: RequestBody, expYr: RequestBody, expMonth: RequestBody,
        engLevel: RequestBody, state: RequestBody, minSalary: RequestBody, maxSalary: RequestBody, prefShift: RequestBody,
        empType: RequestBody, eType: RequestBody, objective: RequestBody
    ): Response<AddUpdCareerPrefResp> {
        return withContext(Dispatchers.IO) {
            empProffService.saveCareerPreference(resume, jobRole,cities,skills,empId,
                formId,expLevel,expYr,expMonth,engLevel,state,minSalary,
                maxSalary,prefShift,empType,eType,objective)
        }
    }

    suspend fun getChatAppliedCount(recEmpIdRequest: RecEmpIdRequest): Response<EmpChatAppliedCountResponse>{
        return empProffService.getChatAppliedCount(recEmpIdRequest)
    }

}