package com.propvivotaskmanagmentapp.propvivoandroid.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Employee


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    employees: List<Employee>?,
    onDismissRequest: () -> Unit,
    onSaveClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    var selectedEmployeeName by remember { mutableStateOf(employees?.firstOrNull()?.name.orEmpty()) }
    var selectedEmployeeId by remember { mutableStateOf(employees?.firstOrNull()?.id) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    Log.e("alert dialog box","saved clicked")
                    selectedEmployeeId?.let { onSaveClick(it) }

                },
                enabled = selectedEmployeeId!=null || (selectedEmployeeId?: "").isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = { Text("Filter by Employee") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

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
