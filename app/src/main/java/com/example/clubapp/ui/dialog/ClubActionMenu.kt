package com.example.clubapp.ui.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubActionMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onAddEventClick: () -> Unit,
    canAddEvent: Boolean,
    isMember: Boolean,
    onLeaveClub: () -> Unit,
    onDeleteClub: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        if (canAddEvent) {
            DropdownMenuItem(
                text = { Text("Add Event") },
                onClick = onAddEventClick,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.EventRepeat,
                        contentDescription = null
                    )
                }
            )
        }
        if(isMember){
            DropdownMenuItem(
                text = { Text("Leave Club") },
                onClick = onLeaveClub,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null
                    )
                }
            )
        }
        if (canAddEvent) {
            DropdownMenuItem(
                text = { Text("Delete Club") },
                onClick = onDeleteClub,
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