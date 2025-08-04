package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.EmployeesRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignOutUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var timerJob: Job? = null
    private var timeElapsed: Long = 0L

    private var timePerTask : Long = 0L
    private val saveIntervalMs = 10 * 60 * 1000L // 10 minutes

    private var userId = ""


    init {
        viewModelScope.launch {
            userId = preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_ID, "")

            val tasks = employeeRepository.getAllTask(userId, LocalDate.now())
            state = state.copy(tasks = tasks )
        }
    }

    fun startTimer(taskId: String) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(10000)
                timeElapsed += 10000
                timePerTask +=10000
                updateTimerText()

                if (timeElapsed % saveIntervalMs == 0L) {
                    val userId =
                        preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_ID, "")
                    employeeRepository.updateTaskTimeSpent(timePerTask, taskId)
                    employeeRepository.updateTotalTime(userId, timeElapsed, LocalDate.now())
                }
            }
        }
    }

    private fun updateTimerText() {
        state = state.copy(timerText = HelperFunction.formatMillisToHoursAndMinutes(timeElapsed)
        , timerPerTask = HelperFunction.formatMillisToHoursAndMinutes(timePerTask))
    }

    fun stopTimer() {
        timerJob?.cancel()
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
                    ),
                )
            }
            state = state.copy(showBreakDialog = false)
        }
    }


    fun onEvent(event: EmployeeTaskScreenEvent) {
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
                    if(state.selectedTask == null){
                        state = state.copy(errorMessage = "Select a task first")
                    }
                }
            }

            is EmployeeTaskScreenEvent.RaiseQuery -> {
                // todo move to query screen
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
                    preferenceDataStoreHelper.removePreference(dsConstants.USER_ID)
                    preferenceDataStoreHelper.removePreference(dsConstants.USER_NAME)
                    preferenceDataStoreHelper.removePreference(dsConstants.USER_EMAIL)
                    preferenceDataStoreHelper.removePreference(dsConstants.USER_ROLE)
                }
                //TODO move to login screen
            }

            EmployeeTaskScreenEvent.FinishWork -> {
                viewModelScope.launch {
                    state.selectedTask?.let { employeeRepository.updateTaskTimeSpent(timePerTask, it.id) }
                    employeeRepository.updateTotalTime(userId, timeElapsed, LocalDate.now())
                    stopTimer()
                }

            }
        }
    }

    companion object {
        val sampleTasks = listOf(
            Task(
                "1",
                "Task 1",
                "Description escription is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection",
                3600000,
                1800000,
                "",
                "",
                0,
                0
            ),
            Task(
                "2",
                "Task 2",
                "Description is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection",
                7200000,
                3000000,
                "",
                "",
                0,
                0
            ),
            Task(
                "3",
                "Task 3",
                "Description is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection",
                1800000,
                1000000,
                "",
                "",
                0,
                0
            ),
        )
    }
}
