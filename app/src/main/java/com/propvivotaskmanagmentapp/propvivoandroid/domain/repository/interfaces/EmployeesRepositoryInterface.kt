package com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.TaskQuery
import java.time.LocalDate

interface EmployeesRepositoryInterface {
    suspend fun getAllTask(employeeId:String, date: LocalDate) : List<Task>

    suspend fun getTaskQueryList(employeeId: String, taskId: String) : List<TaskQuery>

    suspend fun addTask(task: Task)

    suspend fun updateTaskTimeSpent(updatedTime : Long , taskId: String)

    suspend fun updateTotalTime(updatedTime : Long , date: LocalDate)
}