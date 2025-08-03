package com.propvivotaskmanagmentapp.propvivoandroid.domain.model

data class Employee(
    val id: String,
    val name: String,
    val timeCompleted: Boolean = false
)
