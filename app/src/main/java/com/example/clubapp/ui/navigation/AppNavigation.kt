package com.example.clubapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.ui.screens.ClubListScreen
import com.example.clubapp.ui.screens.EventListScreen
import com.example.clubapp.ui.screens.HomeScreen
import com.example.clubapp.ui.screens.addClub.AddClubScreen
import com.example.clubapp.ui.screens.addEvent.AddEventScreenA
import com.example.clubapp.ui.screens.addEvent.AddEventScreenB
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
    userPreferences: UserPreferences
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreenNav
    ) {
        composable<HomeScreenNav> {
            HomeScreen(
                navController = navController,
                navigationViewModel = navigationViewModel,
                eventViewModel = eventViewModel,
                clubViewModel = clubViewModel,
                userPreferences = userPreferences
            )
        }

        composable<ClubScreenNav> {
            ClubListScreen(
                clubUiState,
                navController, navigationViewModel = navigationViewModel
            )
        }

        composable<EventScreenNav> {
            EventListScreen(
                eventUiState,
                navController, navigationViewModel = navigationViewModel
            )
        }

        composable<AddEventNavA> {
            AddEventScreenA(
                navController = navController,
            )
        }

        composable<AddEventNavB>{
            val args = it.toRoute<AddEventNavB>()
            AddEventScreenB(
                navController = navController,
                name = args.name,
                description = args.description,
                tags = args.tags,
                eventViewModel = eventViewModel
            )
        }

        composable<AddClubNav>{
            AddClubScreen(
                navController = navController,
                clubViewModel = clubViewModel
            )
        }
    }
}