package com.example.clubapp.ui.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EventActionMenu(
    expanded:Boolean,
    isMember:Boolean,
    onDismissRequest: () -> Unit,
    onLeaveEvent:() -> Unit,
    role:String,
    onAddNews: () -> Unit,
    onDeleteEvent: () -> Unit,
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        if (isMember) {
            DropdownMenuItem(
                text = {Text("Leave Event")},
                onClick = onLeaveEvent,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null
                    )
                }
            )
        }
        if (role == "admin" || role == "head") {
            DropdownMenuItem(
                text = { Text("Add News") },
                onClick = onAddNews,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            )
        }
        if (role == "head") {
            DropdownMenuItem(
                text = { Text("Delete Event") },
                onClick = onDeleteEvent,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null
                    )
                }
            )
        }
    }
}