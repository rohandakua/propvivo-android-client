package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.startingscreen

import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Summary
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task

data class EmployeeStartTaskScreenState(
    val noOfTask: Int = 0,
    val summary: Summary = Summary.YET_TO_WORK

)
