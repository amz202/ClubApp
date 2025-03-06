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

class MainActivity : ComponentActivity() {
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferences = UserPreferences(this)
        val viewModel: SignInViewModel = ViewModelProvider(this, SignInViewModel.Factory)
            .get(SignInViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            ClubAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SignInButton(viewModel, userPreferences)
                    }
                }
            }
        }
    }
}


