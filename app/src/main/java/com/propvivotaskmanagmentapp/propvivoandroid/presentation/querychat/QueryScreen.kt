package com.propvivotaskmanagmentapp.propvivoandroid.presentation.querychat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.components.TaskItem
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.querychat.QueryViewModel.Companion.sampleMessages
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.theme.AppTheme
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction

@Composable
fun QueryScreen(
    viewModel: QueryViewModel = hiltViewModel()
) {
    val state = viewModel.state
    QueryScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueryScreenContent(
    state: QueryScreenState,
    onEvent: (QueryScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Query") }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.newMessage,
                    onValueChange = { onEvent(QueryScreenEvent.MessageChanged(it)) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") }
                )
                IconButton(onClick = { onEvent(QueryScreenEvent.SendMessage) }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                LazyColumn(Modifier.padding(12.dp)) {
                    item { TaskItem(task = state.task) }
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("Talking to ${state.talkingTo}", color = MaterialTheme.colorScheme.onPrimaryContainer , fontWeight = FontWeight.Bold)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.messages.sortedBy { it.timestamp }) { message ->
                    val isUserMessage = if (state.userIsEmployee) {
                        message.sendByEmployee
                    } else {
                        !message.sendByEmployee
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
                    ) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .fillMaxWidth(0.75f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = message.content)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
                                ) {
                                    Text(
                                        text = HelperFunction.formatMillisToHoursAndMinutes(message.timestamp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline
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


@Preview(showSystemUi = true)
@Composable
fun QueryScreenPreview() {
    AppTheme {
        QueryScreenContent(
            state = QueryScreenState(
                talkingTo = "Supervisor",
                messages = sampleMessages,
                userIsEmployee = true // or false to preview as supervisor
            ),
            onEvent = {}
        )
    }
}

