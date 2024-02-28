package com.bitspan.bitsjobkaro.presentation.viewmodels

import androidx.lifecycle.ViewModel
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
import com.bitspan.bitsjobkaro.data.repositories.OtherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor(private val otherRepository: OtherRepository) : ViewModel() {



    val yes: String = "yes"
    val server: String = "jobkaro.in"

    suspend fun sendRegisterOtp(sendOtpReq: SendOtpReq): Response<OtpResponse> {
        return otherRepository.sendRegisterOtp(sendOtpReq)
    }


    suspend fun sendLoginOtp(sendOtpReq: SendOtpReq): Response<OtpResponse> {
        return otherRepository.sendLoginOtp(sendOtpReq)
    }

    suspend fun sendForgotOtp(sendOtpReq: SendOtpReq): Response<OtpResponse> {
        return otherRepository.sendForgotOtp(sendOtpReq)
    }


    suspend fun verifyRegisterOtp(verifyOtpReq: VerifyOtpReq): Response<VerifyOtpResponse> {
        return otherRepository.verifyRegisterOtp(verifyOtpReq)
    }


    suspend fun verifyLoginOtp(verifyOtpReq: VerifyOtpReq): Response<VerifyLoginOtpResponse> {
        return otherRepository.verifyLoginOtp(verifyOtpReq)
    }

    suspend fun verifyForgotOtp(verifyOtpReq: VerifyOtpReq): Response<VerifyOtpResponse> {
        return otherRepository.verifyForgotOtp(verifyOtpReq)
    }

    suspend fun setNewForgotPass(forgotPassReq: ForgotPassReq): Response<ForgotPassResponse> {
        return otherRepository.setNewForgotPass(forgotPassReq)
    }

    suspend fun recChangePass(changePassReq: ChangePassReq): Response<GeneralMessModel> {
        return otherRepository.recChangePass(changePassReq)
    }

    suspend fun empChangePass(changePassReq: ChangePassReq): Response<GeneralMessModel> {
        return otherRepository.empChangePass(changePassReq)
    }


    suspend fun empUpdateBasicData(empBasicDataReq: EmpBasicDataReq): Response<GeneralMessModel> {
        return otherRepository.empUpdateBasicData(empBasicDataReq)
    }


    suspend fun getEmpProfileImage(profileImageReq: ProfileImageReq): Response<ProfileImageResponse> {
        return otherRepository.getEmpProfileImage(profileImageReq)
    }


    suspend fun getRecProfileImage(profileImageReq: ProfileImageReq): Response<ProfileImageResponse> {
        return otherRepository.getRecProfileImage(profileImageReq)
    }


    suspend fun empSearchJob(empSearchJobReq: EmpSearchJobReq): Response<EmpSearchJobResponse> {
        return otherRepository.empSearchJob(empSearchJobReq)
    }


    suspend fun postEmpProfileImage(uploadPic: MultipartBody.Part, empId: RequestBody): Response<GeneralMessModel> {
        return otherRepository.postEmpProfileImage(uploadPic, empId)
    }

    suspend fun getEmpSavedBookmarkList(recEmpIdRequest: RecEmpIdRequest): Response<GeneralDataAndMessModel<MutableList<EmpSavedBookmarkData>>> {
        return otherRepository.getEmpSavedBookmarkList(recEmpIdRequest)
    }

    suspend fun saveUnsaveJob(saveUnsaveJobReq: SaveUnsaveJobReq): Response<GeneralMessModel> {
        return otherRepository.saveUnsaveJob(saveUnsaveJobReq)
    }

    suspend fun addCurrentProfDetails(addCurrentCompanyReq: AddCurrentCompanyReq): Response<EmpApplyJobResponse> {
        return otherRepository.addCurrentProfDetails(addCurrentCompanyReq)
    }
       suspend fun getAllProfessDetails(addCurrentCompanyReq: AddCurrentCompanyReq): Response<GetAllProfessDetails> {
        return otherRepository.getAllProfessDetails(addCurrentCompanyReq)
    }

    suspend fun savePreviousComDetails(addPreviousCompanyReq: AddPreviousCompanyReq): Response<EmpApplyJobResponse> {
        return otherRepository.savePreviousComDetails(addPreviousCompanyReq)
    }

    suspend fun deletePreDetails(addPreviousCompanyReq: AddPreviousCompanyReq): Response<EmpApplyJobResponse> {
        return otherRepository.deletePreDetails(addPreviousCompanyReq)
    }

}