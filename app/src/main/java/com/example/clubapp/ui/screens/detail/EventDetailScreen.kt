package com.example.clubapp.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.clubapp.network.response.EventResponse
import com.example.clubapp.ui.navigation.EventParticipantsNav
import com.example.clubapp.ui.viewModels.EventViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel,
    navController: NavHostController
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(eventId) {
        eventViewModel.getEvent(eventId)
    }

    val eventState = eventViewModel.eventOfId.collectAsState()
    val event = eventState.value

    if (event == null) {
        return
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    eventViewModel.joinEvent(event.id)
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Join")
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Event",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            EventParticipantsNav(
                                eventId = event.id,
                                eventName = event.name
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ){paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column {
                // Event header with image and name
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier
                        .background(color = Color.DarkGray)
                        .fillMaxWidth()
                        .height(96.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = event.name,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Event description
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),     color = Color(0xFFE3F2FD)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Description",
                            modifier = Modifier.padding(bottom = 8.dp))
                        Text(event.description)
                    }
                }

                // Event details container
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),     color = Color(0xFFE3F2FD)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Date and Time",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = formatDateTimeString(event.dateTime))
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = event.location)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Attendees",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "${event.attendeeCount}${
                                if (event.capacity != null) "/${event.capacity}" else ""
                            } attending")
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Organizer",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "Organized by ${event.organizedBy}")
                        }
                    }
                }
            }
        }
}}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTimeString(isoDateString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoDateString)
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

        val formattedDate = dateTime.format(dateFormatter)
        val formattedTime = dateTime.format(timeFormatter)

        "$formattedDate | $formattedTime"
    } catch (e: Exception) {
        isoDateString
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun EventDetailScreenPreview() {
//    val mockEvent = EventResponse(
//        name = "Photography Workshop",
//        description = "Learn the basics of photography and camera settings in this hands-on workshop. Bring your own camera!",
//        clubId = "club123",
//        dateTime = "May 15, 2024 | 2:00 PM",
//        location = "Art Building, Room 302",
//        capacity = "50",
//        organizedBy = "Photography Club",
//        id = "event123",
//        attendeeCount = 28,
//        tags = "photography,workshop,learning"
//    )
//
//    // Using a stub ViewModel for previe
//
//
//    MaterialTheme {
//        EventDetailScreen(
//            event = mockEvent
//        )
//    }
//}
