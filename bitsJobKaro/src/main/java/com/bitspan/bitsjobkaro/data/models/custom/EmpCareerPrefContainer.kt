package com.bitspan.bitsjobkaro.data.models.custom

data class EmpCareerPrefContainer(
    var jobRole: String? = "",
    var cities: String? = "",
    var skills: List<String>? = listOf(),
    var empId: String? = "",
    var formId: String? = "",
    var expLevel: String? = "",
    var expYr: String? = "",
    var expMonth: String? = "",
    var engLevel: String? = "",
    var stateName: String? = "",
    var stateId: String? = "",
    var minSalary: String? = "",
    var maxSalary: String? = "",
    var prefShift: String? = "",
    var empType: String? = "",
    var eType: String? = "",
    var objective: String? = "",
    var resume: String? = null,
    var dataPresent: Boolean = false
)
