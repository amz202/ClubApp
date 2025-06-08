package com.example.clubapp.ui.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun HomeScreenProfile(
    name: String,
    email: String,
    photoUrl: String? = null,
    modifier: Modifier = Modifier,
    role: String? = null
) {
    val profileHeight = if (role == null) 80.dp else 80.dp

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(profileHeight)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle profile initial
            if (photoUrl == null) {
                Box(
                    modifier = Modifier
                        .size(if (role == null) 50.dp else 40.dp)
                        .clip(CircleShape)
                        .background(getColorForUser(name)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }else{
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "pfp",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (role == null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = email,
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Role badge
            if (role != null) {
                Text(
                    text = role,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

val avatarColors = listOf(
    Color(0xFFEF5350), // Red
    Color(0xFFAB47BC), // Purple
    Color(0xFF5C6BC0), // Indigo
    Color(0xFF29B6F6), // Light Blue
    Color(0xFF66BB6A), // Green
    Color(0xFFFFA726), // Orange
    Color(0xFF8D6E63), // Brown
    Color(0xFFEC407A), // Pink
    Color(0xFFFFCA28), // Amber
)
fun getColorForUser(name: String): Color {
    val index = (name.hashCode() and 0xFFFFFFF) % avatarColors.size
    return avatarColors[index]
}

@Preview(showBackground = true)
@Composable
fun HomeScreenProfilePreview() {
    MaterialTheme {
        HomeScreenProfile(
            name = "John Doe",
            email = "john.doe@example.com",
            photoUrl = "https://a1.espncdn.com/combiner/i?img=%2Fphoto%2F2020%2F1115%2Fr775995_1296x729_16%2D9.jpg"
        )
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenProfileWithRolePreview() {
    MaterialTheme {
        HomeScreenProfile(
            name = "John Doe",
            email = "john.doe@example.com",
            role = "Admin"
        )
    }
}