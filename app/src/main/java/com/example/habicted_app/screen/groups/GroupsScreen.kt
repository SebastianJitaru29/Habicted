package com.example.habicted_app.screen.groups

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habicted_app.R
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.screen.taskscreen.data.CalendarData
import com.example.habicted_app.screen.taskscreen.data.CalendarDataSource
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupScreen(
    modifier: Modifier = Modifier,
    groupList: List<GroupUIState>,
) {
    Column {
        // TODO: Move to topbar in scaffold ?
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Groups",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
        LazyColumn {
            items(groupList.size) { index ->
                GroupCard(
                    modifier = modifier.padding(8.dp),
                    groupUIState = groupList[index],
                    todayDate = LocalDate.now(),
                    expandedInitialValue = false
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupCard(
    modifier: Modifier = Modifier,
    groupUIState: GroupUIState,
    todayDate: LocalDate,
    expandedInitialValue: Boolean = false,
) {
    var expanded by rememberSaveable { mutableStateOf(expandedInitialValue) }

    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(18.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.outline_groups_24),
                    contentDescription = "Group image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(10.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Column(
                    modifier = Modifier.padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = groupUIState.name,
                        fontStyle = MaterialTheme.typography.titleSmall.fontStyle
                    )

                    Text(
                        text = groupUIState.streakDays.toString()
                            .plus(if (groupUIState.isInStreak) " days streak" else " missed days"),
                        fontFamily = MaterialTheme.typography.headlineMedium.fontFamily
                    )
                }

            }

            if (expanded) {
                val dataSource = CalendarDataSource()
                // we use `mutableStateOf` and `remember` inside composable function to schedules recomposition
                val calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    GroupCalendarContent(
                        data = calendarUiModel,
                        onDateClickListener = { },
                        tasksInfo = groupUIState.tasks
                    )
                }

            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupCalendarContent(
    data: CalendarData,
    onDateClickListener: (CalendarData.Date) -> Unit,
    tasksInfo: List<Task>,
) {
    LazyRow {
        items(items = data.visibleDates) { date ->
            GroupCalendarItem(
                date = date,
                onClickListener = onDateClickListener,
                tasksInfo = tasksInfo.filter { it.date == date.date }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupCalendarItem(
    date: CalendarData.Date,
    onClickListener: (CalendarData.Date) -> Unit,
    tasksInfo: List<Task>,
) {

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable { onClickListener(date) },
    ) {

        Column(
            modifier = Modifier
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )

            val allTasksTotal = tasksInfo.sumOf { it.total }
            val allTasksDone = tasksInfo.sumOf { it.done }
            if (date.isToday) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Today",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

            } else if (date.date.isAfter(LocalDate.now())) {
                Icon(
                    imageVector = Icons.Default.Today,
                    contentDescription = "Future date",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                when (allTasksTotal) {
                    0 -> {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "No tasks done",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    allTasksDone -> {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "All tasks done",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    else -> {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Not done",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                    }
                }

            }
            Text(
                text = allTasksDone.toString().plus("/").plus(allTasksTotal),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )


        }
    }
}


@Composable
fun TasksCircle(modifier: Modifier = Modifier, proportions: List<Float>, colors: List<Color>) {
    val stroke = with(LocalDensity.current) { Stroke(2.dp.toPx()) }
    val dividerLengthInDegrees = 10f

    Canvas(modifier = modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = 0f
        proportions.forEachIndexed { index, proportion ->
            val sweep = proportion * 360
            drawArc(
                color = colors[index],
                startAngle = startAngle + dividerLengthInDegrees / 2,
                sweepAngle = sweep - dividerLengthInDegrees,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke
            )
            startAngle += sweep
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GroupScreenPreview() {

    val groupList2 = listOf<GroupUIState>(
        GroupUIState(
            "Group 1",
            emptyList(),
            emptyList(),
            false,
            1,
        ),
        GroupUIState(
            "Group 2",
            emptyList(),
            emptyList(),
            true,
            1,
        )
    )
    GroupScreen(groupList = groupList2)
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun GroupsCardsPrev() {
    GroupCard(
        groupUIState = GroupUIState(
            "Group 1",
            emptyList(),
            listOf(
                Task(
                    id = 1,
                    groupId = 2,
                    name = "Task 1",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    done = 1,
                    total = 2,
                ),
                Task(
                    id = 2,
                    groupId = 2,
                    name = "Task 2",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    done = 1,
                    total = 2,
                ),
            ),
            false,
            1,
        ), todayDate = LocalDate.now(), expandedInitialValue = true
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Prevv() {

    val dataSource = CalendarDataSource()
    // we use `mutableStateOf` and `remember` inside composable function to schedules recomposition
    val calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    GroupCalendarContent(
        data = calendarUiModel,
        onDateClickListener = { },
        tasksInfo = listOf(
            Task(
                id = 1,
                groupId = 1,
                name = "Task 1",
                description = null,
                date = LocalDate.now(),
                isDone = false,
                streakDays = 1,
                done = 1,
                total = 2,
            ),
            Task(
                id = 2,
                groupId = 2,
                name = "Task 2",
                description = null,
                date = LocalDate.now(),
                isDone = false,
                streakDays = 1,
                done = 1,
                total = 2,
            ),
        )
    )
}

@Preview
@Composable
private fun Circleprew() {
    TasksCircle(
        proportions = listOf(0.3f, 0.2f, 0.5f),
        colors = listOf(Color.Red, Color.Blue, Color.Green)
    )
}