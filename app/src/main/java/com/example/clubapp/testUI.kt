package com.example.clubapp

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clubapp.network.request.AuthUser
import com.example.clubapp.signin.GoogleAuthClient
import com.example.clubapp.signin.SignInViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.EventViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun TestScreen(
    signInViewModel: SignInViewModel,
    clubViewModel: ClubViewModel,
    eventViewModel: EventViewModel,
    userPreferences: UserPreferences
) {
    val context = LocalContext.current
    val authClient = remember { GoogleAuthClient(context) }

    val clubUiState = clubViewModel.uiState
    val eventUiState = eventViewModel.uiState

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("AUTH TESTS", fontWeight = FontWeight.Bold)
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                authClient.signOut()
            }
        }) {
            Text("Sign Out")
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                authClient.signIn(signInViewModel)
            }
        }) {
            Text("Sign In with Google")
        }

        Divider()

        Text("CLUB TESTS", fontWeight = FontWeight.Bold)

        Button(onClick = {
            clubViewModel.getClubs()
        }) {
            Text("Get Clubs")
        }

        Button(onClick = {
            clubViewModel.createClub(
                ClubRequest(
                    name = "first",
                    description = "ok",
                    tags = "usaf"
                )
            )
        }) {
            Text("Create Club")
        }

        Button(onClick = {
            // Replace with valid club ID
            clubViewModel.joinClub("78c4ad06-282b-4fcf-8423-37d93c2e886f")
        }) {
            Text("Join Club")
        }

        Button(onClick = {
            clubViewModel.leaveClub("78c4ad06-282b-4fcf-8423-37d93c2e886f")
        }) {
            Text("Leave Club")
        }

        Button(onClick = {
            clubViewModel.deleteClub("78c4ad06-282b-4fcf-8423-37d93c2e886f")
        }) {
            Text("Delete Club")
        }

        Divider()

        Text("EVENT TESTS", fontWeight = FontWeight.Bold)

        Button(onClick = {
            eventViewModel.getEvents()
        }) {
            Text("Get Events")
        }

        Button(onClick = {
            eventViewModel.createEvent(
                EventRequest(
                    name = "Test Event",
                    description = "Sample event",
                    location = "Auditorium",
                    dateTime = "2025-05-01T10:00:00",
                    capacity = "2",
                    organizedBy = "yes",
                    clubId = null,
                    tags = "akf",
                )
            )
        }) {
            Text("Create Event")
        }

        Button(onClick = {
            eventViewModel.joinEvent("edc27ca8-8cab-4879-9790-e02b555503f3")
        }) {
            Text("Join Event")
        }

        Button(onClick = {
            eventViewModel.leaveEvent("edc27ca8-8cab-4879-9790-e02b555503f3")
        }) {
            Text("Leave Event")
        }

        Button(onClick = {
            eventViewModel.deleteEvent("edc27ca8-8cab-4879-9790-e02b555503f3")
        }) {
            Text("Delete Event")
        }

        Divider()

        Text("CLUB STATE:")
        Text(clubUiState.toString(), fontSize = 12.sp)

        Text("EVENT STATE:")
        Text(eventUiState.toString(), fontSize = 12.sp)
    }
}

@Composable
fun SignInButton(viewModel: SignInViewModel, userPreferences: UserPreferences) {
    val context = LocalContext.current
    val authClient = remember { GoogleAuthClient(context) }

    Column {

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    authClient.signOut()
                }
            }
        ) {
            Text("Sign Out")
        }

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    authClient.signIn(viewModel)
                }
            }
        ) {
            Text("Sign in with Google")
        }
    }
}



@Composable
fun UserProfile(user: AuthUser, viewModel: SignInViewModel) {
    val context = LocalContext.current
    val authClient = remember { GoogleAuthClient(context) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Welcome, ${user.name}", fontSize = 20.sp)
        Text(text = "Email: ${user.email}", fontSize = 16.sp, color = Color.Gray)

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    authClient.signOut()
                }
            }
        ) {
            Text("Sign Out")
        }
    }
}

