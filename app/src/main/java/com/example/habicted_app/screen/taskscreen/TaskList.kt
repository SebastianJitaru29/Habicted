package com.example.habicted_app.screen.taskscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.example.habicted_app.screen.taskscreen.data.ListDataSource
import com.example.habicted_app.screen.taskscreen.models.TaskListElement
import androidx.compose.ui.Modifier

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TaskListElementApp() {
    TasksList(
        tasksList = ListDataSource().loadTasks(),
    )
}

@Composable
fun TasksList(tasksList: List<TaskListElement>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(tasksList) { task ->
            TaskCard(
                task = task,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun TaskCard(task:TaskListElement, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
            Image(
                painter = painterResource(task.imageResourceId),
                contentDescription = stringResource(task.stringResourceId),
                modifier = Modifier
                    .fillMaxSize().height(194.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = LocalContext.current.getString(task.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}


