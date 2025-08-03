package com.propvivotaskmanagmentapp.propvivoandroid.domain.model

data class TaskQuery (
    val id: String,
    val taskUid: String,
    val title: String,
    val description: String?,
    val taskStatus: String?,
    val createdAt: Long,
    val updatedAt: Long?,

){
    constructor(): this("","","","","",0,0)

}