package com.propvivotaskmanagmentapp.propvivoandroid.domain.model

data class Message(
    val id: String,
    val timestamp: Long,
    val sendByEmployee: Boolean,
    val taskQueryId: String,
    val content: String,
){
    constructor(): this("",0,false,"","")

}