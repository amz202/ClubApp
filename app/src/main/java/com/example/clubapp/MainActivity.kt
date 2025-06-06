package com.example.clubapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.signin.SignInViewModel
import com.example.clubapp.ui.navigation.AppNavigation
import com.example.clubapp.ui.theme.ClubAppTheme
import com.example.clubapp.ui.viewModels.ChatViewModel
import com.example.clubapp.ui.viewModels.ClubViewModel
import com.example.clubapp.ui.viewModels.EventViewModel
import com.example.clubapp.ui.viewModels.NavigationViewModel

class MainActivity : ComponentActivity() {
    private lateinit var userPreferences: UserPreferences
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Notifications", "Notification permission granted")
        } else {
            Log.d("Notifications", "Notification permission denied")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferences = UserPreferences(this)
        val authViewModel: SignInViewModel = ViewModelProvider(this, SignInViewModel.authFactory)[SignInViewModel::class.java]
        val eventViewModel: EventViewModel = ViewModelProvider(this, EventViewModel.eventFactory)[EventViewModel::class.java]
        val clubViewModel: ClubViewModel = ViewModelProvider(this, ClubViewModel.clubFactory)[ClubViewModel::class.java]
        val navigationViewModel: NavigationViewModel = ViewModelProvider(this)[NavigationViewModel::class.java]
        val chatViewModel: ChatViewModel = ViewModelProvider(this, ChatViewModel.chatFactory)[ChatViewModel::class.java]
        askNotificationPermission()

        enableEdgeToEdge()
        setContent {
            ClubAppTheme {
                AppNavigation(clubUiState = clubViewModel.uiState, eventUiState = eventViewModel.uiState, navigationViewModel = navigationViewModel, clubViewModel = clubViewModel, eventViewModel = eventViewModel, userPreferences = userPreferences, signInViewModel = authViewModel)
            }
        }
    }
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    Log.d("Notifications", "Notification permission already granted")
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            Log.d("Notifications", "Notification permission not required for this Android version")
        }
    }
}


