package com.propvivotaskmanagmentapp.propvivoandroid.presentation.admin


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(
        AdminDashboardState(
            employees = sampleEmployees
        )
    )
    val state: androidx.compose.runtime.State<AdminDashboardState> = _state

    fun onEvent(event: AdminDashboardEvent) {
        when (event) {
            AdminDashboardEvent.Logout -> {
                // handle logout
            }
        }
    }

    companion object {
        val sampleEmployees = listOf(
            Employee("1", "Alice", true),
            Employee("2", "Bob", true),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", false),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", false),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", false),
            Employee("2", "Bob", true),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", true),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", false),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", false),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", false),
            Employee("2", "Bob", true),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", true),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", false),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", false),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", false),
            Employee("2", "Bob", true),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", true),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", false),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", true),
            Employee("2", "Bob", false),
            Employee("3", "Charlie", true),
            Employee("1", "Alice", false),
            Employee("2", "Bob", true),
            Employee("3", "Charlie", true),
        )
    }
}
