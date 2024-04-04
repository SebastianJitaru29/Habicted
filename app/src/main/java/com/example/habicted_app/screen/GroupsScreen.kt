package com.example.habicted_app.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GroupScreen(modifier: Modifier = Modifier){
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(49.dp),
    ) {
        Text(text = "Hello")
    }
}