package com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.TaskQuery
import java.time.LocalDate
import java.util.Date

interface EmployeesRepositoryInterface {
    suspend fun getAllTask(employeeId:String, date: LocalDate) : List<Task>

    suspend fun getTaskQueryList(taskId: String) : List<TaskQuery>

    suspend fun addTask(task: Task)

    suspend fun updateTaskTimeSpent(updatedTime : Long , taskId: String)

    suspend fun updateTotalTime(userId: String,updatedTime : Long , date: LocalDate)

    suspend fun getTotalTime(userId: String, date: LocalDate): Long

}