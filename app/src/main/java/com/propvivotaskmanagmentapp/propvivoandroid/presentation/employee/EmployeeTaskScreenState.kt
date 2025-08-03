package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task

data class EmployeeTaskScreenState(
    val tasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val timerText: String = "00:00:00"
)
