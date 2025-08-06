package com.propvivotaskmanagmentapp.propvivoandroid.presentation.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

object HelperFunction {
    fun formatMillisToHoursAndMinutes(milliseconds: Long): String {
        val totalMinutes = milliseconds / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours} hr ${minutes} min"
    }

    fun getDayBounds(date: Date): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startMillis = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endMillis = calendar.timeInMillis

        return Pair(startMillis, endMillis)
    }


    val todayDate: Date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())


    fun Date.toLocalDate(): LocalDate {
        return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    fun formatMillisToDateTime(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return format.format(date)
    }



}