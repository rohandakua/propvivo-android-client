package com.propvivotaskmanagmentapp.propvivoandroid.presentation.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.admin.AdminDashboardScreen
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.authSection.AuthScreen
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.EmployeeTaskScreen
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.startingscreen.EmployeeStartTaskScreen
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.navigation.AppDestination
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.querychat.QueryScreen
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor.SupervisorDashboardScreen

@Composable
fun AppScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (viewModel.state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {

            NavHost(
                navController = navController,
                modifier = modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .imePadding(),
                startDestination = viewModel.state.startDestination ?: AppDestination.Login.route
            ) {
                composable(
                    route = AppDestination.Login.route,
                ) {
                    AuthScreen(
                        navController = navController
                    )
                }

                composable(
                    route = AppDestination.EmployeeFirstScreen.route
                ) {
                    EmployeeStartTaskScreen(
                        navController = navController
                    )
                }

                composable(
                    route = AppDestination.AdminDashboard.route
                ) {
                    AdminDashboardScreen(
                        navController = navController
                    )
                }

                composable(
                    route = AppDestination.EmployeeDashboard.route
                ) {
                    EmployeeTaskScreen(
                        navController = navController
                    )
                }

                composable(
                    route = AppDestination.TaskQueryScreen(false).route
                ) {
                    QueryScreen()
                }

                composable(
                    route = AppDestination.SupervisorDashboard.route
                ) {
                    SupervisorDashboardScreen(
                        navController = navController
                    )
                }


            }
        }

    }
}