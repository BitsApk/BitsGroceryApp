package com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.CommonUiFunctions.distList
import com.bitspan.bitsjobkaro.CommonUiFunctions.statesList
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.constants.Constant.LOG_TAG
import com.bitspan.bitsjobkaro.data.models.DistrictData
import com.bitspan.bitsjobkaro.data.models.EmpCareerPrefeData
import com.bitspan.bitsjobkaro.data.models.EmpExpData
import com.bitspan.bitsjobkaro.data.models.GeneralDataAndMessModel
import com.bitspan.bitsjobkaro.data.models.GeneralDataModel
import com.bitspan.bitsjobkaro.data.models.RecEmpIdRequest
import com.bitspan.bitsjobkaro.data.models.employee.EmpChatAppliedCountResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.AllShortlistedEmpResponse
import com.bitspan.bitsjobkaro.data.models.recruiter.RecProfileData
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.repositories.recruiter.RecruiterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecruiterViewModel @Inject constructor(private val recruiterRepository: RecruiterRepository) :
    ViewModel() {


    suspend fun getEmpList(jobId: Int?): Response<GeneralDataModel<List<RecruiterEmpData>>> {
        return recruiterRepository.getEmpList(jobId)
    }

    suspend fun getRecProfileData(userId: Int): Response<GeneralDataAndMessModel<List<RecProfileData>>> {
        return recruiterRepository.getRecProfileData(userId)
    }

    suspend fun getEmpExpData(empId: Int): Response<GeneralDataAndMessModel<EmpExpData>> {
        return recruiterRepository.getEmpExpData(empId)

    }

    suspend fun getEmpCareerPref(empId: Int): Response<GeneralDataAndMessModel<List<EmpCareerPrefeData>>> {
        return recruiterRepository.getEmpCareerPref(empId)

    }

    suspend fun getEmpBasicData(empId: Int): Response<GeneralDataModel<List<RecruiterEmpData>>> {
        return recruiterRepository.getEmpBasicData(empId)

    }

    suspend fun getStates() {
        recruiterRepository.getStates().let {
            if (it.isSuccessful) statesList = it.body()!!
            else Log.d(LOG_TAG, "Error in fetching state list")
        }
    }

    fun getStatesPair(): Pair<List<String>, List<String>> {
        val stateTitleList = mutableListOf<String>()
        val statIdList = mutableListOf<String>()
        statesList.let {
            for (item in it) {
                stateTitleList.add(item.stateTitle ?: "")
                statIdList.add(item.stateId ?: "")
            }

        }
        return Pair(stateTitleList, statIdList)
    }


    suspend fun getDistrict(): List<DistrictData> {
        recruiterRepository.getDistrict().let {
            if (it.isSuccessful) {
                distList = it.body()!!
            } else {
                Log.d(Constant.LOG_TAG, "Error in fetching district list")
            }
        }
        return distList
    }

    fun getDistrictWithId(
        stateId: String,
        list: List<DistrictData>
    ): Pair<List<String>, List<String>> {
        val distList = mutableListOf<String>()
        val distIdList = mutableListOf<String>()
        for (item in list) if (stateId == item.stateId) {
            distList.add(item.districtTitle ?: "")
            distIdList.add(item.districtid ?: "")
        }
        return Pair(distList, distIdList)
    }


    suspend fun getAllShortlisted(recId: Int): Response<AllShortlistedEmpResponse> {
        return recruiterRepository.getAllShortlisted(recId)
    }

    suspend fun getRecProfileCount(recEmpIdRequest: RecEmpIdRequest): Response<EmpChatAppliedCountResponse> {
        return recruiterRepository.getRecProfileCount(recEmpIdRequest)
    }


}