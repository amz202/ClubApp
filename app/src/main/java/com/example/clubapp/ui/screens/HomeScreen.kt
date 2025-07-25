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
import androidx.compose.runtime.mutableStateOf
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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.data.Datastore.UserPreferences.UserInfo
import com.example.clubapp.signin.GoogleAuthClient
import com.example.clubapp.signin.SignInViewModel
import com.example.clubapp.ui.dialog.HomeScreenDetail
import com.example.clubapp.ui.navigation.SignInScreenNav
import com.example.clubapp.ui.screens.users.HomeScreenProfile
import com.example.clubapp.ui.theme.PlusJakarta
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.EventViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
 * Copyright 2025 Abdul Majid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel,
    eventViewModel: EventViewModel,
    clubViewModel: ClubViewModel,
    userPreferences: UserPreferences,
    signInViewModel: SignInViewModel
) {
    navigationViewModel.updateSelectedItemIndex(0)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var selectedItemIndex = navigationViewModel.selectedItemIndex
    val items = bottomNavItems
    val context = LocalContext.current
    val authClient = remember { GoogleAuthClient(context) }
    var userInfo by remember { mutableStateOf<UserInfo?>(null) }

    LaunchedEffect(Unit) {
        userInfo = userPreferences.getUserInfo()
        clubViewModel.getMyClubs()
        eventViewModel.getMyEvents()
    }

    val clubList by clubViewModel.usersClub.collectAsState(initial = emptyList())
    val eventList by eventViewModel.usersEvents.collectAsState(initial = emptyList())
    val isUserSignedIn = userInfo != null

    val usersClubState = clubViewModel.userClubsUiState
    val userEventState = eventViewModel.userEventsUiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Home",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontFamily = PlusJakarta,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isUserSignedIn) {
                            CoroutineScope(Dispatchers.Main).launch {
                                authClient.signOut()
                                userPreferences.clearUserData()
                                signInViewModel.resetSignInState()
                                clubViewModel.clearClubsState()
                                eventViewModel.clearEventState()
                                userInfo = null
                                navController.navigate(SignInScreenNav)

                            }
                        }
                    }) {
                        if (isUserSignedIn) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Sign Out"
                            )
                        }
                    }
                },
                actions = {
                    Box(modifier = Modifier.width(32.dp)) { }
                },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars),

            )
        },
        bottomBar = {
            NavigationBar() {
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
            .padding(vertical = 8.dp)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            HomeScreenProfile(userInfo?.name.toString(), userInfo?.email.toString(), userInfo?.photoUrl.toString())
            Spacer(modifier = Modifier.height(24.dp))
            HomeScreenDetail(
                clubList = clubList, eventList = eventList,
                navController = navController,
                clubUiState = usersClubState,
                eventUiState = userEventState
            )
        }
    }
}