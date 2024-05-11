import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habicted_app.R
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.data.repository.local.LocalGroupRepository
import com.example.habicted_app.data.repository.local.LocalTaskRepository
import com.example.habicted_app.screen.taskscreen.TaskUIEvents
import com.example.habicted_app.screen.taskscreen.TaskUIState
import com.example.habicted_app.sensor.BiometricPromptManager
import com.example.habicted_app.ui.styling.ColorPalette
import com.example.habicted_app.ui.theme.HabictedAppTheme


@Composable
fun TaskListApp(tasksList: List<Task>, onTaskUIEvents: (TaskUIEvents) -> TaskUIState?) {
    if (tasksList.isEmpty()) {
        Text(text = stringResource(id = R.string.no_tasks))
    } else {
        TasksListRecylcerView(
            tasksList = tasksList,
            onTaskUIEvents = onTaskUIEvents
        )
    }
}

@Composable
fun TasksListRecylcerView(
    modifier: Modifier = Modifier,
    tasksList: List<Task>,
    onTaskUIEvents: (TaskUIEvents) -> TaskUIState?,
) {
    LazyColumn(modifier = modifier) {
        items(tasksList) { task ->
            TaskCard(
                taskState = onTaskUIEvents(TaskUIEvents.ConvertTaskToTaskUIState(task))
                    ?: TaskUIState(task),
                modifier = Modifier.padding(8.dp),
                onTaskUIEvents = onTaskUIEvents
            )
        }
    }
}

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    taskState: TaskUIState,
    onTaskUIEvents: (TaskUIEvents) -> TaskUIState?,
) {
    val context = LocalContext.current
    val biometricPromptManager =
        remember { BiometricPromptManager(activity = context as AppCompatActivity) }

    val palette = ColorPalette.colorToPalette(taskState.color)

    Card(
        modifier = modifier.fillMaxWidth(),
//        modifier = modifier.border(
//            width = 1.dp,
//            color = palette.primary,
//            shape = MaterialTheme.shapes.medium
//        ),
        colors = CardColors(
            containerColor = if (taskState.task.isDone) palette.primary else palette.container,
            contentColor = palette.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = taskState.task.name,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                style = MaterialTheme.typography.headlineSmall
            )
            CheckboxWithBiometric(
                modifier = Modifier.weight(0.2f),
                biometricPromptManager = biometricPromptManager,
                isDone = taskState.task.isDone,
                updateDoneTask = { newStatus: Boolean ->
                    onTaskUIEvents(TaskUIEvents.UpdateIsDone(newStatus, taskState.task))
                },
            )
        }
    }
}

@Composable
fun CheckboxWithBiometric(
    modifier: Modifier = Modifier,
    biometricPromptManager: BiometricPromptManager,
    isDone: Boolean,
    updateDoneTask: (Boolean) -> Unit = { _: Boolean -> },
) {
    val authenticationSucceeded = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        biometricPromptManager.promptResult.collect { result ->
            when (result) {
                is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                    authenticationSucceeded.value = true
                    updateDoneTask(true)
                }

                else -> {
                    authenticationSucceeded.value = false
                    updateDoneTask(false)
                }
            }
        }
    }

    Checkbox(
        checked = isDone,
        onCheckedChange = { newChecked ->
            if (newChecked) {
                biometricPromptManager.showBiometricPrompt(
                    title = "Biometric Authentication",
                    description = "Authenticate to enable this task."
                )
            } else {
                updateDoneTask(false)
            }
        },
        colors = CheckboxDefaults.colors(
            checkedColor = Color.White,
            uncheckedColor = Color.White,
            disabledCheckedColor = Color.LightGray,
            disabledUncheckedColor = Color.LightGray,
            disabledIndeterminateColor = Color.LightGray
        ),
        modifier = modifier.padding(16.dp)
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Welcome light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun Preview1() {
    val taskRepository = LocalTaskRepository()
    val groupRepository = LocalGroupRepository()
    val tasks = taskRepository.getAllTasks()
    HabictedAppTheme {
        TaskListApp(tasksList = tasks, onTaskUIEvents = { event ->
            when (event) {
                is TaskUIEvents.ConvertTaskToTaskUIState -> {
                    val group = groupRepository.getGroup(event.task.groupId)
                    TaskUIState(
                        event.task,
                        Color(group?.color ?: Color.Transparent.value),
                        groupName = group?.name ?: ""
                    )
                }

                is TaskUIEvents.UpdateIsDone -> TODO()
            }
        })
    }
}
