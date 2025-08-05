package com.propvivotaskmanagmentapp.propvivoandroid.presentation.querychat

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Message
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task

data class QueryScreenState(
    val task: Task = Task(),
    val talkingTo: String = "Supervisor",
    val messages: List<Message> = listOf(),
    val newMessage: String = "",
    val userIsEmployee: Boolean = true,
    val isLoading : Boolean = false,
    val isMessageLoading : Boolean = false,
    val errorMessage : String = "",
    val canTalk : Boolean = true
)
