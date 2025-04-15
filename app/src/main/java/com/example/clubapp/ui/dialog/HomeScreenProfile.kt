package com.example.clubapp.ui.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clubapp.network.request.AuthUser

@Composable
fun HomeScreenProfile(name: String,email:String, modifier: Modifier = Modifier){
    Surface(modifier=modifier.padding(16.dp).fillMaxWidth()) {
        Row {
            Text(
                name
            )
        }
    }
}