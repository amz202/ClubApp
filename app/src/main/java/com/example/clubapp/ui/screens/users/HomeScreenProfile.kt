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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreenProfile(
    name: String,
    email: String,
    modifier: Modifier = Modifier,
    role: String? = null
) {
    val profileHeight = if (role == null) 80.dp else 60.dp

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(profileHeight)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder profile picture
            Box(
                modifier = Modifier
                    .size(if (role == null) 50.dp else 40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1).uppercase(),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (role == null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = email,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Role if present
            if (role != null) {
                Text(
                    text = role,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(
                            Color.LightGray,
                            shape = CircleShape
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenProfilePreview() {
    MaterialTheme {
        HomeScreenProfile(
            name = "John Doe",
            email = "john.doe@example.com"
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