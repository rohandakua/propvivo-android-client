package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class EmployeeTaskViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(EmployeeTaskScreenState(
        tasks = sampleTasks
    ))
        private set

    fun onEvent(event: EmployeeTaskScreenEvent) {
        when (event) {
            is EmployeeTaskScreenEvent.SelectTask -> {
                val selected = state.tasks.find { it.id == event.taskId }
                state = state.copy(selectedTask = selected)
            }

            is EmployeeTaskScreenEvent.StartTask -> {
                println("Start task: ${event.taskId}")
            }

            is EmployeeTaskScreenEvent.TogglePauseResume -> {
                println("Toggle pause/resume: ${event.taskId}")
            }

            is EmployeeTaskScreenEvent.ShowTimer -> {
                println("Show timer for: ${event.taskId}")
            }

            is EmployeeTaskScreenEvent.RaiseQuery -> {
                println("Query raised for: ${event.taskId}")
            }

            EmployeeTaskScreenEvent.TakeBreak -> {
                println("Taking break")
            }

            EmployeeTaskScreenEvent.AddTaskClicked -> {
                println("Navigate to Add Task screen")
            }

            EmployeeTaskScreenEvent.Logout -> {
                println("Logging out")
            }

            EmployeeTaskScreenEvent.FinishWork -> {
                println("Finishing work")
            }
        }
    }

    companion object {
        val sampleTasks = listOf(
            Task("1", "Task 1", "Description escription is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection", 3600000, 1800000, "", "", 0, 0),
            Task("2", "Task 2", "Description is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection", 7200000, 3000000, "", "", 0, 0),
            Task("3", "Task 3", "Description is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection", 1800000, 1000000, "", "", 0, 0),
        )
    }
}
