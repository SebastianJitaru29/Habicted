package com.example.habicted_app.screen.taskscreen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.screen.home.HomeUiEvents
import com.example.habicted_app.screen.taskscreen.components.CalendarApp
import com.example.habicted_app.ui.theme.HabictedAppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    username: String,
    profilePicture: Int,
    tasksList: List<TaskUIState>,
    onEvent: (HomeUiEvents) -> Unit,
    /*onProfilePic: () -> Unit*/
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopRow(navController = navController, username = username, profilePicture = profilePicture)
        Spacer(modifier = Modifier.height(16.dp))
        CalendarApp(taskList = tasksList, onEvent = onEvent)
    }
}

@Composable
fun TopRow(navController: NavHostController, username: String, profilePicture: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfilePicture(
            painter = painterResource(id = profilePicture),
            contentDescription = "Profile Picture",
            onClick = { navController.navigate("profile") }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Welcome!",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = username,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
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

//Modifier change the size, layout, behaivour and appearance of a Composable

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    name = "Welcome dark theme",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Welcome light theme",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun Preview() {
    val navController = rememberNavController()
    HabictedAppTheme {
//        TaskScreen(
//            navController = navController,
//            username = "User",
//            profilePicture = R.drawable.outline_groups_24,
//            tasksList = emptyList(),
//            onDayClick = {  }
//        )
    }
}
