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

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authClient = remember { GoogleAuthClient(context) }

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
                    val signInResult = authClient.signIn(signInViewModel)
                    if (signInResult) {
                        navController.navigate(HomeScreenNav) {
                            popUpTo("signin") { inclusive = true } //user cant go back to this screen using back button
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .width(250.dp)
        ) {
            Text("Sign in with Google")
        }
    }
}