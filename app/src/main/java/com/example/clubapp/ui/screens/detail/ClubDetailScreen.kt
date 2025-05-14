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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.clubapp.ui.dialog.ClubActionMenu
import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.ui.screens.Common.ErrorScreen
import com.example.clubapp.ui.screens.Common.LoadingScreen
import com.example.clubapp.ui.theme.PlusJakarta

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
        eventViewModel.getClubEvents(clubId)
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

    val club = clubViewModel.clubOfId.collectAsState().value
    val clubEvents = eventViewModel.clubEvents.collectAsState().value
    val ownClubRole by clubViewModel.userClubRole.collectAsState(null)
    val isMember = ownClubRole?.role != null

    val clubEventState = eventViewModel.clubEventsUiState
    val joinClubState = clubViewModel.joinClubUiState

    LaunchedEffect(joinClubState) {
        if (joinClubState is BaseUiState.Success) {
            clubViewModel.getClub(clubId)
        }
    }

    if (club == null) return

    Scaffold(
        floatingActionButton = {
            if (!isMember) {
                FloatingActionButton(onClick = { clubViewModel.joinClub(clubId) }) {
                    when (joinClubState) {
                        is BaseUiState.Loading -> CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        else -> Icon(imageVector = Icons.Default.Add, contentDescription = "Join")
                    }
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Club",
                        fontFamily = PlusJakarta,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            ClubMembersNav(clubId, club.name, ownClubRole?.role)
                        )
                    }) {
                        Icon(Icons.Default.Groups, contentDescription = "Members")
                    }

                    if (isMember) {
                        var showMenu by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More options")
                            }
                            ClubActionMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                onAddEventClick = {
                                    navController.navigate(AddEventNavA(clubId))
                                    showMenu = false
                                },
                                canAddEvent = ownClubRole?.role == "admin" || ownClubRole?.role == "creator",
                                isMember = isMember,
                                onLeaveClub = {
                                    clubViewModel.leaveClub(clubId)
                                    navController.popBackStack()
                                },
                                onDeleteClub = {
                                    clubViewModel.deleteClub(clubId)
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

        Column(
            modifier = modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // Header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 3.dp,
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Text(
                    text = club.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            // Description
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Description",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(club.description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Club Metadata
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ClubDetailRow(Icons.Default.Tag, "Tags", club.tags)
                    ClubDetailRow(Icons.Default.Groups, "Members", "${club.memberCount}")
                    ClubDetailRow(Icons.Default.Person, "Created by", club.createdBy)
                    ClubDetailRow(Icons.Default.DateRange, "Created on", formatDateTimeString(club.createdOn))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Club Events Section
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Club Events",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    when (clubEventState) {
                        is BaseUiState.Success -> {
                            if (clubEvents.isNullOrEmpty()) {
                                Text("No events scheduled.", style = MaterialTheme.typography.bodyMedium)
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    clubEvents.forEach { event ->
                                        EventItem(
                                            eventResponse = event,
                                            onClick = {
                                                navController.navigate(EventDetailNav(event.id))
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        is BaseUiState.Loading -> {
                            Text("Loading events...", style = MaterialTheme.typography.bodyMedium)
                        }

                        is BaseUiState.Error -> {
                            Text("Error loading events.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
@Composable
fun ClubDetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.padding(end = 12.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
