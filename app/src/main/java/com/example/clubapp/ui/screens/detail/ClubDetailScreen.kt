package com.example.clubapp.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
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
import com.example.clubapp.ui.cards.EventItem
import com.example.clubapp.ui.navigation.AddEventNavA
import com.example.clubapp.ui.navigation.ClubMembersNav
import com.example.clubapp.ui.navigation.EventDetailNav
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.EventViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.clubapp.ui.dialog.ClubActionMenu
import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.ui.screens.Common.ErrorScreen
import com.example.clubapp.ui.screens.Common.LoadingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubDetailStateScreen(
    clubId: String,
    clubViewModel: ClubViewModel,
    eventViewModel: EventViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(clubId) {
        clubViewModel.getClub(clubId)
        clubViewModel.getClubRole(clubId)
    }

    val clubState = clubViewModel.singleClubUiState

    when (clubState) {
        is BaseUiState.Success -> {
            ClubDetailScreen(
                clubId = clubId,
                clubViewModel = clubViewModel,
                eventViewModel = eventViewModel,
                navController = navController,
                modifier = modifier
            )
        }
        is BaseUiState.Loading -> LoadingScreen()
        is BaseUiState.Error -> ErrorScreen()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubDetailScreen(
    clubId: String,
    clubViewModel: ClubViewModel,
    eventViewModel: EventViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(clubId) {
        clubViewModel.getClub(clubId)
        eventViewModel.getClubEvents(clubId)
        clubViewModel.getClubRole(clubId)
    }

    val club = clubViewModel.clubOfId.collectAsState().value
    val clubEvents = eventViewModel.clubEvents.collectAsState().value
    val ownClubRole by clubViewModel.userClubRole.collectAsState(null)
    val isMember = ownClubRole?.role != null

    val clubEventState = eventViewModel.clubEventsUiState

    if (club == null) {
        return
    }

    Scaffold(
        floatingActionButton = {
            if (!isMember) {
                FloatingActionButton(
                    onClick = {
                        clubViewModel.joinClub(clubId)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Join")
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Club",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
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
                            ClubMembersNav(
                                clubId = clubId,
                                clubName = club.name,
                                ownRole = ownClubRole?.role
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Members"
                        )
                    }

                    if (isMember) {
                        var showMenu by remember { mutableStateOf(false) }
                        Box{
                            IconButton(onClick = { showMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options"
                                )
                            }

                            ClubActionMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                onAddEventClick = {
                                    navController.navigate(
                                        AddEventNavA(
                                            clubId = club?.id ?: ""
                                        )
                                    )
                                    showMenu = false
                                },
                                canAddEvent = ownClubRole?.role == "admin" || ownClubRole?.role == "creator",
                                isMember = isMember,
                                onLeaveClub = {
                                    clubViewModel.leaveClub(clubId)
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
                    .verticalScroll(rememberScrollState())  // Make the column scrollable
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.DarkGray)
                            .fillMaxWidth()
                            .height(96.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = club.name,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFFE3F2FD)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Description",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(club.description)
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color(0xFFE3F2FD)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = "Tags",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = club.tags)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Members",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "${club.memberCount} members")
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Created By",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "Created by ${club.createdBy}")
                        }

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
                            Text(text = formatDateTimeString(club.createdOn))
                        }
                    }
                }

                Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),     color = Color(0xFFE3F2FD)
                ){
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Club Events",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        when (clubEventState) {
                            is BaseUiState.Success -> {
                                val clubEvents = eventViewModel.clubEvents.collectAsState().value
                                Column(
                                    modifier = Modifier.padding(top = 20.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    clubEvents?.forEach { event ->
                                        EventItem(
                                            eventResponse = event,
                                            onClick = {
                                                navController.navigate(
                                                    EventDetailNav(eventId = event.id)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                            is BaseUiState.Loading -> {
                                Text(text = "Loading events...")
                            }
                            is BaseUiState.Error -> {
                                Text(text = "No events scheduled")
                            }
                        }
                    }
                }
            }
        }
    }
}
