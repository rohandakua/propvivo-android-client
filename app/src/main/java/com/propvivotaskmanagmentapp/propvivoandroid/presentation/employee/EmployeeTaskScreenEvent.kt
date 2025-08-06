package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee

sealed class EmployeeTaskScreenEvent {
    data class SelectTask(val taskId: String) : EmployeeTaskScreenEvent()
    data class StartTask(val taskId: String) : EmployeeTaskScreenEvent()
    data class TogglePauseResume(val taskId: String) : EmployeeTaskScreenEvent()
    data class RaiseQuery(val taskId: String) : EmployeeTaskScreenEvent()
    data class SaveNewTaskClicked(val title: String, val description: String, val estimatedTime: String,
                                  val selectedEmployeeId: String?) : EmployeeTaskScreenEvent()
    object TakeBreak : EmployeeTaskScreenEvent()
    object AddTaskClicked : EmployeeTaskScreenEvent()
    object Logout : EmployeeTaskScreenEvent()
    object FinishWork : EmployeeTaskScreenEvent()
    object DismissAddTaskDialog: EmployeeTaskScreenEvent()

}
