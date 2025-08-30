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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
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
import com.example.clubapp.ui.theme.ClubAppTheme

@Composable
fun UserRequestProfile(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    photoUrl: String? = null,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {}
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
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
            // Circle profily
            if (photoUrl == null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = email,
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            //Buttons
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ){
                IconButton(onClick = onAccept, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Accept",
                        tint = Color(0xFF7BFF7B),
                        modifier = Modifier.size(38.dp)
                    )
                }
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                IconButton(onClick = onDecline, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Decline",
                        tint = Color(0xFFFF7B69),
                        modifier = Modifier.size(38.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserRequestProfilePreview() {
    ClubAppTheme {
        UserRequestProfile(
            name = "John Doe",
            email = "john.doe@example.com",
        )
    }
}

@Composable
fun AcceptUserDialog(
    modifier: Modifier = Modifier,
    onAccept: () -> Unit = {},
    onDismiss: () -> Unit = {},
    clubName: String,
    userName: String
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Accept User",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Text(
                text = "Accept $userName to join $clubName?",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        },
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text(
                    text = "Accept",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun DeclineUserDialog(
    modifier: Modifier = Modifier,
    onDecline: () -> Unit = {},
    onDismiss: () -> Unit = {},
    clubName: String,
    userName: String
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Decline User",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Text(
                text = "Decline $userName from joining $clubName?",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        },
        confirmButton = {
            TextButton(onClick = onDecline) {
                Text(
                    text = "Decline",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        },
        modifier = modifier
    )
}