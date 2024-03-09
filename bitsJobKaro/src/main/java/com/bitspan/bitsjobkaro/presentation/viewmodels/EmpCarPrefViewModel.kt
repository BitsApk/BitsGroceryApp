package com.bitspan.bitsjobkaro.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.custom.EmpCareerPrefContainer
import com.bitspan.bitsjobkaro.data.models.employee.AddUpdCareerPrefResp
import com.bitspan.bitsjobkaro.data.repositories.EmpProffessionalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class EmpCarPrefViewModel @Inject constructor(private val empProffRepository: EmpProffessionalRepository) :
    ViewModel() {

    val careerPrefContainer: EmpCareerPrefContainer = EmpCareerPrefContainer()

    suspend fun getEmpCarPrefData(empId: Int) = empProffRepository.getEmpCarPrefData(empId)

    suspend fun getEmpBasicData(empId: Int) = empProffRepository.getEmpBasicData(empId)

    suspend fun saveCareerPreference(
        resume: MultipartBody.Part?,
        jobRole: List<MultipartBody.Part>,
        cities: List<MultipartBody.Part>,
        skills: List<MultipartBody.Part>,
        empId: RequestBody,
        formId: RequestBody,
        expLevel: RequestBody,
        expYr: RequestBody,
        expMonth: RequestBody,
        engLevel: RequestBody,
        state: RequestBody,
        minSalary: RequestBody,
        maxSalary: RequestBody,
        prefShift: RequestBody,
        empType: RequestBody,
        eType: RequestBody,
        objective: RequestBody
    ): Response<AddUpdCareerPrefResp> {
        return empProffRepository.saveCareerPreference(
            resume, jobRole, cities, skills, empId,
            formId, expLevel, expYr, expMonth, engLevel, state, minSalary,
            maxSalary, prefShift, empType, eType, objective
        )
    }

}