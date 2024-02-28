package com.bitspan.bitsjobkaro.data.services

import com.bitspan.bitsjobkaro.data.models.ChangePassReq
import com.bitspan.bitsjobkaro.data.models.ForgotPassReq
import com.bitspan.bitsjobkaro.data.models.ForgotPassResponse
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.GeneralMessModel
import com.bitspan.bitsjobkaro.data.models.OtpResponse
import com.bitspan.bitsjobkaro.data.models.ProfileImageReq
import com.bitspan.bitsjobkaro.data.models.ProfileImageResponse
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.SendOtpReq
import com.bitspan.bitsjobkaro.data.models.VerifyLoginOtpResponse
import com.bitspan.bitsjobkaro.data.models.VerifyOtpReq
import com.bitspan.bitsjobkaro.data.models.VerifyOtpResponse
import com.bitspan.bitsjobkaro.data.models.employee.AddCurrentCompanyReq
import com.bitspan.bitsjobkaro.data.models.employee.AddPreviousCompanyReq
import com.bitspan.bitsjobkaro.data.models.employee.EmpApplyJobResponse
import com.bitspan.bitsjobkaro.data.models.employee.EmpBasicDataReq
import com.bitspan.bitsjobkaro.data.models.employee.EmpSavedBookmarkData
import com.bitspan.bitsjobkaro.data.models.employee.EmpSearchJobReq
import com.bitspan.bitsjobkaro.data.models.employee.EmpSearchJobResponse
import com.bitspan.bitsjobkaro.data.models.employee.GetAllProfessDetails
import com.bitspan.bitsjobkaro.data.models.employee.SaveUnsaveJobReq
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface OtherApiServices {

    @POST("smsapi")
    suspend fun sendRegisterOtp(
        @Body sendOtpReq: SendOtpReq
    ): Response<OtpResponse>


    @POST("send_to_db_sms_login")
    suspend fun sendLoginOtp(
        @Body sendOtpReq: SendOtpReq
    ): Response<OtpResponse>


    @POST("send_otp_resetpassword")
    suspend fun sendForgotOtp(
        @Body sendOtpReq: SendOtpReq
    ): Response<OtpResponse>


    @POST("verify_otp")
    suspend fun verifyRegisterOtp(
        @Body verifyOtpReq: VerifyOtpReq
    ): Response<VerifyOtpResponse>


    @POST("verify_otp_for_login")
    suspend fun verifyLoginOtp(
        @Body verifyOtpReq: VerifyOtpReq
    ): Response<VerifyLoginOtpResponse>



    @POST("verify_otp_resetpassword")
    suspend fun verifyForgotOtp(
        @Body verifyOtpReq: VerifyOtpReq
    ): Response<VerifyOtpResponse>



    @POST("resetnew_password")
    suspend fun setNewForgotPass(
        @Body forgotPassReq: ForgotPassReq
    ): Response<ForgotPassResponse>


    @POST("recruiter_change_password")
    suspend fun recChangePass(
        @Body changePassReq: ChangePassReq
    ): Response<GeneralMessModel>


    @POST("employee_change_password")
    suspend fun empChangePass(
        @Body changePassReq: ChangePassReq
    ): Response<GeneralMessModel>


    @POST("employee_upload_profile_data")
    suspend fun empUpdateBasicData(
        @Body empBasicDataReq: EmpBasicDataReq
    ): Response<GeneralMessModel>


    @POST("employee_profile_image")
    suspend fun getEmpProfileImage(
        @Body profileImageReq: ProfileImageReq
    ): Response<ProfileImageResponse>



    @POST("recruiter_profile_image")
    suspend fun getRecProfileImage(
        @Body profileImageReq: ProfileImageReq
    ): Response<ProfileImageResponse>


    @POST("employee_job_search")
    suspend fun empSearchJob(
        @Body empSearchJobReq: EmpSearchJobReq
    ): Response<EmpSearchJobResponse>


    @POST("emploee_save_job")
    suspend fun getEmpSavedBookmarkList(
        @Body recEmpIdRequest: RecEmpIdRequest
    ): Response<GeneralDataAndMessModel<MutableList<EmpSavedBookmarkData>>>


    @Multipart
    @POST("employee_upload_profile_pic")
    suspend fun postEmpProfileImage(
        @Part uploadPic: MultipartBody.Part,
        @Part ("emp_id") empId: RequestBody
    ): Response<GeneralMessModel>


    @POST("savejob_unsavejob")
    suspend fun saveUnsaveJob(
        @Body saveUnsaveJobReq: SaveUnsaveJobReq
    ): Response<GeneralMessModel>


    @POST("save_curr_ProfessionalDetail")
    suspend fun addCurrentProfDetails(
        @Body addCurrentCompanyReq: AddCurrentCompanyReq
    ): Response<EmpApplyJobResponse>

    @POST("AllProfessionalDetail")
    suspend fun getAllProfessDetails(
        @Body addCurrentCompanyReq: AddCurrentCompanyReq
    ): Response<GetAllProfessDetails>

    @POST("save_prev_ProfessionalDetail")
    suspend fun savePreviousComDetails(
        @Body addPreviousCompanyReq: AddPreviousCompanyReq
    ): Response<EmpApplyJobResponse>

    @POST("delete_prev_ProfessionalDetail")
    suspend fun deletePreDetails(
        @Body addPreviousCompanyReq: AddPreviousCompanyReq
    ): Response<EmpApplyJobResponse>

}