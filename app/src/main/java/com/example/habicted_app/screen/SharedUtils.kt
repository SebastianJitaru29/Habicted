package com.example.habicted_app.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, LocalDate) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("New Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") }
                    )

                    OutlinedTextField(
                        value = group,
                        onValueChange = { group = it },
                        label = { Text("Group") }
                    )

                    TaskDatePicker(onConfirm = { date = it })
                }
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm(title, description, group, date)
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(onConfirm: (LocalDate) -> Unit = {}) {
    val datePickerState = rememberDatePickerState()
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val date = datePickerState.selectedDateMillis?.let { longToLocalDate(it).toString() }

    Row {
        IconButton(onClick = { showDialog.value = true }) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "pick Date")
        }
        Text(
            text = date ?: "Pick a date",
            modifier = Modifier
                .padding(8.dp)
                .align(
                    Alignment.CenterVertically
                )
        )
    }


    if (showDialog.value) {
        DatePickerDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    if (datePickerState.selectedDateMillis != null) {
                        onConfirm(longToLocalDate(datePickerState.selectedDateMillis!!))
                        showDialog.value = false
                    }
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
