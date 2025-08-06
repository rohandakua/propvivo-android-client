package com.propvivotaskmanagmentapp.propvivoandroid.presentation.app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.EmployeesRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val employeesRepository: EmployeesRepositoryInterface

) : ViewModel() {
    var state by mutableStateOf(AppState())
        private set

    init {
        callEachTime()

    }
    fun callEachTime(){
        viewModelScope.launch {
            state = state.copy(isLoading = true, user = null)
            getStartDestination()
        }
    }

    suspend fun getStartDestination() {
        val userId = preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_ID, "")
        val userName = preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_NAME, "")
        val userRole = preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_ROLE, "")
        val userEmail = preferenceDataStoreHelper.getFirstPreference(dsConstants.USER_EMAIL, "")
        val user = User(uid = userId, email = userEmail, role = userRole, name = userName)
        val timeSpentToday =
            employeesRepository.getTotalTime(userId, HelperFunction.todayDate.toLocalDate())
        when (user.role) {
            "Employee" -> {
                state = state.copy(isEmployeeInFirstScreen = timeSpentToday == 0L)
            }
        }
        state = state.copy(user = user, isLoading = false)

    }

}