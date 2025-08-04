package com.propvivotaskmanagmentapp.propvivoandroid.presentation.app

import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Role
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.navigation.AppDestination

data class AppState (
    val user: User? = null,
    val isEmployeeInFirstScreen : Boolean = false,
    val isLoading:Boolean = true
){
    val isLoggedIn: Boolean
        get() = (user==null)
    val startDestination: String?
        get() = if(isLoggedIn && user!=null && user.role == Role.Employee.role){
            if(isEmployeeInFirstScreen) AppDestination.EmployeeFirstScreen.route else AppDestination.EmployeeDashboard.route
        } else if (isLoggedIn && user!=null && user!!.role == Role.Supervisor.role){
            AppDestination.SupervisorDashboard.route
        } else if (isLoggedIn && user!=null && user!!.role == Role.Admin.role){
            AppDestination.AdminDashboard.route
        } else {
            null
        }
}