package com.example.habicted_app.screen.taskscreen

import TaskListApp
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habicted_app.R
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.screen.home.HomeUiEvents
import com.example.habicted_app.screen.taskscreen.components.CalendarRow
import com.example.habicted_app.ui.theme.HabictedAppTheme
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    username: String,
    profilePicture: Int,
    onProfilePic: () -> Unit,
    selectedDate: LocalDate,
    tasksList: List<Task>,
    onEvent: (HomeUiEvents) -> Unit,
    onTaskUIEvents: (TaskUIEvents) -> TaskUIState?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopRow(
            onProfilePic = onProfilePic,
            username = username,
            profilePicture = profilePicture,
            onEvent = onEvent
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalendarRow(selectedDate = selectedDate, onEvent = onEvent)
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                TaskListApp(tasksList, onTaskUIEvents)
            }
        }
    }
}

@Composable
fun TopRow(
    onProfilePic: () -> Unit,
    username: String,
    profilePicture: Int,
    onEvent: (HomeUiEvents) -> Unit,
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfilePicture(
            painter = painterResource(id = profilePicture),
            contentDescription = "Profile Picture",
            onClick = onProfilePic
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Welcome!",
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = username,
                textAlign = TextAlign.Start,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onEvent(HomeUiEvents.UpdateNetworkCurrentStatus(context = context)) },
        ) {
            Icon(imageVector = Icons.Filled.Update, contentDescription = "update")
        }
    }
}

@Composable
fun ProfilePicture(
    painter: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
    }
}

////Modifier change the size, layout, behaivour and appearance of a Composable
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(
//    name = "Welcome dark theme",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Preview(
//    name = "Welcome light theme",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun Preview() {
//    HabictedAppTheme {
//        TaskScreen(
//            username = "User",
//            profilePicture = R.drawable.outline_groups_24,
//            selectedDate = LocalDate.now(),
//            tasksList = emptyList(),
//            onProfilePic = {},
//            onEvent = {},
//            onTaskUIEvents = { event ->
//                when (event) {
//                    is TaskUIEvents.ConvertTaskToTaskUIState -> TODO()
//                    is TaskUIEvents.UpdateIsDone -> TODO()
//                }
//            }
//        )
//    }
//}
