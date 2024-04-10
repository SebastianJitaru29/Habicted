import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habicted_app.screen.preferences.MainViewModel
import com.example.habicted_app.screen.preferences.Priority









@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(mainViewModel: MainViewModel,modifier: Modifier = Modifier) {
    val selectedIsCompleted by mainViewModel.isCompleted.collectAsState(initial = false)

    val isCompletedStatus = if (selectedIsCompleted) "Yes" else "No"

    val selectedPriority by
    mainViewModel.priority.collectAsState(initial = Priority.Low)

    val priorityStatus = when (selectedPriority) {
        Priority.High -> "High"
        Priority.Medium -> "Medium"
        Priority.Low -> "Low"
    }

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
                .padding(innerPadding),
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
            Text(text = "Task Status",)
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Completed: $isCompletedStatus")
            Switch(
                checked = selectedIsCompleted,
                onCheckedChange = {mainViewModel.updateIsCompleted(it)}
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .padding(vertical = 8.dp)
            )
            Text(text = "Priority: $priorityStatus")
            PriorityRow(mainViewModel = mainViewModel, selectedPriority =selectedPriority )
        }
    }
}


@Composable
fun PriorityRow(
    mainViewModel: MainViewModel,
    selectedPriority: Priority
){

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        Priority.entries.forEach { priority ->
            Text(
                text = priority.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            RadioButton(
                selected = priority == selectedPriority,
                onClick = {mainViewModel.updatePriority(priority)},
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
