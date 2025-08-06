package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

import com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.EmployeeTaskScreenEvent

sealed class SupervisorDashboardEvent {
    data class SelectEmployee(val employeeId: String) : SupervisorDashboardEvent()
    data class SaveNewTaskClicked(
        val title: String,
        val description: String,
        val estimatedTime: String,
        val selectedEmployeeId: String
    ) : SupervisorDashboardEvent()
    data class ResolveQuery(val taskId: String) : SupervisorDashboardEvent()
    object AddTaskClicked : SupervisorDashboardEvent()
    object Logout : SupervisorDashboardEvent()
    object FilterClicked : SupervisorDashboardEvent()
    data class FilterApplied(val employeeId: String) : SupervisorDashboardEvent()
    object DismissAddTaskDialog : SupervisorDashboardEvent()
    object DismissFilterDialog : SupervisorDashboardEvent()
}
