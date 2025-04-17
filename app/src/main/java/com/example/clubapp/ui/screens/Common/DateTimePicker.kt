package com.example.clubapp.ui.screens.Common

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimePicker(
    initialDateTime: LocalDateTime = LocalDateTime.now(),
    onDateTimeSelected: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var pickedDate by remember { mutableStateOf(initialDateTime.toLocalDate()) }
    var pickedTime by remember { mutableStateOf(initialDateTime.toLocalTime()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Update the combined date time when either changes
    val updateDateTime = {
        val combinedDateTime = LocalDateTime.of(pickedDate, pickedTime)
        onDateTimeSelected(combinedDateTime)
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        OutlinedButton(onClick = { showDatePicker = true }) {
            Text("Pick Date: ${pickedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = { showTimePicker = true }) {
            Text("Pick Time: ${pickedTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = pickedDate.atStartOfDay()
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                            pickedDate = date
                            updateDateTime()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                pickedTime = LocalTime.of(hourOfDay, minute)
                showTimePicker = false
                updateDateTime()
            },
            pickedTime.hour,
            pickedTime.minute,
            true
        )

        timePickerDialog.show()
        showTimePicker = false // Reset after showing
    }
}
