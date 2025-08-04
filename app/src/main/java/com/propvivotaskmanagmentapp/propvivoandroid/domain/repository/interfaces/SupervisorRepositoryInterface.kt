package com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.TaskQuery
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import java.time.LocalDate

interface SupervisorRepositoryInterface {
    suspend fun getAllEmployee (supervisorId: String) : List<User>

    suspend fun getAllTaskOfADayByEmployeeId (employeeId: String, date: LocalDate): List<Task>

    suspend fun addTask(task: Task)

    suspend fun getTaskQueryList(taskId: String) : List<TaskQuery>

}