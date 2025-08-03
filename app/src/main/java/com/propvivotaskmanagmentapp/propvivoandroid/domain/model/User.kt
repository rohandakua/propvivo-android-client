package com.propvivotaskmanagmentapp.propvivoandroid.domain.model

data class User(
    val uid: String = "",
    val email: String = "",
    val role: String = "",
    val name: String = ""
){
    constructor(): this("","","","")
}
