package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

sealed class SupervisorDashboardEvent {
    data class SelectEmployee(val employeeId: String) : SupervisorDashboardEvent()
    data class SelectTask(val taskId: String) : SupervisorDashboardEvent()
    object AddTaskClicked : SupervisorDashboardEvent()
    object Logout : SupervisorDashboardEvent()
    object FilterClicked : SupervisorDashboardEvent()
}
