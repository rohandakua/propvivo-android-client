package com.propvivotaskmanagmentapp.propvivoandroid.presentation.admin

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Employee

data class AdminDashboardState(
    val employees: List<Employee> = emptyList(),
    val noOfTask: Int = 0,
    val isLoading : Boolean = true
)
