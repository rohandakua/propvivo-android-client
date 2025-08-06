package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Employee
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task

data class SupervisorDashboardState(
    val employees: List<EmployeeWithTasks> = emptyList(),
    val employeeOnlyList : List<Employee> = emptyList(),
    val selectedEmployeeId: String? = null,
    val showAddTaskDialog : Boolean = false,
    val isLoading: Boolean = false,
    val showFilterDialog: Boolean = false,
    val errorMessage: String? = null
)

data class EmployeeWithTasks(
    val id: String,
    val name: String,
    val tasks: List<Task>
)
