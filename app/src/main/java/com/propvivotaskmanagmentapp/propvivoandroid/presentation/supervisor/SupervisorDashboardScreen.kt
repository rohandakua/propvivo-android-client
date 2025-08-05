package com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.components.AddTaskDialog
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.components.FilterDialog
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.components.TaskItem
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.EmployeeTaskScreenEvent
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.theme.AppTheme

@Composable
fun SupervisorDashboardScreen(
    viewModel: SupervisorDashboardViewModel = hiltViewModel(),
    navController : NavHostController
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.navigationEvent
            .collect { event ->
                when (event) {
                    is NavigationEvent.NavigateTo -> navController.navigate(event.route)
                    is NavigationEvent.NavigateBack -> navController.popBackStack()
                }
            }
    }
    if(state.showAddTaskDialog){
        Log.e("SupervisorDashboardScreen", "going to show add task dialog "+state.employeeOnlyList.toString())
        AddTaskDialog(
            employees = state.employeeOnlyList,
            onDismissRequest = {
                viewModel.onEvent(SupervisorDashboardEvent.DismissAddTaskDialog)
            },
            onSaveClick = { title, description, estimatedHours, selectedEmployeeId->
                viewModel.onEvent(SupervisorDashboardEvent.SaveNewTaskClicked(title , description , estimatedHours, selectedEmployeeId?: "" ))
            }
        )
    }
    if(state.showFilterDialog){
        FilterDialog(
            employees = state.employeeOnlyList,
            onDismissRequest = { viewModel.onEvent(SupervisorDashboardEvent.DismissFilterDialog) },
            onSaveClick = { viewModel.onEvent(SupervisorDashboardEvent.FilterApplied(state.selectedEmployeeId ?: "")) }
        )
    }
    SupervisorDashboardScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}
@Composable
fun SupervisorDashboardScreenContent(
    state: SupervisorDashboardState,
    onEvent: (SupervisorDashboardEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onEvent(SupervisorDashboardEvent.FilterClicked) }) {
                    Text("Filter by")
                }
                Button(onClick = { onEvent(SupervisorDashboardEvent.Logout) }) {
                    Text("Logout")
                }
            }
        },
        bottomBar = {
            Column {
                state.errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }

                Button(
                    onClick = { onEvent(SupervisorDashboardEvent.AddTaskClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Add task")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp) // ensures space for bottom button
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder,
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        state.employees.forEach { employee ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                border = ButtonDefaults.outlinedButtonBorder,
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(
                                        employee.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        employee.tasks.forEach { task ->
                                            TaskItem(task = task,
                                                isSupervisor = true
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun SupervisorDashboardPreview(modifier: Modifier = Modifier) {
    AppTheme {
        SupervisorDashboardScreenContent(
            state = SupervisorDashboardState(
                employees = SupervisorDashboardViewModel.sampleEmployees,
                errorMessage = "this is an error"
            ),
            onEvent = {}
        )
    }
}
@Preview(showSystemUi = true)
@Composable
fun SupervisorDashboardPreview1(modifier: Modifier = Modifier) {
    AppTheme {
        SupervisorDashboardScreenContent(
            state = SupervisorDashboardState(
                employees = SupervisorDashboardViewModel.sampleEmployees,
            ),
            onEvent = {}
        )
    }
}

