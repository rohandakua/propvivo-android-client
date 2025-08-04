package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.components.TaskItem
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.theme.AppTheme
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent


@Composable
fun EmployeeTaskScreen(
    viewModel: EmployeeTaskViewModel = hiltViewModel(),
    navController : NavHostController
) {
    val state = viewModel.state
    LaunchedEffect(Unit) {
        viewModel.navigationEvent
            .collect { event ->
                when (event) {
                    is NavigationEvent.NavigateTo -> navController.navigate(event.route)
                    is NavigationEvent.NavigateBack -> navController.popBackStack()
                }
            }
    }
    EmployeeTaskScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeTaskScreenContent(
    state: EmployeeTaskScreenState,
    onEvent: (EmployeeTaskScreenEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Automatically scroll to selected task when it changes
    LaunchedEffect(state.selectedTask) {
        val selectedIndex = state.tasks.indexOfFirst { it.id == state.selectedTask?.id }
        if (selectedIndex >= 0) {
            listState.animateScrollToItem(selectedIndex)
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onEvent(EmployeeTaskScreenEvent.FinishWork) }) {
                    Text("Finish")
                }
                Button(onClick = { onEvent(EmployeeTaskScreenEvent.Logout) }) {
                    Text("Logout")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Timer: ${state.timerText}", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(16.dp))

            Text("Working on", fontWeight = FontWeight.Bold)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                val selectedTitle = state.selectedTask?.title ?: "Select a task"

                TextField(
                    value = selectedTitle,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Task") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.tasks.forEach { task ->
                        DropdownMenuItem(
                            text = { Text(task.title) },
                            onClick = {
                                onEvent(EmployeeTaskScreenEvent.SelectTask(task.id))
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.size(16.dp))
            Card(
                modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(10.dp),
                    state = listState
                ) {
                    items(state.tasks.size) { index ->
                        val task = state.tasks[index]
                        TaskItem(task = task)
                        Spacer(Modifier.size(10.dp))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onEvent(EmployeeTaskScreenEvent.TakeBreak) }) {
                    Text("Take Break")
                }
                Button(onClick = { onEvent(EmployeeTaskScreenEvent.AddTaskClicked) }) {
                    Text("Add new task")
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EmployeeTaskScreenPreview() {
    AppTheme {
        EmployeeTaskScreenContent(
            state = EmployeeTaskScreenState(
                tasks = EmployeeTaskViewModel.sampleTasks,
                selectedTask = EmployeeTaskViewModel.sampleTasks[0],
                timerText = "01:23:45",
            ),
            onEvent = {}
        )
    }
}


