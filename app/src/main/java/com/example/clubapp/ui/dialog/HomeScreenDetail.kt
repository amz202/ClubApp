package com.example.clubapp.ui.dialog

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.ui.cards.ClubItem
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.clubapp.network.response.EventResponse
import com.example.clubapp.ui.cards.EventItem
import com.example.clubapp.ui.navigation.ClubDetailNav
import com.example.clubapp.ui.navigation.EventDetailNav

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenDetail(
    modifier: Modifier = Modifier,
    clubList: List<ClubResponse>?,
    eventList: List<EventResponse>?,
    navController: NavHostController
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(min = 300.dp, max = 400.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
//        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> MyClubs(clubList = clubList, navController = navController)
                    1 -> MyEvents(eventList = eventList, navController = navController)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(2) { page ->
                    val selected = pagerState.currentPage == page
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (selected) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyClubs(
    modifier: Modifier = Modifier,
    clubList: List<ClubResponse>?,
    navController: NavHostController

) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Clubs",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        if (clubList.isNullOrEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "You haven't joined any clubs yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {LazyColumn(
            contentPadding = PaddingValues(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(clubList) { club ->
                ClubItem(
                    modifier = Modifier.fillMaxWidth(),
                    clubResponse = club,
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
}}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyEvents(
    modifier: Modifier = Modifier,
    eventList: List<EventResponse>?,
    navController: NavHostController
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Upcoming Events",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (eventList.isNullOrEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "You haven't joined any events yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(eventList) { event ->
                    EventItem(
                        modifier = Modifier.fillMaxWidth(),
                        eventResponse = event,
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

@Composable
fun CustomHorizontalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { pageIndex ->
            val isSelected = pagerState.currentPage == pageIndex
            Box(
                modifier = Modifier
                    .size(if (isSelected) 12.dp else 8.dp)
                    .background(
                        color = if (isSelected) Color.Black
                        else Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun HomeScreenDetailPreview() {
//    val sampleClubs = listOf(
//        ClubResponse(
//            name = "Photography Club",
//            description = "A club for photography enthusiasts.",
//            id = "1",
//            createdBy = "admin",
//            tags = "Photography, Art",
//            createdOn = "2023-10-01T12:34:56",
//            memberCount = 120
//        ),
//        ClubResponse(
//            name = "Book Club",
//            description = "A club for book lovers.",
//            id = "2",
//            createdBy = "admin",
//            tags = "Books, Reading",
//            createdOn = "2023-09-15T10:00:00",
//            memberCount = 80
//        )
//    )
//
//    val sampleEvents = listOf(
//        EventResponse(
//            name = "Art Exhibition",
//            description = "Showcasing local artists' work.",
//            clubId = "3",
//            dateTime = "2023-11-15T18:00:00",
//            location = "Art Gallery",
//            capacity = "200",
//            organizedBy = "Art Club",
//            id = "3",
//            attendeeCount = 150,
//            tags = "Art, Exhibition"
//        ),
//        EventResponse(
//            name = "Tech Talk",
//            description = "Discussion on emerging technologies.",
//            clubId = "4",
//            dateTime = "2023-12-01T10:00:00",
//            location = "Auditorium",
//            capacity = "300",
//            organizedBy = "Tech Club",
//            id = "4",
//            attendeeCount = 250,
//            tags = "Technology, Talk"
//        ),
//        EventResponse(
//            name = "Music Night",
//            description = "An evening of live music performances.",
//            clubId = "5",
//            dateTime = "2023-12-20T19:00:00",
//            location = "Open Air Theater",
//            capacity = "500",
//            organizedBy = "Music Club",
//            id = "5",
//            attendeeCount = 400,
//            tags = "Music, Live"
//        )
//    )
//
//    HomeScreenDetail(
//        clubList = sampleClubs,
//        eventList = sampleEvents
//    )
//}