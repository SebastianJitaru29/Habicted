package com.example.habicted_app.screen.taskscreen

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.habicted_app.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.habicted_app.ui.theme.HabictedAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(
    modifier: Modifier= Modifier,
    /*onProfilePic: () -> Unit*/
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopRow()
        Spacer(modifier = Modifier.height(16.dp))
        CalendarApp()
        TaskListElementApp()
    }
}
@Composable
fun TopRow(){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
      ProfilePicture(
        painter = painterResource(id = R.drawable.outline_groups_24),
        contentDescription = "Profile Picture"
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
            text = "Username",
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
    modifier: Modifier = Modifier
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
    )
}
//Modifier change the size, layout, behaivour and appearance of a Composable




@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Welcome light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)

@Composable
fun Preview() {
    HabictedAppTheme {
        TaskScreen(/*onProfilePic = {}*/)
    }
}