package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.startingscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.EmployeesRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.PreferenceDataStoreInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignOutUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.navigation.AppDestination
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EmployeeStartTaskViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val employeeRepository: EmployeesRepositoryInterface
) : ViewModel() {

    var state by mutableStateOf(EmployeeStartTaskScreenState(
    ))
        private set

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun getNoOfTask() {
        viewModelScope.launch {
            val userId = preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_ID,"")
            val tasks = employeeRepository.getAllTask(userId, HelperFunction.todayDate.toLocalDate())
            state = state.copy(noOfTask = tasks.size)
        }
    }
    init {
        getNoOfTask()
    }
    fun onStartClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateTo(AppDestination.EmployeeDashboard.route))
        }
    }

    fun onEvent(event: EmployeeStartTaskScreenEvent) {
        when (event) {
            is EmployeeStartTaskScreenEvent.Logout -> {
                viewModelScope.launch {
                    signOutUseCase.invoke()
                    preferenceDataStoreHelper.clearAllPreference()
                    _navigationEvent.emit(NavigationEvent.NavigateTo(AppDestination.Login.route))
                }
            }
            is EmployeeStartTaskScreenEvent.StartDay -> {
                onStartClicked()
            }
        }
    }

}
