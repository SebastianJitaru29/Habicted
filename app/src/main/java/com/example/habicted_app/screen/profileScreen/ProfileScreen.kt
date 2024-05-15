package com.example.habicted_app.screen.profileScreen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.habicted_app.ui.theme.HabictedAppTheme
import com.example.habicted_app.utils.StorageUtil
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.habicted_app.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, onLogout: () -> Unit = {}) {
    var uri by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )
     val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val context = LocalContext.current
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
            Column {
                Button(onClick = {
                    singlePhotoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )

                }) {
                    Text("Pick Single Image")
                }

                    AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .crossfade(true)
                        .build(),
                        error = painterResource(R.drawable.profpick),
                        placeholder = painterResource(R.drawable.profpick),
                        contentDescription = "stringResource(R.string.description)",
                        contentScale = ContentScale.Fit,
                        modifier = modifier.size(Dp(150f))
                    )

                    uri?.let {
                        StorageUtil.uploadToStorage(uri = it, context = context, type = "image",userId)
                    }
                }
                UserActionsSection()
                Button(
                        onClick = onLogout,
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(49.dp)
                ) {
                Text(text = "Logout")
            }
            }

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