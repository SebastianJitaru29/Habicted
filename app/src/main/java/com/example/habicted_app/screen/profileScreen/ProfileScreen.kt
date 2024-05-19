package com.example.habicted_app.screen.profileScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.habicted_app.R
import com.example.habicted_app.ui.theme.HabictedAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, onLogout: () -> Unit = {}) {
    var uri by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            uri = it
        }
    )
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: return
    val context = LocalContext.current

    // Fetch the user's photoUrl parameter from Firestore when the screen loads
    LaunchedEffect(userId) {
        getUserParameter(userId ?: "") { parameterValue ->
            if (parameterValue is String) {
                uri = Uri.parse(parameterValue)
            }
        }
    }

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
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.profpick),
                    placeholder = painterResource(R.drawable.profpick),
                    contentDescription = "stringResource(R.string.description)",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(Dp(150f))
                        // Make the image clickable and launch single photo picker when clicked
                        .clickable {
                            singlePhotoPicker.launch("image/*")
                        }
                )
                uri?.let {
                    StorageUtil.uploadToStorage(uri = it, context = context, type = "image", userId)
                }

            }

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


// Function to retrieve parameter from Firestore
private fun getUserParameter(userId: String, callback: (Any?) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    val userDocRef = firestore.collection("users").document(userId)

    userDocRef.get().addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot != null && documentSnapshot.exists()) {
            val parameterValue =
                documentSnapshot.getString("photoUrl") // Replace "parameterName" with your parameter name
            callback(parameterValue)
        } else {
            userDocRef.update("photoUrl", "")
            callback(null)
        }
    }.addOnFailureListener {
        callback(null)
    }
}

//@Composable
//@Preview
//fun ProfilePrev() {
//    HabictedAppTheme {
//        ProfileScreen()
//    }
//}
