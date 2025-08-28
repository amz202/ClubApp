package com.example.clubapp.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ClubOpenDialog(
    onDismissRequest: () -> Unit,
    onToggleOpen: () -> Unit,
    modifier: Modifier = Modifier,
    clubName: String,
    isOpen: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onToggleOpen) {
                Text(if (isOpen) "Close Club" else "Open Club")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = { Text(text = clubName) },
        text = {
            Text(text = if (isOpen) "The club is open." else "The club is closed.")
        },
        modifier = modifier
    )
}