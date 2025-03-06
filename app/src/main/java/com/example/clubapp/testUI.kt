package com.example.clubapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.clubapp.network.request.AuthUser
import com.example.clubapp.signin.GoogleAuthClient
import com.example.clubapp.signin.SignInViewModel
import com.example.clubapp.signin._uiState
import androidx.lifecycle.viewModelScope
import com.example.clubapp.data.Datastore.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

