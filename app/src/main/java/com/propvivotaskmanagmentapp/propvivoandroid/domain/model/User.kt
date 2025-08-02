package com.propvivotaskmanagmentapp.propvivoandroid.domain.model

import java.util.UUID

data class User(
    val uid : String ,
    val email: String,
    val role: String,
)