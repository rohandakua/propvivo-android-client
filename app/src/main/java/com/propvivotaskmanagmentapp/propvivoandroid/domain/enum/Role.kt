package com.propvivotaskmanagmentapp.propvivoandroid.domain.enum

import com.propvivotaskmanagmentapp.propvivoandroid.domain.util.FirebasePathConstants

enum class Role (val role: String , val path: String){
    Employee("Employee", FirebasePathConstants.EMPLOYEES),
    Admin("Admin" , FirebasePathConstants.ADMINS),
    Supervisor("Supervisor" , FirebasePathConstants.SUPERVISORS)
}