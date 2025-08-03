package com.propvivotaskmanagmentapp.propvivoandroid.domain.model

data class Task (
    val id: String,
    val title: String,
    val description: String?,
    val estimatedTimeMs: Long,
    val timeSpentMs: Long?,
    val assignedTo: String,
    val assignedBy: String,
    val createdAt: Long,
    val updatedAt: Long?

){
    constructor(): this("","","",0,0,"","",0,0)
}