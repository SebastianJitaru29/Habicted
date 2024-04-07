package com.example.habicted_app.screen

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.ui.theme.HabictedAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                actions = {
                    IconButton(onClick = { /* TODO: Add action */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile information
            ProfileInfoSection()

            // User actions
            UserActionsSection()

            // Logout button
            Button(
                onClick = { /* TODO: Implement logout */ },
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(49.dp)
            ) {
                Text(text = "Logout")
            }
        }
    }
}

@Composable
fun ProfileInfoSection() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile picture
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Picture",
            modifier = Modifier.size(100.dp)
        )

        // User name
        Text(
            text = "John Doe",
            style = MaterialTheme.typography.bodySmall
        )

        // Additional profile information
        Text(
            text = "User's role",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun UserActionsSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /* TODO: Add action */ }) {
            Text(text = "Edit Profile")
        }
        Button(onClick = { /* TODO: Add action */ }) {
            Text(text = "Change Password")
        }
    }
}

@Composable
@Preview
fun ProfilePrev() {
   HabictedAppTheme {
        ProfileScreen()
    }
}