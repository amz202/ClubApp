package com.example.clubapp.ui.screens.Common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.clubapp.R

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, onRetry: (() -> Unit)) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "Failed To Load :/", modifier = Modifier.padding(16.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "Retry")
        }
    }
}