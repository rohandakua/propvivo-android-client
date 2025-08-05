package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.SupervisorRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignOutUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.navigation.AppDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onQueryClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateTo(AppDestination.TaskQueryScreen(false).destination))
        }
    }

    fun onEvent(event: SupervisorDashboardEvent) {
        when (event) {
            is SupervisorDashboardEvent.SelectEmployee -> {
                _state.value = _state.value.copy(selectedEmployeeId = event.employeeId)
            }

            is SupervisorDashboardEvent.SelectTask -> {
                onQueryClicked()
            }

            is SupervisorDashboardEvent.AddTaskClicked -> {
                // Future: show add task dialog
                _state.value = _state.value.copy(showAddTaskDialog = true)

            }

            SupervisorDashboardEvent.Logout -> {
                viewModelScope.launch {
                    signOutUseCase.invoke()

                    preferenceDataStoreHelper.clearAllPreference()

                    _navigationEvent.emit(NavigationEvent.NavigateTo(AppDestination.Login.route))
                }

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
