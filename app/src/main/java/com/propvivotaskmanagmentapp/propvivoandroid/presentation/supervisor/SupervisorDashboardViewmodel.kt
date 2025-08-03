package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

import androidx.lifecycle.ViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SupervisorDashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        SupervisorDashboardState(
            employees = sampleEmployees
        )
    )
    val state: StateFlow<SupervisorDashboardState> = _state

    fun onEvent(event: SupervisorDashboardEvent) {
        when (event) {
            is SupervisorDashboardEvent.SelectEmployee -> {
                _state.value = _state.value.copy(selectedEmployeeId = event.employeeId)
            }

            is SupervisorDashboardEvent.SelectTask -> {
                // Future: navigate to task detail or open dialog
            }

            is SupervisorDashboardEvent.AddTaskClicked -> {
                // Future: show add task dialog
            }

            SupervisorDashboardEvent.Logout -> {
                // Handle logout
            }

            SupervisorDashboardEvent.FilterClicked -> {
                // Future: open filter dialog
            }
        }
    }

    companion object {
        val sampleEmployees = listOf(
            EmployeeWithTasks(
                id = "emp1",
                name = "John Doe",
                tasks = listOf(
                    Task(
                        id = "1",
                        title = "Task 1",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    ),
                    Task(
                        id = "2",
                        title = "Task 2",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    ),
                    Task(
                        id = "3",
                        title = "Task 3",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    )
                )
            ),
            EmployeeWithTasks(
                id = "emp1",
                name = "John Doe",
                tasks = listOf(
                    Task(
                        id = "1",
                        title = "Task 1",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    ),
                    Task(
                        id = "2",
                        title = "Task 2",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    ),
                    Task(
                        id = "3",
                        title = "Task 3",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    )
                )
            ),
            EmployeeWithTasks(
                id = "emp1",
                name = "John Doe",
                tasks = listOf(
                    Task(
                        id = "1",
                        title = "Task 1",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    ),
                    Task(
                        id = "2",
                        title = "Task 2",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    ),
                    Task(
                        id = "3",
                        title = "Task 3",
                        description = "Desc",
                        estimatedTimeMs = 0,
                        timeSpentMs = 0,
                        assignedTo = "",
                        assignedBy = "",
                        createdAt = 0,
                        updatedAt = 0
                    )
                )
            )
        )
    }
}
