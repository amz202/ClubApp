package com.example.clubapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.ui.cards.ClubItem
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.clubapp.ui.navigation.AddClubNav
import com.example.clubapp.ui.navigation.ClubDetailNav
import com.example.clubapp.ui.navigation.ClubScreenNav
import com.example.clubapp.ui.navigation.EventScreenNav
import com.example.clubapp.ui.navigation.HomeScreenNav
import com.example.clubapp.ui.navigation.NavBar.bottomNavItems
import com.example.clubapp.ui.screens.Common.ErrorScreen
import com.example.clubapp.ui.screens.Common.LoadingScreen
import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.ui.viewModels.ClubUiState
import com.example.clubapp.ui.viewModels.NavigationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubListScreen(
    clubUiState: ClubUiState,
    navController: NavHostController,
    navigationViewModel: NavigationViewModel
) {
    when (clubUiState) {
        is BaseUiState.Success -> ClubList(
            clubList = clubUiState.data,
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
fun ClubList(
    modifier: Modifier = Modifier,
    clubList: List<ClubResponse>,
    navController: NavHostController, navigationViewModel: NavigationViewModel
) {
    navigationViewModel.updateSelectedItemIndex(2)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var selectedItemIndex = navigationViewModel.selectedItemIndex

    val items = bottomNavItems
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AddClubNav) },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Club")
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Clubs",
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
            .padding(vertical = 8.dp)
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                items(clubList) { club ->
                    ClubItem(
                        clubResponse = club,
                        modifier = modifier.padding(bottom = 16.dp),
                        onClick = {
                            navController.navigate(
                                ClubDetailNav(
                                    clubId = club.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun ClubListScreenPreview() {
//    val sampleClubs = listOf(
//        ClubResponse(
//            name = "Photography Club",
//            description = "A club for photography enthusiasts.",
//            id = "1",
//            createdBy = "admin",
//            tags = "Photography, Art",
//            createdAt = "2023-10-01T12:34:56",
//            memberCount = 120
//        ),
//        ClubResponse(
//            name = "Book Club",
//            description = "A club for book lovers.",
//            id = "2",
//            createdBy = "admin",
//            tags = "Books, Reading",
//            createdAt = "2023-09-15T10:00:00",
//            memberCount = 80
//        ),
//        ClubResponse(
//            name = "Book Club",
//            description = "A club for book lovers.",
//            id = "2",
//            createdBy = "admin",
//            tags = "Books, Reading",
//            createdAt = "2023-09-15T10:00:00",
//            memberCount = 80
//        ),
//        ClubResponse(
//            name = "Book Club",
//            description = "A club for book lovers.",
//            id = "2",
//            createdBy = "admin",
//            tags = "Books, Reading",
//            createdAt = "2023-09-15T10:00:00",
//            memberCount = 80
//        ),
//        ClubResponse(
//            name = "Book Club",
//            description = "A club for book lovers.",
//            id = "2",
//            createdBy = "admin",
//            tags = "Books, Reading",
//            createdAt = "2023-09-15T10:00:00",
//            memberCount = 80
//        ),
//        ClubResponse(
//            name = "Book Club",
//            description = "A club for book lovers.",
//            id = "2",
//            createdBy = "admin",
//            tags = "Books, Reading",
//            createdAt = "2023-09-15T10:00:00",
//            memberCount = 80
//        ),
//        ClubResponse(
//            name = "Book Club",
//            description = "A club for book lovers.",
//            id = "2",
//            createdBy = "admin",
//            tags = "Books, Reading",
//            createdAt = "2023-09-15T10:00:00",
//            memberCount = 80
//        )
//    )
//
//    ClubList(
//        clubList = sampleClubs
//    )
//}
