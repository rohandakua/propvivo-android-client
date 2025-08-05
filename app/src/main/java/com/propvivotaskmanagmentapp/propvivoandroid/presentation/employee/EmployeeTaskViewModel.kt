package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent.NavigateTo
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.EmployeesRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignOutUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.navigation.AppDestination
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class EmployeeTaskViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val employeeRepository: EmployeesRepositoryInterface
) : ViewModel() {

    var state by mutableStateOf(
        EmployeeTaskScreenState(
            tasks = sampleTasks,
        )
    )
        private set

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onQueryClicked() {
        viewModelScope.launch {
            if (state.selectedTask != null && state.selectedTask?.assignedBy == state.selectedTask?.assignedTo) {
                state = state.copy(errorMessage = "You cannot raise a query for your own task")
                return@launch
            } else if (state.selectedTask == null) {
                state = state.copy(errorMessage = "Select a task first")
                return@launch
            } else
                _navigationEvent.emit(NavigationEvent.NavigateTo(AppDestination.TaskQueryScreen(true).destination))

        }
    }

    private var timerJob: Job? = null
    private var timeElapsed: Long = 0L

    private var timePerTask: Long = 0L
    private val saveIntervalMs = 60 * 1000L

    private var userId = ""


    init {
        viewModelScope.launch(Dispatchers.IO) {
            userId = preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_ID, "")
            timeElapsed = employeeRepository.getTotalTime(userId, HelperFunction.todayDate.toLocalDate())
            updateTasks()
            updateTimerText()
        }
    }
    suspend fun updateTasks(){
        val tasks = employeeRepository.getAllTask(userId, HelperFunction.todayDate.toLocalDate())
        Log.e("EmployeeTaskViewModel", "updateTasks: $tasks")
        state = state.copy(tasks = tasks)
    }
    fun startTimer(taskId: String) {
        timerJob?.cancel()
        state = state.copy(isTimerWorking = true)
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(10000)
                timeElapsed += 10000
                timePerTask += 10000
                updateTimerText()
                if (timeElapsed % saveIntervalMs == 0L) {
                    val userId =
                        preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_ID, "")
                    employeeRepository.updateTaskTimeSpent(timePerTask, taskId)
                    employeeRepository.updateTotalTime(userId, timeElapsed, LocalDate.now())
                    val tasks = employeeRepository.getAllTask(userId, HelperFunction.todayDate.toLocalDate())
                    state = state.copy(tasks = tasks)
                }
            }
        }
    }

    private fun updateTimerText() {
        Log.e("EmployeeTaskViewModel", "updateTimerText: $timeElapsed")
        Log.e("EmployeeTaskViewModel", "updateTimerText: $timePerTask")
        state = state.copy(
            timerText = HelperFunction.formatMillisToHoursAndMinutes(timeElapsed),
            timerPerTask = HelperFunction.formatMillisToHoursAndMinutes(timePerTask)
        )
    }

    fun stopTimer() {
        timerJob?.cancel()
        state = state.copy(isTimerWorking = false)
        timerJob = null
    }

    fun takeBreak() {
        stopTimer()
        state = state.copy(showBreakDialog = true)
        startBreakTimer()
    }

    private fun startBreakTimer() {
        timerJob = viewModelScope.launch {
            var breakTime = 30 * 60 // 30 minutes in seconds
            while (breakTime > 0) {
                delay(1000)
                breakTime--
                state = state.copy(
                    timerText = "Break: %02d:%02d".format(
                        breakTime / 60,
                        breakTime % 60
                    )
                )
            }
            state = state.copy(showBreakDialog = false)
        }
    }

    private fun addNewTask(
        title: String,
        description: String,
        estimatedTime: String,
        selectedEmployeeId: String?
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
                assignedTo = userId,
                createdAt = System.currentTimeMillis(),
                updatedAt = 0,
                id = ""
            )
            employeeRepository.addTask(newTask)
            updateTasks()

            state = state.copy(showAddTaskDialog = false)

        }
    }
    fun dismissBreakDialog() {
        timerJob?.cancel() // cancel break timer
        timerJob = null
        state = state.copy(showBreakDialog = false)
        updateTimerText()
    }

    fun onEvent(event: EmployeeTaskScreenEvent) {
        state = state.copy(errorMessage = null)
        when (event) {
            is EmployeeTaskScreenEvent.SelectTask -> {

                val selected = state.tasks.find { it.id == event.taskId }
                timePerTask = selected?.timeSpentMs ?: 0L
                state = state.copy(selectedTask = selected)
                updateTimerText()
            }

            is EmployeeTaskScreenEvent.StartTask -> {
                state.selectedTask?.let { startTimer(it.id) }
            }

            is EmployeeTaskScreenEvent.TogglePauseResume -> {
                if (timerJob?.isActive == true) {
                    stopTimer()
                } else {
                    state.selectedTask?.let { startTimer(it.id) }
                    if (state.selectedTask == null) {
                        state = state.copy(errorMessage = "Select a task first")
                    }
                }
            }

            is EmployeeTaskScreenEvent.RaiseQuery -> {
                onQueryClicked()
            }

            is EmployeeTaskScreenEvent.SaveNewTaskClicked -> {
                addNewTask(event.title, event.description, event.estimatedTime, event.selectedEmployeeId)
            }

            EmployeeTaskScreenEvent.TakeBreak -> {
                takeBreak()
            }

            EmployeeTaskScreenEvent.AddTaskClicked -> {
                state = state.copy(showAddTaskDialog = true)
            }

            EmployeeTaskScreenEvent.Logout -> {
                viewModelScope.launch {
                    signOutUseCase.invoke()
                    preferenceDataStoreHelper.clearAllPreference()
                }
                viewModelScope.launch {
                    _navigationEvent.emit(NavigateTo(AppDestination.Login.route))
                }
            }

            EmployeeTaskScreenEvent.FinishWork -> {
                viewModelScope.launch {
                    try {
                        state.selectedTask?.let {
                            employeeRepository.updateTaskTimeSpent(
                                timePerTask,
                                it.id
                            )
                        }
                        employeeRepository.updateTotalTime(userId, timeElapsed, LocalDate.now())
                    } catch (e: Exception) {
                        Log.e(
                            "EmployeeTaskViewModel",
                            "Error updating task time spent or total time: ${e.message}"
                        )
                    }
                    stopTimer()
                    _navigationEvent.emit(NavigateTo(AppDestination.EmployeeFirstScreen.route))
                }

            }

            EmployeeTaskScreenEvent.DismissAddTaskDialog -> {
                state = state.copy(showAddTaskDialog = false)
            }
        }
    }

    companion object {
        val sampleTasks = listOf<Task>()
//            Task(
//                "1",
//                "Task 1",
//                "Description escription is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection",
//                3600000,
//                1800000,
//                "",
//                "",
//                0,
//                0
//            ),
//            Task(
//                "2",
//                "Task 2",
//                "Description is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection",
//                7200000,
//                3000000,
//                "",
//                "",
//                0,
//                0
//            ),
//            Task(
//                "3",
//                "Task 3",
//                "Description is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection",
//                1800000,
//                1000000,
//                "",
//                "",
//                0,
//                0
//            ),
//        )
    }
}
