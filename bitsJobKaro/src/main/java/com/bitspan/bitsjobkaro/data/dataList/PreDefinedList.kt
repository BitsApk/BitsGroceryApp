package com.bitspan.bitsjobkaro.data.dataList

import androidx.fragment.app.Fragment
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.models.OnBoardData
import com.bitspan.bitsjobkaro.ui.subFragment.jobInfo.JobAppliedFragment
import com.bitspan.bitsjobkaro.ui.subFragment.jobInfo.JobSavedFragment
import com.bitspan.bitsjobkaro.ui.subFragment.jobInfo.JobViewedByFragment
import com.bitspan.bitsjobkaro.ui.subFragment.recInter.RecruiterManageJobFragment
import com.bitspan.bitsjobkaro.ui.subFragment.recInter.RecruiterSavedFragment
import com.bitspan.bitsjobkaro.ui.subFragment.recInter.RecruiterViewedByFragment

object PreDefinedList {
    val imageList: List<OnBoardData> = listOf(
        OnBoardData(
            "Apply in Dream Company",
            "Hire the best, Recruit with the best",
            R.drawable.image_onboard_first
        ),
        OnBoardData(
            "Chat with job seekers",
            "Get direct involvement using our smooth chat",
            R.drawable.image_onboard_second
        ),
        OnBoardData(
            "Get Hired",
            "JobKaro – where opportunities meet excellence!",
            R.drawable.image_onboard_third
        ),
    )

    val jobFragmentList = listOf<Fragment>(
        JobAppliedFragment(),
        JobSavedFragment(),
        JobViewedByFragment()
    )


    val jobFragmentNamesList = listOf<String>(
        "Applied",
        "Saved",
        "Viewed You"
    )

    val recInteractedFragmentList = listOf(
        RecruiterManageJobFragment(),
        RecruiterSavedFragment(),
        RecruiterViewedByFragment()
    )

    val recInteractFragmentNamesList = listOf<String>(
        "Posted Job",
        "ShortList",
        "Viewed You"
    )

    val industryType = listOf("Automobiles", "Banking", "E-commerce", "Education", "Financial services", "Gems and jewellery", "IT & BPO", "Media & Entertainment",
    "Metals & Mining", "Ports & Power", "Science & Technology", "Real estate", "Telecommunication", "Tourism & Hospitality", "Textile", "Other")


    val docType = listOf("Appointment letter or offer letter", "Company PAN", "FSSAI License",
        "Company incorporation certificate", "Shop & establishment certificate", "MSME REGISTER CERTIFICATE",
        "ID CARD EMPLOYEE", "Any Other")

    // Recruiter
    val formattedExpList = listOf("Any", "Fresher", "1 - 3 yr", "3 - 5 yr", "5+ yr")
    val jobTypeList = listOf("Any", "Part Time", "Full Time", "Contractual")
    val empTimeType = listOf("Any", "Work From Home", "Work From Office", "Field Work")
    val qualList = listOf("Any", "Below 10th", "Class 10th", "Class 12th", "Graduate", "Post Graduate")
    val engList = listOf("Any", "No English", "Little English", "Good English", "Fluent English")
    val ageList = listOf("Any", "18 - 25", "25 - 40", "40 - 50")

    val recChipGroupList = listOf("Java", "Kotlin", "Android sdk", "Android studio", "Retrofit", "UI/UX")
    val jobPostTempList = listOf("Android Engineer", "Rs 2 - 3LPA", "Work from home", "Full time")

    // Employee
    val salaryList = listOf("Any", "0 - 3 LPA", "3 - 5 LPA", "7 - 9 LPA", "10 - 15 LPA")
    val jobEmpTypeList = listOf("Any", "Part Time", "Full Time", "Contractual", "Internship")
    val prefShift = listOf("Any", "Day shift", "Night shift", "Rotational")


}