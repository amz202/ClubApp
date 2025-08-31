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
import com.example.clubapp.network.response.ClubMembersResponse
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.clubapp.network.response.EventParticipantsResponse
import com.example.clubapp.ui.viewModels.ClubViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.clubapp.network.response.RoleResponse
import com.example.clubapp.ui.dialog.ClubRoleDialog
import com.example.clubapp.ui.dialog.EventRoleDialog
import com.example.clubapp.ui.navigation.ClubRequestsNav
import com.example.clubapp.ui.viewModels.EventViewModel
import com.example.clubapp.ui.viewModels.NavigationViewModel
import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.ui.screens.Common.ErrorScreen
import com.example.clubapp.ui.screens.Common.LoadingScreen
import com.example.clubapp.ui.theme.PlusJakarta


@Composable
fun ClubMembersListStateScreen(
    modifier: Modifier = Modifier,
    clubId: String,
    clubName: String,
    navController: NavHostController,
    clubViewModel: ClubViewModel,
    navViewModel: NavigationViewModel,
    ownClubRole: String?
) {
    LaunchedEffect(clubId) {
        clubViewModel.getClubMembers(clubId)
    }

    val membersState = clubViewModel.clubMemberUiState

    when (membersState) {
        is BaseUiState.Success -> {
            ClubMembersList(
                modifier = modifier,
                clubId = clubId,
                clubName = clubName,
                navController = navController,
                clubViewModel = clubViewModel,
                navViewModel = navViewModel,
                ownClubRole = ownClubRole
            )
        }

        is BaseUiState.Loading -> LoadingScreen()
        is BaseUiState.Error -> ErrorScreen(
            onRetry = {
                clubViewModel.getClubMembers(clubId)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubMembersList(
    modifier: Modifier = Modifier,
    clubId: String,
    clubName: String,
    navController: NavHostController,
    clubViewModel: ClubViewModel,
    navViewModel: NavigationViewModel,
    ownClubRole: String?
) {
    val members by clubViewModel.clubMembers.collectAsState(initial = emptyList())
    val showClubDialog by navViewModel.showClubRoleDialog.collectAsState(false)
    val clubRoleUser by navViewModel.clubRoleUser.collectAsState(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Club Members",
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
                    if (ownClubRole=="creator" || ownClubRole=="admin"){
                        IconButton(
                            onClick = { navController.navigate(ClubRequestsNav(clubId = clubId, clubName = clubName)) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockClock,
                                contentDescription = "Requests"
                            )
                        }
                    }else{
                        Box(modifier = Modifier.width(32.dp)) {}
                    }
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
                        role = member.clubRole,
                        modifier = Modifier.clickable {
                            navViewModel.showClubRoleDialog(member.id)
                        }
                    )
                    if (showClubDialog && clubRoleUser == member.id) {
                        ClubRoleDialog(
                            clubId = clubId,
                            userName = member.name,
                            userEmail = member.email,
                            currentRole = member.clubRole,
                            navViewModel = navViewModel,
                            clubViewModel = clubViewModel,
                            userId = member.id,
                            ownRole = ownClubRole ?: "member"
                        )
                    }
                }
            }
        }
        //
    }
}