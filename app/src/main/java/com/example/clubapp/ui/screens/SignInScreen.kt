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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import com.example.clubapp.ui.navigation.HomeScreenNav
import com.example.clubapp.ui.theme.Syne
import com.example.clubapp.ui.viewModels.BaseUiState
import com.example.clubapp.R

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
            navController.navigate(HomeScreenNav)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        // Top Half - Title + Caption
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 120.dp) // fine-tuned to mid-upper-half
                .offset(y = 40.dp),   // slight push down
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ClubApp",
                fontFamily = FontFamily(Font(R.font.syne_medium, FontWeight.Medium)),
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your campus, connected.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            )
        }

        // Button - Just below the center
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        authClient.signIn(signInViewModel)
                    }
                },
                modifier = Modifier
                    .width(250.dp)
                    .height(52.dp),
                enabled = signInUiState !is BaseUiState.Loading
            ) {
                Text(
                    text = "Sign in with Google",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Loading State
            if (signInUiState is BaseUiState.Loading) {
                Spacer(modifier = Modifier.height(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

            // Error State
            if (signInUiState is BaseUiState.Error) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Try Again",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


