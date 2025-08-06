package com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Employee
import java.util.Date

interface AdminRepositoryInterface {
    suspend fun getTotalNoOfTask (date: Date) : Int

    suspend fun getAllEmployeeWithStatus(date: Date) : List<Employee>
}