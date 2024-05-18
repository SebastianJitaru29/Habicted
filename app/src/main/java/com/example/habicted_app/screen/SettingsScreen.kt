import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.habicted_app.screen.preferences.MainViewModel
import com.example.habicted_app.screen.preferences.NetworkPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(mainViewModel: MainViewModel, modifier: Modifier = Modifier) {
    val selectedIsCompleted by mainViewModel.isCompleted.collectAsState(initial = false)
    val selectedNetworkPreference by mainViewModel.priority.collectAsState(initial = NetworkPreference.WIFI)

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
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Light Mode", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.size(8.dp))
                        Switch(
                            checked = selectedIsCompleted,
                            onCheckedChange = { mainViewModel.updateIsCompleted(it) }
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Network", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.size(16.dp))
                    NetworkPreferences(
                        mainViewModel = mainViewModel,
                        selectedNetworkPreference = selectedNetworkPreference
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(
                        onClick = { mainViewModel.launchFunction() },
                    ) {
                        Text(text = "Launch Cloud Function")
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(
                        onClick = { mainViewModel.getCurrentRegistrationToken() },
                    ) {
                        Text(text = "GetToken")
                    }
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(
                        onClick = { mainViewModel.subscribeToMessagingTopic("testTopic") },
                    ) {
                        Text(text = "Subscribe to topic")
                    }
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(
                        onClick = { mainViewModel.sendTestMessage() },
                    ) {
                        Text(text = "send with fucntions")
                    }
                }
            }
        }
    }
}

@Composable
fun NetworkPreferences(
    mainViewModel: MainViewModel,
    selectedNetworkPreference: NetworkPreference,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NetworkPreference.entries.forEach { networkPreference ->
            Text(
                text = networkPreference.name,
                maxLines = 1,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            RadioButton(
                selected = networkPreference == selectedNetworkPreference,
                onClick = { mainViewModel.updateNetworkPreference(networkPreference) },
            )
        }
    }
}
