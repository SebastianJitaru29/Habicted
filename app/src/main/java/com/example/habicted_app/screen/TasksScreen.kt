package com.example.habicted_app.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.sharp.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habicted_app.R
import com.example.habicted_app.ui.theme.righteousFamily
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.habicted_app.screen.access.ForgotPasswordScreen
import com.example.habicted_app.ui.theme.HabictedAppTheme
import com.example.habicted_app.ui.theme.backgroundDark
import com.example.habicted_app.ui.theme.backgroundLight

@OptIn(ExperimentalMaterial3Api::class)
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
        CalendarHeader()
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
@Composable
fun CalendarHeader(){
    Row {
        Text(
            text = "April 1, May 2024",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(

                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Next"
            )
        }
    }
}

@Composable
fun CalendarApp(modifier: Modifier=Modifier){
    Column(modifier= modifier.fillMaxSize()) {
        CalendarHeader()
        CalendarContent()
    }
}

@Composable
fun CalendarItem(day:String,date:String){
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Column(
            modifier = Modifier
                .width(40.dp)
                .height(48.dp)
                .padding(4.dp)
        ) {
            Text(
                text = day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun CalendarContent(modifier: Modifier = Modifier){
    LazyRow {
        items(count = 7) { index ->
            CalendarItem(day = "Day ${index + 1}", date = "21") // Assuming 21 is the date
        }
    }
}

@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview() {
    HabictedAppTheme {
        TaskScreen(/*onProfilePic = {}*/)
    }
}