import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.example.habicted_app.data.repository.local.LocalTaskRepository
import com.example.habicted_app.screen.taskscreen.TaskUIState
import com.example.habicted_app.screen.taskscreen.data.ListDataSource
import com.example.habicted_app.screen.taskscreen.data.TaskListElement
import com.example.habicted_app.sensor.BiometricPromptManager
import com.example.habicted_app.ui.theme.HabictedAppTheme


@Composable
fun TaskListApp(tasksList: List<TaskUIState>) {
    TasksListRecylcerView(
        tasksList = tasksList,
    )
}

@Composable
fun TasksListRecylcerView(tasksList: List<TaskUIState>, modifier: Modifier = Modifier) {
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
fun TaskCard(task: TaskUIState, modifier: Modifier = Modifier) {
    val isCheck = remember { mutableStateOf(task.isChecked) }
    val context = LocalContext.current
    val biometricPromptManager =
        remember { BiometricPromptManager(activity = context as AppCompatActivity) }

    Card(
        modifier = modifier.border(
            1.dp,
            MaterialTheme.colorScheme.inversePrimary,
            MaterialTheme.shapes.medium
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = task.name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            CheckboxWithBiometric(
                biometricPromptManager = biometricPromptManager,
                isChecked = isCheck// task.isDone, TODO
            )
        }
    }
}

@Composable
fun CheckboxWithBiometric(
    biometricPromptManager: BiometricPromptManager,
    isChecked: MutableState<Boolean>,
    biometricResutl: (Boolean) -> Unit = {}
) {
    val authenticationSucceeded = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        biometricPromptManager.promptResult.collect { result ->
            when (result) {
                is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                    authenticationSucceeded.value = true
                    isChecked.value = true
                }

                else -> {
                    authenticationSucceeded.value = false
                    isChecked.value = false
                }
            }
        }
    }

    Checkbox(
        checked = isChecked.value,
        onCheckedChange = { newChecked ->
            if (newChecked) {
                biometricPromptManager.showBiometricPrompt(
                    title = "Biometric Authentication",
                    description = "Authenticate to enable this task."
                )
            } else {
                isChecked.value = false
            }
        },
        colors = CheckboxDefaults.colors(
            checkedColor = Color.White,
            uncheckedColor = Color.White,
            disabledCheckedColor = Color.LightGray,
            disabledUncheckedColor = Color.LightGray,
            disabledIndeterminateColor = Color.LightGray
        ),
        modifier = Modifier.padding(16.dp)
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Welcome light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun Preview1() {
    val taskRepository = LocalTaskRepository()
    val tasks = taskRepository.getTasks()
        .map {
            TaskUIState(
                name = it.name,
                color = "",
                date = it.date,
                isChecked = it.isDone
            )
        }
    HabictedAppTheme {
        TaskListApp(tasks)
    }
}
