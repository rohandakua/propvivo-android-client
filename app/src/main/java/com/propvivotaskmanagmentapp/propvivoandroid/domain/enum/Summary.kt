package com.propvivotaskmanagmentapp.propvivoandroid.domain.enum

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

enum class Summary(
    val label: String,
    val emoji: String,
    val colorProvider: (ColorScheme) -> Color
) {
    YET_TO_WORK("Yet to work", "🕒", { it.error }),
    IN_PROGRESS("In Progress", "🔄", { it.primary }),
    COMPLETED("Completed", "✅", { it.secondary });

    fun getColor(colorScheme: ColorScheme): Color = colorProvider(colorScheme)

    fun displayName(): String = "$emoji $label"
}
