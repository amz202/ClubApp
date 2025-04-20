package com.example.clubapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.signin.SignInViewModel
import com.example.clubapp.ui.navigation.AppNavigation
import com.example.clubapp.ui.theme.ClubAppTheme
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.EventViewModel
import com.example.clubapp.ui.viewModels.NavigationViewModel

class MainActivity : ComponentActivity() {
    private lateinit var userPreferences: UserPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferences = UserPreferences(this)
        val authViewModel: SignInViewModel = ViewModelProvider(this, SignInViewModel.authFactory)[SignInViewModel::class.java]
        val eventViewModel: EventViewModel = ViewModelProvider(this, EventViewModel.eventFactory)[EventViewModel::class.java]
        val clubViewModel: ClubViewModel = ViewModelProvider(this, ClubViewModel.clubFactory)[ClubViewModel::class.java]
        val navigationViewModel: NavigationViewModel = ViewModelProvider(this)[NavigationViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ClubAppTheme {
                AppNavigation(clubUiState = clubViewModel.uiState, eventUiState = eventViewModel.uiState, navigationViewModel = navigationViewModel, clubViewModel = clubViewModel, eventViewModel = eventViewModel, userPreferences = userPreferences, signInViewModel = authViewModel)
//              TestScreen(signInViewModel = authViewModel, clubViewModel = clubViewModel, eventViewModel = eventViewModel, userPreferences = userPreferences)
            }
        }
    }

}


