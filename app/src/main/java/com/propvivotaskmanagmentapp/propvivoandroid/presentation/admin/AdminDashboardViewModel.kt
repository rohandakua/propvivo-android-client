package com.propvivotaskmanagmentapp.propvivoandroid.presentation.admin


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Employee
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AdminRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignOutUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.navigation.AppDestination
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction.todayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val adminRepository: AdminRepositoryInterface,
    private val signOutUseCase: SignOutUseCase,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper
) : ViewModel() {
    private val _state = mutableStateOf(
        AdminDashboardState(employees = emptyList())
    )
    val state: androidx.compose.runtime.State<AdminDashboardState> = _state
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()



    init {
        viewModelScope.launch {
            val employees = adminRepository.getAllEmployeeWithStatus(todayDate)
            val noOfTask = adminRepository.getTotalNoOfTask(todayDate)
            _state.value = _state.value.copy(employees = employees,noOfTask = noOfTask, isLoading = false)
        }
    }

    fun onEvent(event: AdminDashboardEvent) {
        when (event) {
            AdminDashboardEvent.Logout -> {
                viewModelScope.launch {
                    signOutUseCase.invoke()

                    preferenceDataStoreHelper.clearAllPreference()

                    _navigationEvent.emit(NavigationEvent.NavigateTo(AppDestination.Login.route))
                }
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
