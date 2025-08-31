package com.example.clubapp.ui.screens.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.clubapp.ui.dialog.ClubRoleDialog
import com.example.clubapp.ui.screens.Common.ErrorScreen
import com.example.clubapp.ui.screens.Common.LoadingScreen
import com.example.clubapp.ui.theme.PlusJakarta
import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.NavigationViewModel

@Composable
fun ClubRequestListStateScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    clubName:String,
    clubId: String,
    clubViewModel: ClubViewModel,
    navViewModel: NavigationViewModel,
) {
    val clubRequestListState = clubViewModel.clubPendingMemberUiState

    LaunchedEffect(clubId) {
        clubViewModel.getPendingMembers(clubId)
    }
    when (clubRequestListState) {
        is BaseUiState.Success -> {
            ClubRequestListScreen(
                modifier = modifier,
                navController = navController,
                clubName = clubName,
                clubId = clubId,
                clubViewModel = clubViewModel,
                navViewModel = navViewModel,
            )
        }
        is BaseUiState.Loading -> LoadingScreen()
        is BaseUiState.Error -> ErrorScreen(
            onRetry = {
                clubViewModel.getPendingMembers(clubId)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubRequestListScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    clubName:String,
    clubId: String,
    clubViewModel: ClubViewModel,
    navViewModel: NavigationViewModel,
) {
    val pendingMembers by clubViewModel.pendingMembers.collectAsState(initial = emptyList())
    val showAcceptUserDialog by navViewModel.showAcceptUserDialog.collectAsState()
    val showRejectUserDialog by navViewModel.showRejectUserDialog.collectAsState()
    val requestUserName by navViewModel.requestUserName.collectAsState()
    val requestUserId by navViewModel.requestUserId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Club Join Requests",
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
            pendingMembers?.let { membersList ->
                if (membersList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        items(items = membersList) { user ->
                            UserRequestProfile(
                                name = user.name,
                                email = user.email,
                                onAccept = { navViewModel.showAcceptUserDialog(userId = user.userId, userName = user.name) },
                                onDecline = { navViewModel.showRejectUserDialog(userId = user.userId, userName = user.name) },
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No pending requests",
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
    if(showAcceptUserDialog){
        AcceptUserDialog(
            clubName = clubName,
            userName = requestUserName,
            onDismiss = { navViewModel.hideAcceptUserDialog() },
            onAccept = {
                clubViewModel.approveMember(clubId, requestUserId)
                navViewModel.hideAcceptUserDialog()
            }
        )
    }
    if(showRejectUserDialog){
        RejectUserDialog(
            clubName = clubName,
            userName = requestUserName,
            onDismiss = { navViewModel.hideRejectUserDialog() },
            onDecline = {
                clubViewModel.rejectMember(clubId, requestUserId)
                navViewModel.hideRejectUserDialog()
            }
        )
    }
}
