package com.example.clubapp.ui.navigation.NavBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.clubapp.ui.navigation.ClubScreenNav
import com.example.clubapp.ui.navigation.EventScreenNav
import com.example.clubapp.ui.navigation.HomeScreenNav
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

data class BottomNavItem(
    val title: String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavItem(
        title = "Events",
        selectedIcon = Icons.Filled.Event,
        unselectedIcon = Icons.Outlined.Event,
    ),
    BottomNavItem(
        title = "Clubs",
        selectedIcon = Icons.Filled.Group,
        unselectedIcon = Icons.Outlined.Group,
    )
)