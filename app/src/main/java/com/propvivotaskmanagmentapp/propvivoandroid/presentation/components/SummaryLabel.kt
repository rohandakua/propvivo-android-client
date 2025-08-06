package com.propvivotaskmanagmentapp.propvivoandroid.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Summary

@Composable
fun SummaryLabel(summary: Summary) {
    val color = summary.getColor(MaterialTheme.colorScheme)
    Text(
        text = summary.displayName(),
        color = color,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}
