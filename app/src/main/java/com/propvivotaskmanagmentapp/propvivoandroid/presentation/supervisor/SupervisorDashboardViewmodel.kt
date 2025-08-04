package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.SupervisorRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
@HiltViewModel
class SupervisorDashboardViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val supervisorRepository: SupervisorRepositoryInterface
) : ViewModel() {

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
                _state.value = _state.value.copy(showAddTaskDialog = true)

            }

            SupervisorDashboardEvent.Logout -> {
                viewModelScope.launch {
                    signOutUseCase.invoke()
                    preferenceDataStoreHelper.removePreference(dsConstants.USER_ID)
                    preferenceDataStoreHelper.removePreference(dsConstants.USER_NAME)
                    preferenceDataStoreHelper.removePreference(dsConstants.USER_EMAIL)
                    preferenceDataStoreHelper.removePreference(dsConstants.USER_ROLE)
                }
                //TODO move to login screen
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
