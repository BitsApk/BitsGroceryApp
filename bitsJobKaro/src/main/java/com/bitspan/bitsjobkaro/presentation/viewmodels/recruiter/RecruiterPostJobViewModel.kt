package com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter

import androidx.lifecycle.ViewModel
import com.bitspan.bitsjobkaro.data.models.GeneralMessModel
import com.bitspan.bitsjobkaro.data.models.recruiter.PostAboutCompanyData
import com.bitspan.bitsjobkaro.data.models.recruiter.PostJobBody
import com.bitspan.bitsjobkaro.data.repositories.recruiter.RecruiterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecruiterPostJobViewModel @Inject constructor(private val recruiterRepository: RecruiterRepository) :
    ViewModel()  {

    val postJobBody = PostJobBody()

    suspend fun postJob(postJobBody: PostJobBody): Response<GeneralMessModel> {
        return recruiterRepository.postJob(postJobBody)
    }




}