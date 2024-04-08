import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.R
import com.example.habicted_app.routes.allItemsNav
import com.example.habicted_app.ui.theme.HabictedAppTheme

@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var currentIndex by rememberSaveable { mutableStateOf(0) }
    var isDialogOpen by remember { mutableStateOf(false) } // State to track if the dialog is open
    val context = LocalContext.current
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                allItemsNav.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = currentIndex == index,
                        onClick = { currentIndex = index },
                        icon = item.icon
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isDialogOpen = true // Open the dialog when FAB is clicked
                }
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            allItemsNav.get(currentIndex).screen(navController)
        }
    }

    // Display dialog when FAB is clicked
    if (isDialogOpen) {
        CreateTaskDialog(onDismiss = { isDialogOpen = false })
    }
}

@Composable
fun CreateTaskDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.create_task))
        },
        text = {
             Text(stringResource(R.string.choose_task_options))
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                // You can customize the button text as needed
                // Text(stringResource(R.string.create))
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(
                contentColor = Color.Red
            )) {
                // You can customize the button text as needed
                // Text(stringResource(R.string.cancel))
                Text("Cancel")
            }
        }
    )
}

@Composable
@Preview
fun NavBarPrev() {
    val navController = rememberNavController()
    HabictedAppTheme {
        MainScreen(navController = navController)
    }
}
