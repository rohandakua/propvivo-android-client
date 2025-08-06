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

    data class TaskQueryScreen(
        val userIsEmployee: Boolean,
        val taskId: String
    ) : AppDestination(
        route = "taskQueryScreen?userIsEmployee={userIsEmployee}&taskId={taskId}",
        destination = "taskQueryScreen?userIsEmployee=$userIsEmployee&taskId=$taskId",
        args = listOf(
            navArgument("userIsEmployee") {
                type = NavType.BoolType
            },
            navArgument("taskId") {
                type = NavType.StringType
            }
        )
    )



}