import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habicted_app.screen.taskscreen.data.ListDataSource
import com.example.habicted_app.screen.taskscreen.data.TaskListElement
import com.example.habicted_app.sensor.BiometricPromptManager
import com.example.habicted_app.ui.theme.HabictedAppTheme


@Composable
fun TaskListApp() {
    TasksList(
        tasksList = ListDataSource().initialLoadList(),
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
fun TaskCard(task: TaskListElement, modifier: Modifier = Modifier) {
    var isChecked by remember { mutableStateOf(task.isChecked) }
    val context = LocalContext.current
    val biometricPromptManager = remember { BiometricPromptManager(activity = context as AppCompatActivity) }

    Card(
        modifier = modifier.border(
            1.dp,
            MaterialTheme.colorScheme.inversePrimary,
            MaterialTheme.shapes.medium
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = LocalContext.current.getString(task.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            CheckboxWithBiometric(biometricPromptManager = biometricPromptManager)
        }
    }
}

@Composable
fun CheckboxWithBiometric(
    biometricPromptManager: BiometricPromptManager
) {
    val (isChecked, setChecked) = remember { mutableStateOf(false) }
    val authenticationSucceeded = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        biometricPromptManager.promptResult.collect { result ->
            when (result) {
                is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                    authenticationSucceeded.value = true
                    setChecked(true)
                }
                else -> {
                    authenticationSucceeded.value = false
                    setChecked(false)
                }
            }
        }
    }

    Checkbox(
        checked = isChecked,
        onCheckedChange = { newChecked ->
            if (newChecked) {
                biometricPromptManager.showBiometricPrompt(
                    title = "Biometric Authentication",
                    description = "Authenticate to enable this task."
                )
            } else {
                setChecked(false)
            }
        },
        colors = CheckboxDefaults.colors(
            checkedColor = if (authenticationSucceeded.value) Color.Green else Color.White,
            uncheckedColor = Color.White,
            disabledCheckedColor = Color.LightGray,
            disabledUncheckedColor = Color.LightGray,
            disabledIndeterminateColor = Color.LightGray
        ),
        modifier = Modifier.padding(16.dp)
    )
}


@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Welcome light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun Preview1() {
    HabictedAppTheme {
        TaskListApp()
    }
}
