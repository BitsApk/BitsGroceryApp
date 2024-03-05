package com.bitspan.bitsjobkaro.data.repositories

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
import com.bitspan.bitsjobkaro.data.services.OtherApiServices
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


class OtherRepository @Inject constructor(private val otherApiServices: OtherApiServices) {


    // Post api's
    suspend fun sendRegisterOtp(sendOtpReq: SendOtpReq): Response<OtpResponse> {
        return otherApiServices.sendRegisterOtp(sendOtpReq)
    }


    suspend fun sendLoginOtp(sendOtpReq: SendOtpReq): Response<OtpResponse> {
        return otherApiServices.sendLoginOtp(sendOtpReq)
    }

    suspend fun sendForgotOtp(sendOtpReq: SendOtpReq): Response<OtpResponse> {
        return otherApiServices.sendForgotOtp(sendOtpReq)
    }


    suspend fun verifyRegisterOtp(verifyOtpReq: VerifyOtpReq): Response<VerifyOtpResponse> {
        return otherApiServices.verifyRegisterOtp(verifyOtpReq)
    }


    suspend fun verifyLoginOtp(verifyOtpReq: VerifyOtpReq): Response<VerifyLoginOtpResponse> {
        return otherApiServices.verifyLoginOtp(verifyOtpReq)
    }


    suspend fun verifyForgotOtp(verifyOtpReq: VerifyOtpReq): Response<VerifyOtpResponse> {
        return otherApiServices.verifyForgotOtp(verifyOtpReq)
    }

    suspend fun setNewForgotPass(forgotPassReq: ForgotPassReq): Response<ForgotPassResponse> {
        return otherApiServices.setNewForgotPass(forgotPassReq)
    }

    suspend fun recChangePass(changePassReq: ChangePassReq): Response<GeneralMessModel> {
        return otherApiServices.recChangePass(changePassReq)
    }


    suspend fun empChangePass(changePassReq: ChangePassReq): Response<GeneralMessModel> {
        return otherApiServices.empChangePass(changePassReq)
    }



    suspend fun empUpdateBasicData(empBasicDataReq: EmpBasicDataReq): Response<GeneralMessModel> {
        return otherApiServices.empUpdateBasicData(empBasicDataReq)
    }



    suspend fun getEmpProfileImage(profileImageReq: ProfileImageReq): Response<ProfileImageResponse> {
        return otherApiServices.getEmpProfileImage(profileImageReq)
    }



    suspend fun getRecProfileImage(profileImageReq: ProfileImageReq): Response<ProfileImageResponse> {
        return otherApiServices.getRecProfileImage(profileImageReq)
    }




    suspend fun empSearchJob(empSearchJobReq: EmpSearchJobReq): Response<EmpSearchJobResponse> {
        return otherApiServices.empSearchJob(empSearchJobReq)
    }




    suspend fun postEmpProfileImage(uploadPic: MultipartBody.Part, empId: RequestBody): Response<GeneralMessModel> {
        return otherApiServices.postEmpProfileImage(uploadPic, empId)
    }



    suspend fun getEmpSavedBookmarkList(recEmpIdRequest: RecEmpIdRequest): Response<GeneralDataAndMessModel<MutableList<EmpSavedBookmarkData>>> {
        return otherApiServices.getEmpSavedBookmarkList(recEmpIdRequest)
    }



    suspend fun saveUnsaveJob(saveUnsaveJobReq: SaveUnsaveJobReq): Response<GeneralMessModel> {
        return otherApiServices.saveUnsaveJob(saveUnsaveJobReq)
    }

    suspend fun addCurrentProfDetails(addCurrentCompanyReq: AddCurrentCompanyReq): Response<EmpApplyJobResponse> {
        return otherApiServices.addCurrentProfDetails(addCurrentCompanyReq)
    }
    suspend fun getAllProfessDetails(addCurrentCompanyReq: AddCurrentCompanyReq): Response<GetAllProfessDetails> {
        return otherApiServices.getAllProfessDetails(addCurrentCompanyReq)
    }

    suspend fun savePreviousComDetails(addPreviousCompanyReq: AddPreviousCompanyReq): Response<EmpApplyJobResponse> {
        return otherApiServices.savePreviousComDetails(addPreviousCompanyReq)
    }

    suspend fun deletePreDetails(addPreviousCompanyReq: AddPreviousCompanyReq): Response<EmpApplyJobResponse> {
        return otherApiServices.deletePreDetails(addPreviousCompanyReq)
    }

}