package com.propvivotaskmanagmentapp.propvivoandroid.presentation.querychat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Message
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.TaskQueryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class QueryViewModel @Inject constructor(
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    private val queryRepository: TaskQueryInterface,
    savedStateHandle: androidx.lifecycle.SavedStateHandle
    ) : ViewModel() {
    val isUserEmployee = savedStateHandle.get<Boolean>("userIsEmployee")
    val taskId = savedStateHandle.get<String>("taskId")
    var state by mutableStateOf(
        QueryScreenState(
            userIsEmployee = isUserEmployee?: true,
            talkingTo = if(isUserEmployee?: true) "Supervisor" else "Employee",
        )
    )
    init {
        getTask()
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                getMessages()
                updateCanTalk()
                delay(10000)
            }
        }
    }
    fun updateCanTalk(){
        if( state.task.assignedBy.isNotBlank() && state.task.assignedTo == state.task.assignedBy ){
            state = state.copy(errorMessage = "Can't talk to the Employee (Self Task)", canTalk = false)
        }else{
            state = state.copy(canTalk = true , errorMessage = "")
        }
    }
    fun getTask(){
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isLoading = true , isMessageLoading = true)
            if (taskId != null) {
                val task = queryRepository.getTask(taskId)
                if(task != null) {
                    state = state.copy(task = task , isLoading = false)
                }else{
                    state = state.copy(errorMessage = "Task not found", isLoading = false)
                }
            }else{
                state = state.copy(errorMessage = "Task not found", isLoading = false)

            }
        }
    }

    fun getMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isMessageLoading = true)
            if (taskId != null) {
                val messages = queryRepository.getAllMessages(taskId)
                state = state.copy(messages = messages, isMessageLoading = false)
            }else{
                state = state.copy(errorMessage = "Task not found", isMessageLoading = false)
            }
        }
    }

    fun sendMessage (){
        viewModelScope.launch(Dispatchers.IO) {
            if(!state.canTalk){
                state = state.copy(errorMessage = "Can't talk to the Employee (Self Task)")
                return@launch
            }
            val newMessage = Message(
                timestamp = System.currentTimeMillis(),
                sendByEmployee = state.userIsEmployee,
                taskQueryId = state.task.id,
                content = state.newMessage.trim()
            )
            state = state.copy(
                newMessage = ""
            )
            queryRepository.sendMessage(newMessage)
            getMessages()
        }
    }

    fun onEvent(event: QueryScreenEvent) {
        state = state.copy(errorMessage = "")
        when (event) {
            is QueryScreenEvent.MessageChanged -> {
                state = state.copy(newMessage = event.value)
            }

            is QueryScreenEvent.SendMessage -> {
                if (state.newMessage.isNotBlank()) {
                    sendMessage()
                } else {
                    state = state.copy(errorMessage = "Message cannot be empty")
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
