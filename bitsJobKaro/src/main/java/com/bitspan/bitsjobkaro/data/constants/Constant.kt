package com.bitspan.bitsjobkaro.data.constants

object Constant {
    const val ONBOARD_DELAY_TIME: Long = 2000
    const val ONBOARD_PERIOD_TIME: Long = 2000
    const val DATA_STORE_NAME: String = "Bits_Hiring_Store"
//    const val BASE_URL: String = "https://teamtest.xyz/hiring_system_ci/hiring_system_employee_ci/index.php/Rest_controller/"
//    const val BASE_URL: String = "https://jobkaro.in/jd/jdem/index.php/Rest_controller/"
    const val BASE_URL: String = "https://jobkaro.org.in/jd/jdem/index.php/Rest_controller/"
//    const val BASE_URL_OTHER: String = "https://jobkaro.in/index.php/Rest_controller/"
    const val BASE_URL_OTHER: String = "https://jobkaro.org.in/index.php/Rest_controller/"
//    const val BASE_URL_AUTH: String = "https://jobkaro.in/index.php/Auth_api/"
    const val BASE_URL_AUTH: String = "https://jobkaro.org.in/index.php/Auth_api/"
//    const val BASE_URL_NEW_REC: String = "https://jobkaro.in/jd/jdrec/index.php/Restcontroller/"
    const val BASE_URL_NEW_REC: String = "https://jobkaro.org.in/jd/jdrec/index.php/Restcontroller/"
    var userType: Boolean = true     // True for employee and false for recruiter
    var userLogin: Boolean = false   // Tells whether user is already login in his device or not
    var userId: String = ""
    var recFirstAddCompany: Boolean = false   // Recruiter first time register used for taking details about company if complete then true
    var recFirstJobPost: Boolean = false // Recruiter first job posted or not
    const val LOG_TAG: String = "RisDev"


    var AAU = ""
    var AAP = ""


}