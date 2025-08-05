package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task

data class EmployeeTaskScreenState(
    val tasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val timerText: String = "",
    val showBreakDialog: Boolean = false,
    val timerPerTask : String= "",
    val errorMessage: String? = null,
    val showAddTaskDialog: Boolean = false,
    val isTimerWorking: Boolean = false,
)
