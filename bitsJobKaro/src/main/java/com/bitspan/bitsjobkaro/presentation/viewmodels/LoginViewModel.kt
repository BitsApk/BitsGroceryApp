package com.bitspan.bitsjobkaro.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitspan.bitsjobkaro.data.constants.NetworkResult
import com.bitspan.bitsjobkaro.data.models.LoginBody
import com.bitspan.bitsjobkaro.data.models.LoginData
import com.bitspan.bitsjobkaro.data.models.OtpResponse
import com.bitspan.bitsjobkaro.data.models.RegisterBody
import com.bitspan.bitsjobkaro.data.models.RegisterResponseBody
import com.bitspan.bitsjobkaro.data.models.SendOtpReq
import com.bitspan.bitsjobkaro.data.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {


    lateinit var registerReq: RegisterBody

    val userResponseLiveData: LiveData<NetworkResult<LoginData>>
        get() = loginRepository.userResponseLiveData

    val userRegisterResLiveData: LiveData<NetworkResult<RegisterResponseBody>>
        get() = loginRepository.userRegisterResLiveData

    val recLoginResponseLiveData: LiveData<NetworkResult<LoginData>>
        get() = loginRepository.recLoginResponseLiveData

    val recRegisterResponseLiveData: LiveData<NetworkResult<RegisterResponseBody>>
        get() = loginRepository.recRegisterResponseLiveData


    //    For Employee Login
    fun empLoginUser(empLoginBody: LoginBody) {
        viewModelScope.launch {
            loginRepository.loginUser(empLoginBody)
        }
    }

    // For Recruiter Login
    fun doRecruitLogin(empLoginBody: LoginBody) {
        viewModelScope.launch {
            loginRepository.doRecruitLogin(empLoginBody)
        }
    }

    //    For Employee Register
    fun registerUser(empRegisterBody: RegisterBody) {
        viewModelScope.launch {
            loginRepository.empRegisterUser(empRegisterBody)
        }
    }

    //    For Recruiter Register
    fun doRecruitRegister(empRegisterBody: RegisterBody) {
        viewModelScope.launch {
            loginRepository.doRecruitRegister(empRegisterBody)
        }
    }

}