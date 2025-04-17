package com.example.clubapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.clubapp.ui.navigation.ClubScreenNav
import com.example.clubapp.ui.navigation.EventScreenNav
import com.example.clubapp.ui.navigation.HomeScreenNav
import com.example.clubapp.ui.navigation.NavBar.bottomNavItems
import com.example.clubapp.ui.viewModels.NavigationViewModel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.data.Datastore.UserPreferences.UserInfo
import com.example.clubapp.ui.dialog.HomeScreenDetail
import com.example.clubapp.ui.dialog.HomeScreenProfile
import com.example.clubapp.ui.navigation.AddClubNav
import com.example.clubapp.ui.navigation.AddEventNavA
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.EventViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel,
    eventViewModel: EventViewModel,
    clubViewModel: ClubViewModel,
    userPreferences: UserPreferences
) {
    navigationViewModel.updateSelectedItemIndex(0)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var selectedItemIndex = navigationViewModel.selectedItemIndex
    val items = bottomNavItems

    var userInfo by remember { mutableStateOf<UserInfo?>(null) }

    LaunchedEffect(Unit) {
        userInfo = userPreferences.getUserInfo()
    }

    val clubList by clubViewModel.usersClub.collectAsState(initial = emptyList())
    val eventList by eventViewModel.usersEvents.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Home",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex.value == index,
                        onClick = {
                            navigationViewModel.updateSelectedItemIndex(index)
                            when (item.title) {
                                "Home" -> navController.navigate(HomeScreenNav)
                                "Events" -> navController.navigate(EventScreenNav)
                                "Clubs" -> navController.navigate(ClubScreenNav)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex.value) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = item.title
                            )
                        }
                    )

                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            HomeScreenProfile(userInfo?.name.toString(), userInfo?.email.toString())
            HomeScreenDetail(clubList = clubList, eventList = eventList)
        }
    }
}