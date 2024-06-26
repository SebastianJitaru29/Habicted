package com.example.habicted_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.navigation.graphs.RootNavigationGraph
import com.example.habicted_app.screen.preferences.MyPreferencesDataStore
import com.example.habicted_app.ui.theme.HabictedAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var myPreferencesDataStore: MyPreferencesDataStore
    private val REQUEST_NOTIFICATION_PERMISSION = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("MainActivity", "onCreate: ")
            requestNotificationPermission()
        }
        setContent {
            HabictedAppTheme(myPreferencesDataStore) {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
//                    HabiictedNavHost()
                    RootNavigationGraph(navController = rememberNavController())
                }
            }
        }
    }

    // Request notification permission
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    // Create notification channel
    private fun createNotificationChannel() {
        val name = "JetpackPushNotifications"
        val description = "Jetpack Push Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel("Global", name, importance)
        } else {
            // For versions lower than Android O, you can handle this differently if needed
            TODO("VERSION.SDK_INT < O")
        }
        channel.description = description

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    HabictedAppTheme {
//        Greeting("Android")
//    }
//}
