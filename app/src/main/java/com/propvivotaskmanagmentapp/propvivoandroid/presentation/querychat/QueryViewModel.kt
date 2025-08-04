package com.propvivotaskmanagmentapp.propvivoandroid.presentation.querychat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Message
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.TaskQueryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.util.UUID

@HiltViewModel
class QueryViewModel @Inject constructor(
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val queryRepository: TaskQueryInterface,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
    ) : ViewModel() {

    val isUserEmployee = savedStateHandle.get<Boolean>("UserIsEmployee")
    var state by mutableStateOf(
        QueryScreenState(
            userIsEmployee = isUserEmployee?: true, // or false based on user role
            talkingTo = "Supervisor",
            messages = sampleMessages
        )
    )

    fun onEvent(event: QueryScreenEvent) {
        when (event) {
            is QueryScreenEvent.MessageChanged -> {
                state = state.copy(newMessage = event.value)
            }

            is QueryScreenEvent.SendMessage -> {
                if (state.newMessage.isNotBlank()) {
                    val newMessage = Message(
                        id = UUID.randomUUID().toString(),
                        timestamp = System.currentTimeMillis(),
                        sendByEmployee = state.userIsEmployee,
                        taskQueryId = "task1", // replace with dynamic if needed
                        content = state.newMessage.trim()
                    )

                    state = state.copy(
                        messages = state.messages + newMessage,
                        newMessage = ""
                    )
                }
            }
        }
    }

    companion object {
        val sampleMessages = listOf(
            Message(
                "1",
                1000,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "Hello Supervisor"
            ),
            Message(
                "2",
                20002,
                sendByEmployee = false,
                taskQueryId = "task1",
                content = "Yes, tell me"
            ),
            Message(
                "3",
                30002,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "I have a doubt."
            ),
            Message(
                "2",
                20200,
                sendByEmployee = false,
                taskQueryId = "task1",
                content = "Yes, tell me"
            ),
            Message(
                "3",
                30020,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "I have a doubt."
            ),
            Message(
                "2",
                20040,
                sendByEmployee = false,
                taskQueryId = "task1",
                content = "Yes, tell me"
            ),
            Message(
                "3",
                30050,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "I have a doubt."
            ),
            Message(
                "2",
                221000,
                sendByEmployee = false,
                taskQueryId = "task1",
                content = "Yes, tell me"
            ),
            Message(
                "3",
                30400,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "I have a doubt."
            ),
            Message(
                "2",
                20050,
                sendByEmployee = false,
                taskQueryId = "task1",
                content = "Yes, tell me"
            ),
            Message(
                "3",
                300660,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "I have a doubt."
            ),
            Message(
                "2",
                204600,
                sendByEmployee = false,
                taskQueryId = "task1",
                content = "Yes, tell me"
            ),
            Message(
                "3",
                300760,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "I have a doubt."
            ),
            Message(
                "2",
                208700,
                sendByEmployee = false,
                taskQueryId = "task1",
                content = "Yes, tell me"
            ),
            Message(
                "3",
                300980,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "I have a doubt."
            ),
            Message(
                "2",
                20090,
                sendByEmployee = false,
                taskQueryId = "task1",
                content = "Yes, tell me"
            ),
            Message(
                "3",
                38000,
                sendByEmployee = true,
                taskQueryId = "task1",
                content = "I have a doubt."
            )
        )
    }
}
