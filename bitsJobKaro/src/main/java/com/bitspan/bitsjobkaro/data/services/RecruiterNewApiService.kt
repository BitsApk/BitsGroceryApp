package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.*
import com.bitspan.bitsjobkaro.data.models.recruiter.AllShortlistedEmpResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.PostAboutCompanyData
import com.bitspan.bitsjobkaro.data.models.recruiter.PostJobBody
import com.bitspan.bitsjobkaro.data.models.recruiter.RecProfileData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecSearchEmpResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.RecSearchEmployeReq
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RecruiterNewApiService {

    @Multipart
    @POST("recruiter_add_aboutcompany")
    suspend fun postAboutCompanyData(
        @Part documents: MultipartBody.Part?,
        @Part c_logo: MultipartBody.Part?,
        @Part("c_logo11") c_logo11: RequestBody?,
        @Part("documents11")documents11: RequestBody?,
        @Part("name") name: RequestBody,
        @Part("c_type") cType: RequestBody,
        @Part("number") cNumber: RequestBody,
        @Part("email") cEmail: RequestBody,
        @Part("r_name") rName: RequestBody,
        @Part("r_mobile") rMobile: RequestBody,
        @Part("r_desig") rDesign: RequestBody,
        @Part("c_desc") cDesc: RequestBody,
        @Part("c_gst") cGst: RequestBody,
        @Part("n_emp") nEmp: RequestBody,
        @Part("adress") adress: RequestBody,
        @Part("link") link: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("zip") zip: RequestBody,
        @Part("District") district: RequestBody,
        @Part("created_by") createdBy: RequestBody,
        @Part("formId") formId: RequestBody,
        @Part("rec_id") recId: RequestBody,
        @Part("doc_type") docType: RequestBody
    ): Response<GeneralMessModel>


    @POST("recruiter_employee_details_search")
    suspend fun recSearchEmployee(
        @Body recSearchEmployeReq: RecSearchEmployeReq
    ): Response<RecSearchEmpResponse>


    @Multipart
    @POST("upload_recruiter_logo")
    suspend fun postRecProfileLogo(
        @Part c_logo: MultipartBody.Part,
        @Part ("rec_id") recId: RequestBody
    ): Response<GeneralMessModel>

}