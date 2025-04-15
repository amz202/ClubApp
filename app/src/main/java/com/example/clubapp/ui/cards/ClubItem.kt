package com.example.clubapp.ui.cards

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clubapp.network.response.ClubResponse
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubItem(
    clubResponse: ClubResponse,
    modifier: Modifier
) {
    Card(modifier = modifier, shape = RoundedCornerShape(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Club Icon",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = clubResponse.name,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = clubResponse.tags,
                    color = Color(red = 120, green = 120, blue = 120) // Custom greyish color

                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = "Members Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = clubResponse.memberCount.toString(),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Created At Icon",
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = timeAgo(clubResponse.createdOn),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun timeAgo(createdAt: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val createdTime = LocalDateTime.parse(createdAt, formatter)
        val now = LocalDateTime.now(ZoneId.systemDefault())

        val minutes = ChronoUnit.MINUTES.between(createdTime, now)
        val hours = ChronoUnit.HOURS.between(createdTime, now)
        val days = ChronoUnit.DAYS.between(createdTime, now)

        when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes minute${if (minutes > 1) "s" else ""}"
            hours < 24 -> "$hours hour${if (hours > 1) "s" else ""}"
            else -> "$days day${if (days > 1) "s" else ""}"
        }
    } catch (e: Exception) {
        "Invalid date"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ClubItemPreview() {
    val sampleClub = ClubResponse(
        name = "Photography Club",
        description = "A club for photography enthusiasts.",
        id = "1",
        createdBy = "admin",
        tags = "Photography, Art",
        createdOn = "2023-10-01T12:34:56",
        memberCount = 120
    )

    ClubItem(
        clubResponse = sampleClub,
        modifier = Modifier.padding(16.dp)
    )
}