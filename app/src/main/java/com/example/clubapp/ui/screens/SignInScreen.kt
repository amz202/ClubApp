package com.example.clubapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clubapp.signin.GoogleAuthClient
import com.example.clubapp.signin.SignInViewModel
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import com.example.clubapp.ui.navigation.HomeScreenNav
import com.example.clubapp.ui.viewModels.BaseUiState

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authClient = remember { GoogleAuthClient(context) }
    val signInUiState = signInViewModel.uiState

    LaunchedEffect(signInUiState) {
        if (signInUiState is BaseUiState.Success && signInUiState.data != null) {
            navController.navigate(HomeScreenNav) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to ClubApp",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    authClient.signIn(signInViewModel)
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .width(250.dp),
            enabled = signInUiState !is BaseUiState.Loading
        ) {
            Text("Sign in with Google")
        }

        if (signInUiState is BaseUiState.Loading) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Signing in...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (signInUiState is BaseUiState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Try Again",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}