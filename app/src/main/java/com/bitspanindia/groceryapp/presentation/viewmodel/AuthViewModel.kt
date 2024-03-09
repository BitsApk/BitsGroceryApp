package com.bitspanindia.groceryapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bitspanindia.groceryapp.data.model.response.ApiAuthResponse
import com.bitspanindia.groceryapp.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _splashUseInfo = MutableLiveData<Boolean>(false)
    val splashUserInfo
        get() = _splashUseInfo


    fun setSplashUseInfo(value: Boolean) {
        _splashUseInfo.postValue(value)
    }

    suspend fun getAuthDetails(): Response<ApiAuthResponse> {
        return authRepository.getAuthDetails()
    }
}