package com.bitspan.bitsjobkaro.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bitspan.bitsjobkaro.storage.DataStoreManager
import com.bitspan.bitsjobkaro.storage.DataStoreManagerImpl.Companion.ALREADY_LOGIN
import com.bitspan.bitsjobkaro.storage.DataStoreManagerImpl.Companion.STORAGE_LOC
import com.bitspan.bitsjobkaro.storage.DataStoreManagerImpl.Companion.USER_ID
import com.bitspan.bitsjobkaro.storage.DataStoreManagerImpl.Companion.USER_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(private val dataStore: DataStoreManager) : ViewModel() {

    fun saveUserType(isEmployee: Boolean) {
        viewModelScope.launch {
            dataStore.put(USER_TYPE, isEmployee)
        }
    }


    fun saveLogin(isLogin: Boolean) {
        viewModelScope.launch {
            dataStore.put(ALREADY_LOGIN, isLogin)
        }
    }

    fun saveUserId(id: String) {
        viewModelScope.launch {
            dataStore.put(USER_ID, id)
        }
    }

    fun saveStorageLoc(uri: String) {
        viewModelScope.launch {
            dataStore.put(STORAGE_LOC, uri)
        }
    }

    fun getUserType() = dataStore.get(USER_TYPE, true).asLiveData()
    fun getLogin() = dataStore.get(ALREADY_LOGIN, false).asLiveData()
    fun getUserId() = dataStore.get(USER_ID, "").asLiveData()
    fun getStorageLoc() = dataStore.get(STORAGE_LOC, "").asLiveData()
//        fun getFirstRegister() = dataStore.get(FIRST_REGISTER, false).asLiveData()
//        fun getFirstPost() = dataStore.get(FIRST_POST, false).asLiveData()

}