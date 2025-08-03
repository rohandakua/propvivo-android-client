package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.startingscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class EmployeeStartTaskViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(EmployeeStartTaskScreenState(
    ))
        private set

    fun onEvent(event: EmployeeStartTaskScreenEvent) {
        when (event) {
            is EmployeeStartTaskScreenEvent.Logout -> {
                // Handle logout
            }
            is EmployeeStartTaskScreenEvent.StartDay -> {

            }
        }
    }

}
