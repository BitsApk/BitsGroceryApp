package com.bitspan.bitsjobkaro.data.repositories

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.constants.NetworkResult
import com.bitspan.bitsjobkaro.data.models.*
import com.bitspan.bitsjobkaro.data.services.LoginService
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginService: LoginService) {


    //  Response is same for the Login and Registration
    private val _userResponseLiveData = MutableLiveData<NetworkResult<LoginData>>()
    val userResponseLiveData: LiveData<NetworkResult<LoginData>>
        get() = _userResponseLiveData

    private val _userRegisterResLiveData = MutableLiveData<NetworkResult<RegisterResponseBody>>()
    val userRegisterResLiveData: LiveData<NetworkResult<RegisterResponseBody>>
        get() = _userRegisterResLiveData

    private val _recLoginResponseLiveData = MutableLiveData<NetworkResult<LoginData>>()
    val recLoginResponseLiveData: LiveData<NetworkResult<LoginData>>
        get() = _recLoginResponseLiveData

    private val _recRegisterResponseLiveData = MutableLiveData<NetworkResult<RegisterResponseBody>>()
    val recRegisterResponseLiveData: LiveData<NetworkResult<RegisterResponseBody>>
        get() = _recRegisterResponseLiveData


    //    For Login employee
    suspend fun loginUser(empLoginBody: LoginBody) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = loginService.getEmpLoginData(empLoginBody.email, empLoginBody.password)
        Log.d(TAG, "loginUser:$response")
        handleLoginResponse(response, true)
    }

    // For login recruiter
    suspend fun doRecruitLogin(loginBody: LoginBody) {
        _recLoginResponseLiveData.postValue(NetworkResult.Loading())
        val response = loginService.doRecruitLogin(loginBody.email, loginBody.password)
        Log.d(LOG_TAG, "loginUser:$response")
        handleLoginResponse(response, false)
    }


    //    For Registration
    suspend fun empRegisterUser(empRegisterBody: RegisterBody) {
        _userRegisterResLiveData.postValue(NetworkResult.Loading())
        val registerResponse = loginService.getEmpRegisterData(empRegisterBody)
        handleRegisterResponse(registerResponse, true)
    }

    suspend fun doRecruitRegister(empRegisterBody: RegisterBody) {
        _recRegisterResponseLiveData.postValue(NetworkResult.Loading())
        val registerResponse = loginService.doRecruitRegister(empRegisterBody)
        handleRegisterResponse(registerResponse, false)
    }

    private fun handleLoginResponse(response: Response<LoginData>, userType: Boolean) {
        val netWorkResult = if (response.body() != null) {
            if (response.body()!!.loginStatus == 200) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.body()?.loginMessage  ?: "Response message is null")
            }
        }  else {
            NetworkResult.Error("Something Went Wrong")
        }
        if (userType) _userResponseLiveData.postValue(netWorkResult)
        else _recLoginResponseLiveData.postValue(netWorkResult)
    }

    private fun handleRegisterResponse(registerResponse: Response<GeneralDataAndMessModel<RegisterResponseBody>>, userType: Boolean) {
        val networkResult = if (registerResponse.body() != null) {
            if (registerResponse.body()!!.status == 200) {
                NetworkResult.Success(registerResponse.body()!!.data!!)
            } else NetworkResult.Error(registerResponse.body()!!.mess)
        } else {
            NetworkResult.Error("Something Went Wrong")
        }
        if (userType) _userRegisterResLiveData.postValue(networkResult)
        else _recRegisterResponseLiveData.postValue(networkResult)
    }

}