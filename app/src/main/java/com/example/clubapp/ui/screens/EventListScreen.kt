package com.example.clubapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.clubapp.network.response.EventResponse
import com.example.clubapp.ui.cards.EventItem
import com.example.clubapp.ui.navigation.AddEventNavA
import com.example.clubapp.ui.navigation.ClubScreenNav
import com.example.clubapp.ui.navigation.EventDetailNav
import com.example.clubapp.ui.navigation.EventScreenNav
import com.example.clubapp.ui.navigation.HomeScreenNav
import com.example.clubapp.ui.navigation.NavBar.bottomNavItems
import com.example.clubapp.ui.screens.Common.ErrorScreen
import com.example.clubapp.ui.screens.Common.LoadingScreen

import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.ui.viewModels.EventUiState
import com.example.clubapp.ui.viewModels.NavigationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventListScreen(
    eventUiState: EventUiState,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel
) {
    when (eventUiState) {
        is BaseUiState.Success -> EventList(
            eventList = eventUiState.data,
            modifier = Modifier,
            navController = navController,
            navigationViewModel = navigationViewModel
        )

        is BaseUiState.Loading -> LoadingScreen()
        is BaseUiState.Error -> ErrorScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventList(
    modifier: Modifier = Modifier,
    eventList: List<EventResponse>,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel
) {
    navigationViewModel.updateSelectedItemIndex(1)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var selectedItemIndex = navigationViewModel.selectedItemIndex

    val items = bottomNavItems
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AddEventNavA()) },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Item")
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Events",
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
        Column(modifier = modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                items(eventList) { event ->
                    EventItem(
                        eventResponse = event,
                        modifier = modifier.padding(bottom = 16.dp),
                        onClick = {
                            navController.navigate(
                                EventDetailNav(
                                    eventId = event.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}