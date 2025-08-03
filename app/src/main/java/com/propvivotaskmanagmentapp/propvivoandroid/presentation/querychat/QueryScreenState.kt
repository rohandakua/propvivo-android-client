package com.propvivotaskmanagmentapp.propvivoandroid.presentation.querychat

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Message
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task

data class QueryScreenState(
    val task: Task = Task("1", "Task 1", "Description escription is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection", 3600000, 1800000, "", "", 0, 0),
    val talkingTo: String = "Supervisor",
    val messages: List<Message> = listOf(),
    val newMessage: String = "",
    val userIsEmployee: Boolean = true
)
