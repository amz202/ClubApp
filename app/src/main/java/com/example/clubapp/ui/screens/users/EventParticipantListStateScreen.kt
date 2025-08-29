package com.example.clubapp.ui.screens.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.clubapp.ui.dialog.EventRoleDialog
import com.example.clubapp.ui.viewModels.EventViewModel
import com.example.clubapp.ui.viewModels.NavigationViewModel
import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.ui.screens.Common.ErrorScreen
import com.example.clubapp.ui.screens.Common.LoadingScreen
import com.example.clubapp.ui.theme.PlusJakarta

@Composable
fun EventParticipantListStateScreen(
    modifier: Modifier = Modifier,
    eventId: String,
    eventName: String,
    navController: NavHostController,
    eventViewModel: EventViewModel,
    navViewModel: NavigationViewModel,
    ownEventRole: String?
) {
    LaunchedEffect(eventId) {
        eventViewModel.getEventParticipants(eventId)
    }

    val participantsState = eventViewModel.eventParticipantUiState

    when (participantsState) {
        is BaseUiState.Success -> {
            EventParticipantList(
                modifier = modifier,
                eventId = eventId,
                eventName = eventName,
                navController = navController,
                eventViewModel = eventViewModel,
                navViewModel = navViewModel,
                ownEventRole = ownEventRole
            )
        }
        is BaseUiState.Loading -> LoadingScreen()
        is BaseUiState.Error -> ErrorScreen(
            onRetry = {
                eventViewModel.getEventParticipants(eventId)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventParticipantList(
    modifier: Modifier = Modifier,
    eventId: String,
    eventName: String,
    navController: NavHostController,
    eventViewModel: EventViewModel,
    navViewModel: NavigationViewModel,
    ownEventRole: String?
) {

    val members by eventViewModel.eventParticipants.collectAsState(initial = emptyList())
    val showEventRoleDialog by navViewModel.showEventRoleDialog.collectAsState(false)
    val eventRoleUser by navViewModel.eventRoleUser.collectAsState(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Event Participants",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontFamily = PlusJakarta,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
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
                    Box(modifier = Modifier.width(32.dp)) {}
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                items(members) { member ->
                    HomeScreenProfile(
                        name = member.name,
                        email = member.email,
                        role = member.eventRole,
                        modifier = Modifier.clickable{
                            navViewModel.showEventRoleDialog(member.id)
                        }
                    )
                    if(showEventRoleDialog && eventRoleUser == member.id){
                        EventRoleDialog(
                            eventId = eventId,
                            userName = member.name,
                            userEmail = member.email,
                            currentRole = member.eventRole,
                            navViewModel = navViewModel,
                            eventViewModel = eventViewModel,
                            userId = member.id,
                            ownRole = ownEventRole?: "attendee"
                        )
                    }
                }
            }
        }
    }
}