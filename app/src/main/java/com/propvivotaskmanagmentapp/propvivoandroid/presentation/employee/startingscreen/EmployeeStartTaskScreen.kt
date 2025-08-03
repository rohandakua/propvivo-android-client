package com.propvivotaskmanagmentapp.propvivoandroid.presentation.employee.startingscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Summary
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.components.SummaryLabel
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.theme.AppTheme


@Composable
fun EmployeeStartTaskScreen(
    viewModel: EmployeeStartTaskViewModel = hiltViewModel()
) {
    val state = viewModel.state
    EmployeeStartTaskScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeStartTaskScreenContent(
    state: EmployeeStartTaskScreenState,
    onEvent: (EmployeeStartTaskScreenEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { onEvent(EmployeeStartTaskScreenEvent.Logout) }) {
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Number of task for today = " + state.noOfTask.toString(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(30.dp))
            Button(onClick = { onEvent(EmployeeStartTaskScreenEvent.StartDay) }) {
                Text("Start Day")
            }
            Spacer(Modifier.height(30.dp))

            Text(
                text = "Summary",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))

            SummaryLabel(summary = state.summary)

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EmployeeTaskScreenPreview() {
    AppTheme {
        EmployeeStartTaskScreenContent(
            state = EmployeeStartTaskScreenState(
                noOfTask = 10,
                summary = Summary.IN_PROGRESS
            ),
            onEvent = {}
        )
    }
}


