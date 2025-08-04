package com.propvivotaskmanagmentapp.propvivoandroid.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppDestination(
    val route: String,
    val destination: String = route,
    val args: List<NamedNavArgument> = emptyList(),
) {
    data object Login : AppDestination(
        route = "login"
    )

    data object EmployeeFirstScreen: AppDestination(
        route = "employeeFirstScreen"
    )

    data object EmployeeDashboard: AppDestination(
        route = "employeeDashboard"
    )

    data object SupervisorDashboard: AppDestination(
        route = "supervisorDashboard"
    )

    data object AdminDashboard: AppDestination(
        route = "adminDashboard"
    )

    data class TaskQueryScreen( val userIsEmployee: Boolean): AppDestination(
        route = "taskQueryScreen?UserIsEmployee={userIsEmployee}",
        destination = "taskQueryScreen${if (userIsEmployee) "?UserIsEmployee=true" else "?UserIsEmployee=true"}",
        args = listOf(
            navArgument("userIsEmployee") {
                type = NavType.BoolType
                nullable = false
                defaultValue = true
            }
        )
    )
}