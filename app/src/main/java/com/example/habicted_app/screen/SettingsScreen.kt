import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.habicted_app.screen.preferences.MainViewModel
import com.example.habicted_app.screen.preferences.Priority


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(mainViewModel: MainViewModel, modifier: Modifier = Modifier) {
    val selectedIsCompleted by mainViewModel.isCompleted.collectAsState(initial = false)

    val isCompletedStatus = if (selectedIsCompleted) "Yes" else "No"

    val selectedPriority by
    mainViewModel.priority.collectAsState(initial = Priority.Low)

    val priorityStatus = when (selectedPriority) {
        Priority.High -> "High"
        Priority.Medium -> "Medium"
        Priority.Low -> "Low"
    }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.bodySmall
            )

            // Example settings options
            SettingsOption(text = "Notifications")
            SettingsOption(text = "Privacy")
            SettingsOption(text = "Language")

            // Save button
            Button(
                onClick = { /* TODO: Implement save settings */ },
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(49.dp)
            ) {
                Text(text = "Save Settings")
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Task Status")
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Completed: $isCompletedStatus")
            Switch(
                checked = selectedIsCompleted,
                onCheckedChange = { mainViewModel.updateIsCompleted(it) }
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .padding(vertical = 8.dp)
            )
            Text(text = "Priority: $priorityStatus")
            PriorityRow(mainViewModel = mainViewModel, selectedPriority = selectedPriority)
        }
    }
}


@Composable
fun PriorityRow(
    mainViewModel: MainViewModel,
    selectedPriority: Priority,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Priority.entries.forEach { priority ->
            Text(
                text = priority.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            RadioButton(
                selected = priority == selectedPriority,
                onClick = { mainViewModel.updatePriority(priority) },
                colors = RadioButtonDefaults.colors(selectedPriority.color)

            )
        }
    }
}

@Composable
fun SettingsOption(text: String) {
    Button(
        onClick = { /* TODO: Handle settings option click */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text)
    }
}
