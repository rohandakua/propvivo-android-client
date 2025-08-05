package com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces

import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.TaskQueryStatus
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Message
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task

interface TaskQueryInterface {
    suspend fun getAllMessages(taskQueryId: String) : List<Message>

    suspend fun sendMessage(message: Message)

    suspend fun updateQueryStatus ( taskQueryId: String, taskQueryStatus: TaskQueryStatus)

    suspend fun getTask(taskId: String): Task?
}