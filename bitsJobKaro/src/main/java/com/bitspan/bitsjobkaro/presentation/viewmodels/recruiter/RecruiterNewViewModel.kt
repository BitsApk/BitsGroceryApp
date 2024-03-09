package com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.GeneralMessModel
import com.bitspan.bitsjobkaro.data.models.recruiter.PostAboutCompanyData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecSearchEmpResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.RecSearchEmployeReq
import com.bitspan.bitsjobkaro.data.repositories.recruiter.RecruiterNewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecruiterNewViewModel @Inject constructor(private val recruiterNewRepository: RecruiterNewRepository) :
    ViewModel() {


    val postAboutCompanyData = PostAboutCompanyData()

    suspend fun postAboutCompanyData(
        documents: MultipartBody.Part?, c_logo: MultipartBody.Part?,  c_logo11: RequestBody?, documents11: RequestBody?, name: RequestBody, cType: RequestBody,
        cNumber: RequestBody, cEmail: RequestBody, rName: RequestBody, rMobile: RequestBody,
        rDesign: RequestBody, cDesc: RequestBody, cGst: RequestBody, nEmp: RequestBody, adress: RequestBody,
        link: RequestBody, city: RequestBody, state: RequestBody, zip: RequestBody, district: RequestBody,
        createdBy: RequestBody, formId: RequestBody, recId: RequestBody, docType: RequestBody
    ): Response<GeneralMessModel> {
        return recruiterNewRepository.postAboutCompanyData(
            documents, c_logo,  c_logo11, documents11, name, cType, cNumber, cEmail, rName,
            rMobile, rDesign, cDesc, cGst, nEmp, adress, link, city,
            state, zip, district, createdBy, formId, recId, docType
        )
    }


    suspend fun postRecProfileLogo(c_logo: MultipartBody.Part, recId: RequestBody): Response<GeneralMessModel> {
        return recruiterNewRepository.postRecProfileLogo(c_logo, recId)
    }


    suspend fun recSearchEmployee(recSearchEmployeReq: RecSearchEmployeReq): Response<RecSearchEmpResponse> {
        return recruiterNewRepository.recSearchEmployee(recSearchEmployeReq)
    }

}