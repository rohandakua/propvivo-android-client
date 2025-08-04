package com.propvivotaskmanagmentapp.propvivoandroid.presentation.util

import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

object HelperFunction {
    fun formatMillisToHoursAndMinutes(milliseconds: Long): String {
        val totalMinutes = milliseconds / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours} hr ${minutes} min"
    }

    fun getDayBounds(date: Date): Pair<Date, Date> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.time

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val end = calendar.time

        return Pair(start, end)
    }

    val todayDate: Date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())


    fun Date.toLocalDate(): LocalDate {
        return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }



}