package com.example.clubapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.data.Datastore.UserPreferences.UserInfo
import com.example.clubapp.network.response.RoleResponse
import com.example.clubapp.signin.SignInViewModel
import com.example.clubapp.ui.screens.ClubListScreen
import com.example.clubapp.ui.screens.EventListScreen
import com.example.clubapp.ui.screens.HomeScreen
import com.example.clubapp.ui.screens.SignInScreen
import com.example.clubapp.ui.screens.addClub.AddClubScreen
import com.example.clubapp.ui.screens.addEvent.AddEventScreenA
import com.example.clubapp.ui.screens.addEvent.AddEventScreenB
import com.example.clubapp.ui.screens.chat.ChatStateScreen
import com.example.clubapp.ui.screens.detail.ClubDetailScreen
import com.example.clubapp.ui.screens.detail.ClubDetailStateScreen
import com.example.clubapp.ui.screens.detail.EventDetailScreen
import com.example.clubapp.ui.screens.detail.EventDetailStateScreen
import com.example.clubapp.ui.screens.users.ClubMembersList
import com.example.clubapp.ui.screens.users.ClubMembersListStateScreen
import com.example.clubapp.ui.screens.users.ClubRequestListStateScreen
import com.example.clubapp.ui.screens.users.EventParticipantList
import com.example.clubapp.ui.screens.users.EventParticipantListStateScreen
import com.example.clubapp.ui.viewModels.ChatViewModel
import com.example.clubapp.ui.viewModels.ClubMemberUiState
import com.example.clubapp.ui.viewModels.ClubUiState
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.EventParticipantUiState
import com.example.clubapp.ui.viewModels.EventRoleUiState
import com.example.clubapp.ui.viewModels.EventUiState
import com.example.clubapp.ui.viewModels.EventViewModel
import com.example.clubapp.ui.viewModels.NavigationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    clubUiState: ClubUiState,
    eventUiState: EventUiState,
    eventViewModel: EventViewModel,
    clubViewModel: ClubViewModel,
    navigationViewModel: NavigationViewModel,
    userPreferences: UserPreferences,
    chatViewModel: ChatViewModel,
    signInViewModel: SignInViewModel
) {
    val navController = rememberNavController()
    var userInfo by remember { mutableStateOf<UserInfo?>(null) }

    LaunchedEffect(Unit) {
        userInfo = userPreferences.getUserInfo()
    }

    val startDestination = if (userInfo!=null) HomeScreenNav else SignInScreenNav

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<SignInScreenNav>{
            SignInScreen(
                signInViewModel = signInViewModel,
                navController = navController
            )
        }

        composable<HomeScreenNav> {
            HomeScreen(
                navController = navController,
                navigationViewModel = navigationViewModel,
                eventViewModel = eventViewModel,
                clubViewModel = clubViewModel,
                userPreferences = userPreferences,
                signInViewModel = signInViewModel
            )
        }

        composable<ClubScreenNav> {
            ClubListScreen(
                clubUiState,
                navController, navigationViewModel = navigationViewModel,
                clubViewModel
            )
        }

        composable<EventScreenNav> {
            EventListScreen(
                eventUiState,
                navController, navigationViewModel = navigationViewModel,
                eventViewModel
            )
        }

        composable<AddEventNavA> {
            val args = it.toRoute<AddEventNavA>()
            AddEventScreenA(
                navController = navController,
                clubId = args.clubId
            )
        }

        composable<AddEventNavB>{
            val args = it.toRoute<AddEventNavB>()
            AddEventScreenB(
                navController = navController,
                name = args.name,
                description = args.description,
                tags = args.tags,
                eventViewModel = eventViewModel,
                clubId = args.clubId
            )
        }

        composable<AddClubNav>{
            AddClubScreen(
                navController = navController,
                clubViewModel = clubViewModel
            )
        }

        composable<EventDetailNav>{
            val args = it.toRoute<EventDetailNav>()
            EventDetailStateScreen(
                eventId = args.eventId,
                eventViewModel = eventViewModel,
                navController = navController,
                navViewModel = navigationViewModel,
                clubId = args.clubId,
                clubName = args.clubName
            )
        }

        composable<EventParticipantsNav>{
            val args = it.toRoute<EventParticipantsNav>()
            EventParticipantListStateScreen(
                eventId = args.eventId,
                eventViewModel = eventViewModel,
                navController = navController,
                eventName = args.eventName,
                navViewModel = navigationViewModel,
                ownEventRole = args.ownRole
            )
        }

        composable<ClubDetailNav>{
            val args = it.toRoute<ClubDetailNav>()
            ClubDetailStateScreen(
                clubId = args.clubId,
                clubViewModel = clubViewModel,
                navController = navController,
                eventViewModel = eventViewModel,
                navViewModel = navigationViewModel
            )
        }

        composable<ClubMembersNav>{
            val args = it.toRoute<ClubMembersNav>()
            ClubMembersListStateScreen(
                clubId = args.clubId,
                clubName = args.clubName,
                navController = navController,
                clubViewModel = clubViewModel,
                navViewModel = navigationViewModel,
                ownClubRole = args.ownRole
            )
        }

        composable<ChatScreenNav>{
            val args = it.toRoute<ChatScreenNav>()
            ChatStateScreen(
                clubId = args.clubId,
                groupId = args.groupId,
                userPreferences = userPreferences,
                chatViewModel = chatViewModel,
                navController = navController,
                groupName = args.groupName
            )
        }

        composable<ClubRequestsNav>{
            val args = it.toRoute<ClubRequestsNav>()
            ClubRequestListStateScreen(
                clubId = args.clubId,
                clubName = args.clubName,
                navController = navController,
                clubViewModel = clubViewModel,
                navViewModel = navigationViewModel,
            )
        }
    }
}