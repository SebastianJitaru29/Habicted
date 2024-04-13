package com.example.habicted_app.screen.taskscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, LocalDate) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }
    var date: LocalDate? by remember { mutableStateOf(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Task") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // TODO: Show error text if input is invalid
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

                // TODO: add group picker (list of groups of user)
                OutlinedTextField(
                    value = group,
                    onValueChange = { group = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Group") }
                )
                TaskDatePicker(onConfirm = { date = it })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(title, description, group.toInt(), date!!)
                },
                enabled = title.isNotBlank() && description.isNotBlank() && group.isNotBlank() && date != null
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    onConfirm: (LocalDate) -> Unit = {},
    initialShowDialog: Boolean = false,
) {
    val datePickerState = rememberDatePickerState()
    val showDialog = rememberSaveable { mutableStateOf(initialShowDialog) }
    val date = datePickerState.selectedDateMillis?.let { longToLocalDate(it).toString() }

    TextButton(onClick = { showDialog.value = true }) {
        Icon(imageVector = Icons.Default.DateRange, contentDescription = "pick Date")
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
                TextButton(
                    onClick = {
                        if (datePickerState.selectedDateMillis != null) {
                            onConfirm(longToLocalDate(datePickerState.selectedDateMillis!!))
                            showDialog.value = false
                        }
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false },
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun longToLocalDate(value: Long): LocalDate {
    return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DialogPrev() {
    TaskDialog(onDismiss = { }, onConfirm = { _, _, _, _ -> })
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DatePicker() {
    TaskDatePicker(initialShowDialog = true)
}