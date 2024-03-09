package com.bitspan.bitsjobkaro.data.repositories.recruiter

import com.bitspan.bitsjobkaro.data.models.DistrictData
import com.bitspan.bitsjobkaro.data.models.EmpCareerPrefeData
import com.bitspan.bitsjobkaro.data.models.EmpExpData
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.GeneralDataModel
import com.bitspan.bitsjobkaro.data.models.GeneralMessModel
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.StateData
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.AllShortlistedEmpResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.PostJobBody
import com.bitspan.bitsjobkaro.data.models.recruiter.RecProfileData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.services.RecruiterApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RecruiterRepository @Inject constructor(private val recruiterApiService: RecruiterApiService) {

    suspend fun getEmpList(jobId: Int?) : Response<GeneralDataModel<List<RecruiterEmpData>>> {
        return withContext(Dispatchers.IO) {
            recruiterApiService.getEmpList(jobId)
        }
    }

    suspend fun getRecProfileData(userId: Int) : Response<GeneralDataAndMessModel<List<RecProfileData>>> {
        return withContext(Dispatchers.IO) {
            recruiterApiService.getRecProfileData(userId)
        }
    }

    suspend fun getEmpExpData(empId: Int) : Response<GeneralDataAndMessModel<EmpExpData>> {
        return withContext(Dispatchers.IO) {
            recruiterApiService.getEmpExpData(empId)
        }
    }

    suspend fun getEmpCareerPref(empId: Int) : Response<GeneralDataAndMessModel<List<EmpCareerPrefeData>>> {
        return withContext(Dispatchers.IO) {
            recruiterApiService.getEmpCareerPref(empId)
        }
    }

    suspend fun getEmpBasicData(empId: Int) : Response<GeneralDataModel<List<RecruiterEmpData>>> {
        return withContext(Dispatchers.IO) {
            recruiterApiService.getEmpBasicData(empId)
        }
    }

    suspend fun getStates(): Response<List<StateData>> {
        return withContext(Dispatchers.IO) {
            recruiterApiService.getStateList()
        }
    }

    suspend fun getDistrict(): Response<List<DistrictData>> {
        return withContext(Dispatchers.IO) {
            recruiterApiService.getDistrictList()
        }
    }

    // Post api's
    suspend fun postJob(postJobBody: PostJobBody): Response<GeneralMessModel> {
        return withContext(Dispatchers.IO) { recruiterApiService.postJob(postJobBody) }
    }


    suspend fun getAllShortlisted(recId: Int): Response<AllShortlistedEmpResponse> {
        return withContext(Dispatchers.IO) { recruiterApiService.getAllShortlisted(recId)
        }
    }

    suspend fun getRecProfileCount(recEmpIdRequest: RecEmpIdRequest): Response<EmpChatAppliedCountResponse> {
        return withContext(Dispatchers.IO) { recruiterApiService.getRecProfileCount(recEmpIdRequest)
        }
    }

}