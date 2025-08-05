package com.propvivotaskmanagmentapp.propvivoandroid.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.propvivotaskmanagmentapp.propvivoandroid.R
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.Task
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.theme.AppTheme
import com.propvivotaskmanagmentapp.propvivoandroid.presentation.util.HelperFunction.formatMillisToHoursAndMinutes

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onQueryClick: () -> Unit = {},
    onStartClick: () -> Unit = {},
    onPauseResumeClick: () -> Unit = {},
    isTimerWorking: Boolean = false,
    messageArrived: Boolean = false,
    showActionButton: Boolean = true,
    isSupervisor: Boolean = false
) {
    val scrollState = rememberScrollState()
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    task.title,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(formatMillisToHoursAndMinutes(task.estimatedTimeMs))
            }
            Spacer(modifier = Modifier.size(10.dp))

            if (showActionButton && !isSupervisor) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BadgedBox(
                            modifier = Modifier.clickable { onQueryClick() },
                            badge = {
                                if (messageArrived) {
                                    Badge()
                                }
                            }) {
                            Icon(
                                painter = painterResource(R.drawable.chat),
                                contentDescription = "chat",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Icon(
                            painter = painterResource(if (!isTimerWorking) R.drawable.play else R.drawable.pause),
                            contentDescription = "play/pause",
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { onPauseResumeClick() }
                        )


                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            formatMillisToHoursAndMinutes(task.timeSpentMs ?: 0L),
                            Modifier
                                .border(1.dp, MaterialTheme.colorScheme.outline)
                                .padding(10.dp)
                        )

                    }
                }

                Spacer(modifier = Modifier.size(10.dp))
            }
            if (isSupervisor) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(end = 20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        Log.e("Button" , "Clicked" )
                        onQueryClick() }) {


                        Icon(
                            painter = painterResource(R.drawable.chat),
                            contentDescription = "chat",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            Text(
                modifier = Modifier.scrollable(scrollState, Orientation.Vertical),
                text = task.description ?: ""
            )
        }


    }


}

@Preview
@Composable
fun TaskItemPreview() {
    val task = Task(
        "1",
        "fix the issue",
        "Description is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection",
        3600000,
        1800000,
        "",
        "",
        0,
        0
    )
    AppTheme {
        TaskItem(task = task, modifier = Modifier.size(height = 200.dp, width = 500.dp))
    }
}
@Preview
@Composable
fun TaskItemPreview1() {
    val task = Task(
        "1",
        "fix the issue",
        "Description is this description and thhis is the only description Create new scratch file from selection Create new scratch file from selection Create new scratch file from selection",
        3600000,
        1800000,
        "",
        "",
        0,
        0
    )
    AppTheme {
        TaskItem(task = task, modifier = Modifier.size(height = 200.dp, width = 500.dp), isSupervisor = true)
    }
}