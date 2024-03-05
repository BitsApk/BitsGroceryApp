package com.bitspan.bitsjobkaro

import java.util.Locale

object CommonDataFunctions {

    fun checkJobType(type: String?): String {
        return when (type) {
            "wfh", "wrkFHome" -> "Work From Home"
            "field", "fieldWork" -> "Field Work"
            else -> "Work From Office"
        }
    }

    fun checkJobTimeType(type: String?): String {
        return when (type) {
            "partTime", "part" -> "Part Time"
            "contractual" -> "Contractual"
            "internship", "Internship" -> "Internship"
            else -> "Full Time"
        }
    }

    fun getFormattedExp(exp: String?): String {
        return when(exp) {
            "fresher" -> "Fresher"
            "1_to_2" -> "1 - 2 yr"
            "above_5" -> "5+ yr"
            else -> "2 - 5 yr"
        }
    }

    fun getFormattedShifts(shift: String?): String {
        return when (shift) {
            "day", "dayShift" -> "Day Shift"
            "night", "nightShift" -> "Night Shift"
            "rotationalShift" -> "Rotational"
            "flexible" -> "Flexible"
            else -> ""
        }
    }

    fun getFormattedEng(eng: String?): String {
        return when (eng) {
            "no_eng", "no" -> "No English"
            "litt_eng", "little" -> "Little English"
            "good_eng", "good" -> "Good English"
            else -> "Fluent English"
        }
    }

    fun getFormattedQualif(qual: String?): String {
        return when (qual) {
            "below_10" -> "Below 10th"
            "below_12" -> "10 - 12th"
            "graduation" -> "Graduation"
            else -> "Post Graduation"
        }
    }



    fun getFormattedEmpQualif(qual: String?): String {
        return when (qual) {
            "10th" -> "Class 10th"
            "12th" -> "Class 12th"
            "grad" -> "Graduate"
            "post_grad" -> "Post Graduate"
            "Doctrate" -> "Doctrate"
            else -> "NA"
        }
    }


    fun getIndusType(indus: String?): String {
        return when(indus) {
            "e_commerce" -> "E-commerce"
            "financial_services" -> "Financial services"
            "gems_and_jewellery" -> "Gems and jewellery"
            "IT_BPO" -> "IT & BPO"
            "media_entertainment" -> "Media & Entertainment"
            "metals_minig" -> "Metals & Mining"
            "ports_power" -> "Ports & Power"
            "science_technology" -> "Science & Technology"
            "real_estate" -> "Real estate"
            "tourism_hospitality" -> "Tourism & Hospitality"
            else -> indus?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } ?: "Others"
        }
    }


    fun getDocType(doc: String?): String {
        return when(doc) {
            "app_let" -> "Appointment letter or offer letter"
            "com_pan" -> "Company PAN"
            "fssai_lic" -> "FSSAI License"
            "company_incorp" -> "Company incorporation certificate"
            "shop_estab_cert" ->  "Shop & establishment certificate"
            "msme_reg_cert" ->  "MSME REGISTER CERTIFICATE"
            "id_card_emp" ->  "ID CARD EMPLOYEE"
            else -> "Any Other"

        }
    }


    fun getFormattedSalary(sMin: String?, sMax: String?): String {
        return "Rs $sMin - $sMax LPA"
    }

    fun getFormattedExpYr(year: String? = "0", month: String? = "00"): String {
        val mon = if (month?.length == 1) "0$month" else month
        return "${year}.$mon Yr"
    }


    fun postJobSalary(sal: String?): String {
        return when(sal) {
            "0 - 3 LPA" -> "0to3"
            "3 - 5 LPA" -> "3to5"
            "7 - 9 LPA" -> "7to9"
            "10 - 15 LPA" -> "10to15"
            else -> ""
        }
    }


    fun postJobTimeType(type: String?): String {
        return when (type) {
            "Part Time" -> "partTime"
            "Contractual" -> "contractual"
            "Internship" -> "Internship"
            "Full Time" -> "fullTime"
            else -> ""
        }
    }

    fun postEmpEmpType(type: String?): String {
        return when (type) {
            "Work From Home" -> "wrkFHome"
            "Field Work" -> "fieldWork"
            "Work From Office" -> "wrkFoffice"
            else -> ""
        }
    }



    // Recruiter side reverse filters
    fun postRecEmpType(type: String?): String {
        return when (type) {
            "Work From Home" -> "wrkFHome"
            "Field Work" -> "fieldWork"
            else -> "wrkFoffice"
        }
    }

    fun postRecJobTimeType(type: String?): String {
        return when (type) {
            "Part Time" -> "partTime"
            "Contractual" -> "contractual"
            "Internship" -> "Internship"
            else -> "fullTime"
        }
    }

    fun postRecExperience(exp: String?): String {
        return when(exp) {
            "Fresher" -> "fresher"
            "1 - 2 Yr" -> "1_to_2"
            "5+ Yr" -> "above_5"
            else -> "2_to_5"
        }
    }

    fun postRecShifts(shift: String?): String {
        return when (shift) {
            "Day Shift" -> "dayShift"
            "Night Shift" -> "nightShift"
            "Rotational" -> "rotationalShift"
            else -> ""
        }
    }

    fun postRecQualif(qual: String?): String {
        return when (qual) {
            "Below 10th" -> "below_10"
            "10 - 12th" -> "below_12"
            "Graduation" -> "graduation"
            else -> "postGraduation"
        }
    }

    fun postRecEng(eng: String?): String {
        return when (eng) {
            "No English" -> "no_eng"
            "Little English" -> "litt_eng"
            else -> "good_eng"
        }
    }


    fun postEmpEng(eng: String?): String {
        return when (eng) {
            "No English" -> "no"
            "Little English" -> "little"
            "Fluent English" -> "fluent"
            "Good English" -> "good"
            else -> ""
        }
    }

    fun postIndusType(indus: String?): String {
        return when(indus) {
            "E-commerce" -> "e_commerce"
            "Financial services" -> "financial_services"
            "Gems and jewellery" -> "gems_and_jewellery"
            "IT & BPO" -> "IT_BPO"
            "Media & Entertainment" -> "media_entertainment"
            "Metals & Mining" -> "metals_minig"
            "Ports & Power" -> "ports_power"
            "Science & Technology" -> "science_technology"
            "Real estate" -> "real_estate"
            "Tourism & Hospitality" -> "tourism_hospitality"
            else -> indus?.replaceFirstChar { if (it.isUpperCase()) it.lowercase(Locale.ROOT) else it.toString() } ?: "others"
        }
    }

    fun postDocType(doc: String?): String {
        return when(doc) {
            "Appointment letter or offer letter" -> "app_let"
            "Company PAN" -> "com_pan"
            "FSSAI License" -> "fssai_lic"
            "Company incorporation certificate" -> "company_incorp"
            "Shop & establishment certificate" -> "shop_estab_cert"
            "MSME REGISTER CERTIFICATE" -> "msme_reg_cert"
            "ID CARD EMPLOYEE" -> "id_card_emp"
            else -> "any_other"

        }
    }


    // Employee side reverse filter
    fun postEmployeeEmpType(type: String?): String {
        return when (type) {
            "Work From Home" -> "wfh"
            "Field Work" -> "field"
            "Work From Office" -> "wfo"
            else -> ""
        }
    }

    fun postEmpJobTimeType(type: String?): String {
        return when (type) {
            "Part Time" -> "part"
            "Contractual" -> "contractual"
            "Internship" -> "internship"
            "Full", "Full Time" -> "full"
            else -> ""
        }
    }



    fun postEmpShifts(shift: String?): String {
        return when (shift) {
            "Day" -> "day"
            "Night" -> "night"
            else -> "flexible"
        }
    }


    fun postEmpQualif(qual: String?): String {
        return when (qual) {
            "Below 10th" -> "below10th"
            "Class 10th" -> "10th"
            "Class 12th" -> "12th"
            "Graduate" -> "grad"
            "Post Graduate" -> "post_grad"
            "Doctrate" -> "Doctrate"
            else -> ""
        }
    }

    fun postEmpAge(age: String?): String {
        return when (age) {
            "18 - 25" -> "18to25"
            "25 - 40" -> "25to40"
            "40 - 50" -> "40to50"
            else -> ""
        }

    }


    fun postEmpExperience(exp: String?): String {
        return when(exp) {
            "Fresher" -> "fresher"
            "1 - 3 yr" -> "1to3"
            "3 - 5 yr" -> "3to5"
            "5+ yr" -> "moreTha5"
            else -> ""
        }
    }



    fun postNoticePeriod(noticePeriod: String?): Int {
        return when (noticePeriod) {
            "15 Days" -> 15
            "1 Month" -> 1
            "2 Month" -> 2
            "3 Month" -> 3
            else -> 0
        }

    }

    fun getNoticePeriod(noticePeriod: Int?): String {
        return when (noticePeriod) {
             15-> "15 Days"
             1-> "1 Month"
             2-> "2 Month"
             3-> "3 Month"
            else -> ""
        }

    }



}