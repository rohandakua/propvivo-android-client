package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task

data class SupervisorDashboardState(
    val employees: List<EmployeeWithTasks> = emptyList(),
    val selectedEmployeeId: String? = null,
    val showAddTaskDialog : Boolean = false,
)

data class EmployeeWithTasks(
    val id: String,
    val name: String,
    val tasks: List<Task>
)
