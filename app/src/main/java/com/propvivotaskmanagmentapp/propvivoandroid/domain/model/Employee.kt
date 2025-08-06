package com.propvivotaskmanagmentapp.propvivoandroid.domain.model

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User

data class Employee(
    val id: String,
    val name: String,
    val timeCompleted: Boolean = false
){
    constructor(): this("","")
}
