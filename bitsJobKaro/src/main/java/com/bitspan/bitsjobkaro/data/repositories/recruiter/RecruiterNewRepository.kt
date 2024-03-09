package com.bitspan.bitsjobkaro.data.repositories.recruiter

import com.bitspan.bitsjobkaro.data.models.*
import com.bitspan.bitsjobkaro.data.models.recruiter.AllShortlistedEmpResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.PostAboutCompanyData
import com.bitspan.bitsjobkaro.data.models.recruiter.PostJobBody
import com.bitspan.bitsjobkaro.data.models.recruiter.RecProfileData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecSearchEmpResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.RecSearchEmployeReq
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.services.RecruiterApiService
import com.bitspan.bitsjobkaro.data.services.RecruiterNewApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Part
import javax.inject.Inject

class RecruiterNewRepository @Inject constructor(private val recruiterNewApiService: RecruiterNewApiService) {

    suspend fun postAboutCompanyData(
        documents: MultipartBody.Part?, c_logo: MultipartBody.Part?, c_logo11: RequestBody?, documents11: RequestBody?, name: RequestBody, cType: RequestBody,
        cNumber: RequestBody, cEmail: RequestBody, rName: RequestBody, rMobile: RequestBody,
        rDesign: RequestBody, cDesc: RequestBody, cGst: RequestBody, nEmp: RequestBody, adress: RequestBody,
        link: RequestBody, city: RequestBody, state: RequestBody, zip: RequestBody, district: RequestBody,
        createdBy: RequestBody, formId: RequestBody, recId: RequestBody, docType: RequestBody
    ): Response<GeneralMessModel> {
        return recruiterNewApiService.postAboutCompanyData(
            documents, c_logo, c_logo11, documents11, name, cType, cNumber, cEmail, rName,
            rMobile, rDesign, cDesc, cGst, nEmp, adress, link, city,
            state, zip, district, createdBy, formId, recId, docType
        )
    }

    suspend fun postRecProfileLogo(c_logo: MultipartBody.Part, recId: RequestBody): Response<GeneralMessModel> {
        return recruiterNewApiService.postRecProfileLogo(c_logo, recId)
    }

    suspend fun recSearchEmployee(recSearchEmployeReq: RecSearchEmployeReq): Response<RecSearchEmpResponse> {
        return recruiterNewApiService.recSearchEmployee(recSearchEmployeReq)
    }


}