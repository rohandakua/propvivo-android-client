package com.propvivotaskmanagmentapp.propvivoandroid.presentation.components
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.NavigationEvent
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Summary
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.components.SummaryLabel
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.supervisor.EmployeeWithTasks
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    employees: List<EmployeeWithTasks>?,
    onDismissRequest: () -> Unit,
    onSaveClick: (title: String, description: String, estimatedHours: String, selectedEmployeeId: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var estimatedTime by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    var selectedEmployeeName by remember { mutableStateOf(employees?.firstOrNull()?.name.orEmpty()) }
    var selectedEmployeeId by remember { mutableStateOf(employees?.firstOrNull()?.id) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    Log.e("alert dialog box","saved clicked")
                    onSaveClick(title, description, estimatedTime, null)
                },
                enabled = title.isNotBlank() && description.isNotBlank() && estimatedTime.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = { Text("Add Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estimatedTime,
                    onValueChange = { estimatedTime = it },
                    label = { Text("Estimated Time (hrs)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Show dropdown only if employees list is not null or empty
                if (!employees.isNullOrEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedEmployeeName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Assign to") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            employees.forEach { employee ->
                                DropdownMenuItem(
                                    text = { Text(employee.name) },
                                    onClick = {
                                        selectedEmployeeName = employee.name
                                        selectedEmployeeId = employee.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
