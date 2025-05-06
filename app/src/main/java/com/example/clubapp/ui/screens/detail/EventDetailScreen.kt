package com.example.clubapp.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.clubapp.ui.cards.isEventInPast
import com.example.clubapp.ui.dialog.AddEventNewsDialog
import com.example.clubapp.ui.dialog.EventActionMenu
import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.ui.screens.Common.ErrorScreen
import com.example.clubapp.ui.screens.Common.LoadingScreen
import com.example.clubapp.ui.viewModels.NavigationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailStateScreen(
    eventId: String,
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel,
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    LaunchedEffect(eventId) {
        eventViewModel.getEvent(eventId)
        eventViewModel.getEventRole(eventId)
        eventViewModel.getEventNews(eventId)
    }
    val singleEventState = eventViewModel.singleEventUiState

    when (singleEventState) {
        is BaseUiState.Success -> {
            EventDetailScreen(
                eventId = eventId,
                modifier = modifier,
                eventViewModel = eventViewModel,
                navController = navController,
                navViewModel
            )
        }

        is BaseUiState.Loading -> LoadingScreen()
        is BaseUiState.Error -> ErrorScreen()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel,
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val eventState = eventViewModel.eventOfId.collectAsState()
    val event = eventState.value
    val ownEventRole by eventViewModel.userEventRole.collectAsState(null)
    val isMember = ownEventRole?.role != null
    val showAddEvent = navViewModel.showAddEventNewsDialog.collectAsState().value
    val capacityLimitReached: Boolean? = event?.capacity?.toIntOrNull()?.let { capacity ->
        event.attendeeCount.let { attendeeCount ->
            if (capacity > 0) capacity <= attendeeCount else null
        }
    }
    val eventNewsState = eventViewModel.eventNewsUiState
    val joinEventState = eventViewModel.joinEventUiState

    LaunchedEffect(joinEventState) {
        if (joinEventState is BaseUiState.Success) {
            eventViewModel.getEventRole(eventId)
        }
    }

    if (event == null) {
        return
    }

    Scaffold(
        floatingActionButton = {
            if (!isMember && (capacityLimitReached == null || !capacityLimitReached)) {
                FloatingActionButton(
                    onClick = {
                        eventViewModel.joinEvent(event.id)
                    }
                ) {
                    when (joinEventState) {
                        is BaseUiState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        else -> {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Join")
                        }
                    }
                }
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
                                eventName = event.name,
                                ownRole = ownEventRole?.role
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Participants"
                        )
                    }
                    if (isMember && !isEventInPast(event.dateTime)) {
                        var showMenu by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options"
                                )
                            }
                            EventActionMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                isMember = isMember,
                                onLeaveEvent = {
                                    eventViewModel.leaveEvent(eventId)
                                    navController.popBackStack()
                                },
                                role = ownEventRole?.role ?: "attendee",
                                onAddNews = {
                                    navViewModel.showAddEventNewsDialog(eventId)
                                    showMenu = false
                                },
                                onDeleteEvent = {
                                    eventViewModel.deleteEvent(eventId)
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(vertical = 8.dp)

    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Event header with image and name
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.DarkGray)
                            .fillMaxWidth()
                            .height(96.dp)
                    )
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
                        .padding(horizontal = 16.dp, vertical = 8.dp), color = Color(0xFFE3F2FD)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Description",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(event.description)
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFFE3F2FD)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Event Updates",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        when (eventNewsState) {
                            is BaseUiState.Success -> {
                                val eventNews = eventViewModel.eventNews.collectAsState().value
                                    ?.sortedByDescending { it.createdOn } // Sort by newest first

                                if (eventNews.isNullOrEmpty()) {
                                    Text(text = "No updates available")
                                } else {
                                    var expandedNews by remember { mutableStateOf(false) }
                                    val displayedNews =
                                        if (expandedNews) eventNews else eventNews.take(1)

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        displayedNews.forEach { news ->
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                color = Color(0xFFEEEEEE)
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(12.dp)
                                                ) {
                                                    Text(text = news.news)
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = formatDateTimeString(news.createdOn),
                                                        color = Color.Gray
                                                    )
                                                }
                                            }
                                        }

                                        if (eventNews.size > 1) {
                                            TextButton(
                                                onClick = { expandedNews = !expandedNews },
                                                modifier = Modifier
                                                    .align(Alignment.End)
                                                    .padding(0.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Text(text = if (expandedNews) "Show Less" else "Show More")
                                                    Icon(
                                                        imageVector = if (expandedNews)
                                                            Icons.Default.KeyboardArrowUp
                                                        else
                                                            Icons.Default.KeyboardArrowDown,
                                                        contentDescription = if (expandedNews) "Collapse" else "Expand"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            is BaseUiState.Loading -> {
                                Text(text = "Loading news...")
                            }

                            is BaseUiState.Error -> {
                                Text(text = "No news")
                            }
                        }
                    }
                }
                // Event details container
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), color = Color(0xFFE3F2FD)
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
                            Text(
                                text = "${event.attendeeCount}${
                                    if (event.capacity != "null") "/${event.capacity}" else ""
                                } attending"
                            )
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
        if (showAddEvent) {
            AddEventNewsDialog(
                navViewModel = navViewModel,
                eventViewModel = eventViewModel
            )
        }
    }
}

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
