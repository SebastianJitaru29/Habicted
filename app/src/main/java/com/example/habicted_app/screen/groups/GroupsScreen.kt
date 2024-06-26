package com.example.habicted_app.screen.groups

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.model.User
import com.example.habicted_app.screen.taskscreen.calendar.CalendarData
import com.example.habicted_app.screen.taskscreen.calendar.CalendarDataSource
import com.example.habicted_app.ui.styling.ColorPalette
import com.example.habicted_app.ui.theme.Green500
import com.example.habicted_app.ui.theme.Red500
import com.example.habicted_app.ui.theme.onPrimaryLight
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupScreen(
    modifier: Modifier = Modifier,
    groupList: List<Group>,
    searchUsersByEmail: (String, (List<User>) -> Unit) -> Unit,
    onInvite: (Group, String) -> Unit,
) {
    val showSheet = remember { mutableStateOf(false) }
    val clickedGroup = remember { mutableStateOf(Group()) }
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
            items(
                items = groupList,
                key = { group -> group.id },
            ) { group ->
                GroupCard(
                    modifier = modifier.padding(8.dp),
                    groupUIState = GroupUIState(group),
                    todayDate = LocalDate.now(),
                    expandedInitialValue = false,
                    onInviteClick = {
                        showSheet.value = true
                        clickedGroup.value = group
                    }
                )
            }
        }

        if (showSheet.value) {
            BottomSheet(
                group = clickedGroup.value,
                onDismiss = {
                    showSheet.value = false
                },
                searchUsersByEmail = searchUsersByEmail,
                onInvite = { group, id ->
                    showSheet.value = false
                    onInvite(group, id)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    group: Group,
    onDismiss: () -> Unit = {},
    searchUsersByEmail: (String, (List<User>) -> Unit) -> Unit,
    onInvite: (Group, String) -> Unit,
) {
    val modalBottomSheet = rememberModalBottomSheetState()
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<User>()) }

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxSize(),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                singleLine = true,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    searchUsersByEmail(searchQuery) {
                        searchResults = it
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Invite",
                    )
                },
                label = { Text("Search by email") },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn {
                items(searchResults) { user ->
                    UserSearchResultCard(user = user, onInvite = { u -> onInvite(group, u.id) })
                }
            }
        }
    }

}

@Composable
fun UserSearchResultCard(modifier: Modifier = Modifier, user: User, onInvite: (User) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "User")
            Text(
                text = user.email,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onInvite(user) }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Add User",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Green500,
                            shape = CircleShape
                        )
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
    onInviteClick: () -> Unit = {},
) {
    Log.d("GroupColor", "GrouColor: ${groupUIState.color}")
    var expanded by rememberSaveable { mutableStateOf(expandedInitialValue) }
    val palette = ColorPalette.colorToPalette(groupUIState.color)
    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        colors = CardColors(
            containerColor = palette.container,
            contentColor = palette.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(18.dp)) {
                GroupImage(Modifier.size(50.dp))
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
                Spacer(modifier = Modifier.weight(1f))
                if (expanded)
                    Invite(color = groupUIState.color, onClick = onInviteClick)

            }

            if (expanded) {
                val dataSource = CalendarDataSource()
                // we use `mutableStateOf` and `remember` inside composable function to schedules recomposition
                val calendarUiModel by remember {
                    mutableStateOf(
                        dataSource.getData(
                            lastSelectedDate = dataSource.today
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
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

@Composable
fun Invite(color: Color, onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Outlined.Add,
        contentDescription = "Invite",
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .background(color)
            .padding(10.dp)
            .clickable { onClick() },
        tint = Color.White
    )
}


@Composable
fun GroupImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.outline_groups_24),
        contentDescription = "Group image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(10.dp)
            )
            .background(onPrimaryLight)
            .padding(10.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupCalendarContent(
    data: CalendarData,
    onDateClickListener: (CalendarData.Date) -> Unit,
    tasksInfo: List<Task>,
) {
    LazyRow(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
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
            .run {
                if (date.isToday) {
                    this.border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(10.dp)
                    )
                } else this
            }
            .clickable { onClickListener(date) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
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
            val allTasksDone = tasksInfo.sumOf { it.doneBy }
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
                            imageVector = Icons.Default.Check,
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
            val displayText =
                if (allTasksTotal == 0) "" else allTasksDone.toString().plus("/")
                    .plus(allTasksTotal)
            Text(
                text = displayText,
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

    val groupList: List<Group> = listOf(
        Group(
            id = "",
            name = "Group 1",
            color = Red500.value,
            tasksList = listOf(
                Task(
                    id = "",
                    groupId = "",
                    name = "Task 1",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    doneBy = 1,
                    total = 2,
                ),
                Task(
                    id = "",
                    groupId = "",
                    name = "Task 2",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    doneBy = 1,
                    total = 2,
                ),
            ),
            members = emptyList(),
            usersTokens = emptyList(),
        ),
        Group(
            id = "",
            name = "Group 2",
            color = Red500.value,
            tasksList = listOf(
                Task(
                    id = "",
                    groupId = "",
                    name = "Task 1",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    doneBy = 1,
                    total = 2,
                ),
                Task(
                    id = "",
                    groupId = "",
                    name = "Task 2",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    doneBy = 1,
                    total = 2,
                ),
            ),
            members = emptyList(),
            usersTokens = emptyList(),
        ),
    )
    GroupScreen(groupList = groupList, searchUsersByEmail = { _, _ -> }, onInvite = { _, _ -> })
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun GroupsCardsPrev() {
    GroupCard(
        groupUIState = GroupUIState(
            "Group 1",
            emptyList(),
            Red500,
            listOf(
                Task(
                    id = "",
                    groupId = "",
                    name = "Task 1",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    doneBy = 1,
                    total = 2,
                ),
                Task(
                    id = "",
                    groupId = "",
                    name = "Task 2",
                    description = null,
                    date = LocalDate.now(),
                    isDone = false,
                    streakDays = 1,
                    doneBy = 1,
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
                id = "",
                groupId = "",
                name = "Task 1",
                description = null,
                date = LocalDate.now(),
                isDone = false,
                streakDays = 1,
                doneBy = 1,
                total = 2,
            ),
            Task(
                id = "",
                groupId = "",
                name = "Task 2",
                description = null,
                date = LocalDate.now(),
                isDone = false,
                streakDays = 1,
                doneBy = 1,
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