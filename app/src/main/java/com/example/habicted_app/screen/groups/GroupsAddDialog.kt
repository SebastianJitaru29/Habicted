package com.example.habicted_app.screen.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habicted_app.ui.theme.allColors

@Composable
fun GroupAddDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Color, List<String>) -> Unit,
) {
    // Do state hoisting ?
    var name by rememberSaveable { mutableStateOf("") }
    val members by rememberSaveable { mutableStateOf(listOf<String>()) }
    var color by remember { mutableStateOf(Color.Transparent) }
    var isExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create new group") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") }
                    )
                }
                item {
                    // User add list
                    Text(
                        text = "Add members to the group:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = "icon",
                        //  modifier = Modifier.clickable {
                        //    viewModel.fetchAllUsers()
                        //}
                    )
                }
                item {
                    // Color pick boxes
                    GroupColorPicker(
                        allGroupColors = allColors,
                        currentColor = color,
                        onColorSelected = { color = it },
                        isExpanded = isExpanded,
                        setIsExpanded = { isExpanded = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(name, color, members)
                },
                enabled = name.isNotBlank() && color != Color.Transparent
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

@Composable
fun GroupColorPicker(
    modifier: Modifier = Modifier,
    allGroupColors: List<Color>,
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    isExpanded: Boolean,
    setIsExpanded: (Boolean) -> Unit,
) {

    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("Select a color for the ")
        }
        withStyle(style = SpanStyle(color = if (currentColor == Color.Transparent) MaterialTheme.colorScheme.onBackground else currentColor)) {
            append("group")
        }
        append(":")
    }

    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium
    )


    if (isExpanded) {
        GroupColorPickerExpanded(
            modifier = modifier,
            groupColors = allGroupColors,
            onColorSelected = onColorSelected,
            selectedColor = currentColor,
            onCollapse = { setIsExpanded(false) }
        )
    } else {
        GroupColorPickerCollapsed(
            modifier = modifier,
            groupColors = allGroupColors.subList(0, 3),
            onColorSelected = onColorSelected,
            selectedColor = currentColor,
            onExpand = { setIsExpanded(true) }
        )
    }
}

@Composable
fun GroupColorPickerCollapsed(
    modifier: Modifier,
    groupColors: List<Color>,
    onColorSelected: (Color) -> Unit,
    selectedColor: Color,
    onExpand: () -> Unit,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        groupColors.forEach { color ->
            ColorButton(
                color = color,
                onClick = {
                    onColorSelected(color)
                },
                isSelected = selectedColor == color
            )
        }
        TextButton(onClick = onExpand) {
            Text("More")
        }
    }
}

@Composable
fun GroupColorPickerExpanded(
    modifier: Modifier = Modifier,
    groupColors: List<Color>,
    onColorSelected: (Color) -> Unit,
    selectedColor: Color,
    onCollapse: () -> Unit,
) {
    // Split the groupColors list into sublists of size 4
    val colorRows = groupColors.chunked(4)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        colorRows.forEach { rowColors ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowColors.forEach { color ->
                    ColorButton(
                        color = color,
                        onClick = { onColorSelected(color) },
                        isSelected = selectedColor == color
                    )
                }
            }
        }
        TextButton(onClick = onCollapse) {
            Text("Less")
        }
    }
}

@Composable
fun ColorButton(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit,
    isSelected: Boolean = false,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(color)
    ) {
        if (isSelected) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "Tick")
        } else {
            Text("")
        }
    }

}


@Preview
@Composable
private fun GroupDialog() {
    MaterialTheme {
        GroupAddDialog(onDismiss = { /*TODO*/ }, onConfirm = { _, _, _ -> /*TODO*/ })
    }
}