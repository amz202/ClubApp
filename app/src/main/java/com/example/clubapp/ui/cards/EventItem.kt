package com.example.clubapp.ui.cards

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.network.response.EventResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventItem(
    eventResponse: EventResponse,
    modifier: Modifier= Modifier,
    onClick: () -> Unit
) {
    val stripColor = if (isEventInPast(eventResponse.dateTime)) {
        Color.Red
    } else {
        Color.Green
    }
    Card(modifier = modifier, shape = RoundedCornerShape(8.dp), onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(stripColor)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = eventResponse.name,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = eventResponse.tags,
                    color = Color(red = 120, green = 120, blue = 120)

                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Created At Icon",
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = timeUntilEvent(eventResponse.dateTime),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = "Members Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = eventResponse.attendeeCount.toString(),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun timeUntilEvent(dateTime: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val eventDateTime = LocalDateTime.parse(dateTime, formatter)
    val now = LocalDateTime.now()

    return if (eventDateTime.isAfter(now)) {
        val days = ChronoUnit.DAYS.between(now, eventDateTime)
        if (days > 0) {
            "$days d"
        } else {
            val hours = ChronoUnit.HOURS.between(now, eventDateTime)
            if (hours > 0) {
                "$hours h"
            } else {
                val minutes = ChronoUnit.MINUTES.between(now, eventDateTime)
                "$minutes m"
            }
        }
    } else {
        "Passed"
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun isEventInPast(dateTime: String): Boolean {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val eventDateTime = LocalDateTime.parse(dateTime, formatter)
    return eventDateTime.isBefore(LocalDateTime.now())
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun EventItemPreview() {
//    val sampleEvent = EventResponse(
//        name = "Sample Event",
//        description = "This is a sample event description.",
//        clubId = "1",
//        dateTime = "2025-12-25T18:00:00",
//        location = "Sample Location",
//        capacity = "100",
//        organizedBy = "Sample Organizer",
//        id = "1",
//        attendeeCount = 50,
//        tags = "Sample, Event"
//    )
//
//    EventItem(
//        eventResponse = sampleEvent,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    )
//}