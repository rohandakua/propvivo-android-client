package com.propvivotaskmanagmentapp.propvivoandroid.presentation.util

object HelperFunction {
    fun formatMillisToHoursAndMinutes(milliseconds: Long): String {
        val totalMinutes = milliseconds / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours} hr ${minutes} min"
    }

}