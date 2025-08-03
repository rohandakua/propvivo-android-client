package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.startingscreen

sealed class EmployeeStartTaskScreenEvent {
    object Logout : EmployeeStartTaskScreenEvent()
    object StartDay : EmployeeStartTaskScreenEvent()

}
