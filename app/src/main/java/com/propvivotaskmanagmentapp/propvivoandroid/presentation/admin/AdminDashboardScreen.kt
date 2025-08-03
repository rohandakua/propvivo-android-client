package com.propvivotaskmanagmentapp.propvivoandroid.presentation.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.theme.AppTheme

@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state

    AdminDashboardScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun AdminDashboardScreenContent(
    state: AdminDashboardState,
    onEvent: (AdminDashboardEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { onEvent(AdminDashboardEvent.Logout) }) {
                    Text("Logout")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Number of task for today = " + state.noOfTask.toString(),
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Employee name" , style = MaterialTheme.typography.titleMedium)
                        Text("Status", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    LazyColumn {
                        items(state.employees.size) { index ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• ${state.employees[index].name}")
                                Text(if (state.employees[index].timeCompleted) " - Completed" else " - Not completed",
                                    color = if(state.employees[index].timeCompleted) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error)
                            }
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AdminDashboardPreview() {
    AppTheme {
        AdminDashboardScreenContent(
            state = AdminDashboardState(
                employees = AdminDashboardViewModel.sampleEmployees
            ),
            onEvent = {}
        )
    }
}


