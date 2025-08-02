package com.propvivotaskmanagmentapp.propvivoandroid.presentation.authSection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Role
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.theme.AppTheme


@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel(),
               modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        AuthScreenContent(
            state = viewModel.state,
            onEvent = viewModel::onEvent
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreenContent(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Toggle buttons
        Row {
            TextButton(
                onClick = { onEvent(AuthEvent.ToggleAuthMode(true)) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (state.isSignIn) MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text("Sign In")
            }
            TextButton(
                onClick = { onEvent(AuthEvent.ToggleAuthMode(false)) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (!state.isSignIn) MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text("Register")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (!state.isSignIn) {
            Spacer(modifier = Modifier.height(12.dp))

            var expanded by remember { mutableStateOf(false) }
            val roles = Role.entries.map { it.role }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.role.role,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Role") },
                    trailingIcon = {
                        TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    roles.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role) },
                            onClick = {
                                onEvent(AuthEvent.RoleChanged(Role.valueOf(role)))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onEvent(AuthEvent.Submit) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(if (state.isSignIn) "Sign In" else "Register")
            }
        }

        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showSystemUi = true, name = "Sign In UI")
@Composable
fun SignInPreview() {
    AppTheme {
        AuthScreenContent(
            state = AuthState(
                isSignIn = true,
                email = "email@example.com",
                password = "password"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true, name = "Register UI")
@Composable
fun RegisterPreview() {
    AppTheme {
        AuthScreenContent(
            state = AuthState(
                isSignIn = false,
                email = "email@example.com",
                password = "password",
                role = Role.Admin
            ),
            onEvent = {}
        )
    }
}

