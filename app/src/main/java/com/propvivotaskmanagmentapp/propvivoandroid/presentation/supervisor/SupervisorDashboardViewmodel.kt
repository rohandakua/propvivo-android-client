package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent.*
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Employee
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.SupervisorRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignOutUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.navigation.AppDestination
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    var userId = ""

    init {
        getList()
        getAllEmployee()
    }

    fun onQueryClicked(taskId: String) {
        viewModelScope.launch {
            _navigationEvent.emit(NavigateTo(AppDestination.TaskQueryScreen(false, taskId).destination))
        }
    }

    fun getList(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            val supervisorId = preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_ID,"")
            userId = supervisorId
            val employeeList = supervisorRepository.getAllEmployee(supervisorId = supervisorId)
            Log.e("SupervisorDashboardViewModel", "going to get list "+ employeeList.toString())
            val employeeWithTasks: List<EmployeeWithTasks> = employeeList.map { employee ->
                val tasks = supervisorRepository.getAllTaskOfADayByEmployeeId(
                    employeeId = employee.uid,
                    date = HelperFunction.todayDate.toLocalDate()
                )
                EmployeeWithTasks(
                    tasks = tasks,
                    id = employee.uid,
                    name = employee.name
                )
            }
            _state.value = _state.value.copy(employees = employeeWithTasks , isLoading = false)
        }
    }

    fun getAllEmployee(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            val employeeList = supervisorRepository.getAllEmployee()
            Log.e("SupervisorDashboardViewModel", "going to get list "+ employeeList.toString())
            _state.value = _state.value.copy(employeeOnlyList = employeeList, isLoading = false )
        }
    }

    private fun addNewTask(
        title: String,
        description: String,
        estimatedTime: String,
        selectedEmployeeId: String
    ) {
        viewModelScope.launch {
            Log.e(
                "EmployeeTaskViewModel",
                "going to add new task"
            )
            val newTask = Task(
                title = title,
                description = description,
                estimatedTimeMs = estimatedTime.toLong() * (60 * 60 * 1000),
                timeSpentMs = 0,
                assignedBy = userId,
                assignedTo = selectedEmployeeId,
                createdAt = System.currentTimeMillis(),
                updatedAt = 0,
                id = ""
            )
            supervisorRepository.addTask(newTask)
            getList()

            _state.value = _state.value.copy(showAddTaskDialog = false)

        }
    }

    fun getListByEmployeeId(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            val employeeId = _state.value.selectedEmployeeId
            if (employeeId != null) {
                val tasks = supervisorRepository.getAllTaskOfADayByEmployeeId(
                    employeeId = employeeId,
                    date = HelperFunction.todayDate.toLocalDate()
                )
                val employee: Employee = supervisorRepository.getEmployeeById(employeeId)
                val employeeWithTasks: List<EmployeeWithTasks> = listOf(
                    EmployeeWithTasks(
                        tasks = tasks,
                        id = employeeId,
                        name = employee.name
                    )
                )
                _state.value = _state.value.copy(employees = employeeWithTasks , isLoading = false)
            }else{
                _state.value = _state.value.copy(isLoading = false, errorMessage = "No employee selected")
            }
        }
    }

    fun onEvent(event: SupervisorDashboardEvent) {
        _state.value = _state.value.copy(errorMessage = null)
        when (event) {
            is SupervisorDashboardEvent.SelectEmployee -> {
                _state.value = _state.value.copy(selectedEmployeeId = event.employeeId)
            }

            is SupervisorDashboardEvent.AddTaskClicked -> {
                // Future: show add task dialog
                _state.value = _state.value.copy(showAddTaskDialog = true)

            }
            is SupervisorDashboardEvent.SaveNewTaskClicked -> {
                if (event.selectedEmployeeId.isEmpty() || event.selectedEmployeeId.isBlank()){
                    _state.value = _state.value.copy(errorMessage = "Please select an employee")
                    return
                }
                addNewTask(
                    title = event.title,
                    description = event.description,
                    estimatedTime = event.estimatedTime,
                    selectedEmployeeId = event.selectedEmployeeId
                )
            }

            is SupervisorDashboardEvent.FilterApplied -> {
                _state.value = _state.value.copy(selectedEmployeeId = event.employeeId)
                getListByEmployeeId()
                _state.value = _state.value.copy(showFilterDialog = false)
            }
            is SupervisorDashboardEvent.ResolveQuery -> {
                onQueryClicked(event.taskId)
            }

            SupervisorDashboardEvent.Logout -> {
                viewModelScope.launch {
                    signOutUseCase.invoke()

                    preferenceDataStoreHelper.clearAllPreference()

                    _navigationEvent.emit(NavigateTo(AppDestination.Login.route))
                }

            }

            SupervisorDashboardEvent.FilterClicked -> {
                _state.value = _state.value.copy(showFilterDialog = true)
            }

            SupervisorDashboardEvent.DismissAddTaskDialog -> {
                _state.value = _state.value.copy(showAddTaskDialog = false)
            }
            SupervisorDashboardEvent.DismissFilterDialog -> {
                _state.value = _state.value.copy(showFilterDialog = false)
            }


        }
    }

    companion object {
//        val sampleEmployees = listOf<EmployeeWithTasks>()
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
