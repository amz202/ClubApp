package com.example.clubapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.signin.GoogleAuthClient
import com.example.clubapp.signin.SignInViewModel
import com.example.clubapp.ui.theme.ClubAppTheme
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.EventViewModel

class MainActivity : ComponentActivity() {
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferences = UserPreferences(this)
        val authViewModel: SignInViewModel = ViewModelProvider(this, SignInViewModel.authFactory)[SignInViewModel::class.java]
        val eventViewModel: EventViewModel = ViewModelProvider(this, EventViewModel.eventFactory)[EventViewModel::class.java]
        val clubViewModel: ClubViewModel = ViewModelProvider(this, ClubViewModel.clubFactory)[ClubViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            ClubAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TestScreen(
                            signInViewModel = authViewModel,
                            clubViewModel = clubViewModel,
                            eventViewModel = eventViewModel,
                            userPreferences = userPreferences
                        )
                    }
                }
            }
        }
    }
}


