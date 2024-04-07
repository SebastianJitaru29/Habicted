import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
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
