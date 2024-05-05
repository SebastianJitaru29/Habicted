package com.example.habicted_app.screen.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.habicted_app.data.model.User

@Composable
fun GroupAddDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Color, List<User>) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    val color by remember { mutableStateOf(Color.Transparent) }
    val members by remember { mutableStateOf(listOf<User>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create new group") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )

                // User add list

                // Color pick boxes

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(name, color, members)
                },
                enabled = name.isNotBlank()
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

