package com.foqos.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foqos.android.ui.screens.ProfilesScreen
import com.foqos.android.ui.screens.StatsScreen
import com.foqos.android.ui.screens.SettingsScreen
import com.foqos.android.ui.viewmodel.ProfileViewModel
import com.foqos.android.ui.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoqosApp() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }
    
    // Shared ViewModels across tabs
    val profileViewModel: ProfileViewModel = viewModel()
    val sessionViewModel: SessionViewModel = viewModel()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foqos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Face, contentDescription = "Profiles") },
                    label = { Text("Profiles") },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate("profiles") {
                            popUpTo("profiles") { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Stats") },
                    label = { Text("Stats") },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("stats") {
                            popUpTo("profiles")
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController.navigate("settings") {
                            popUpTo("profiles")
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "profiles",
            modifier = Modifier.padding(padding)
        ) {
            composable("profiles") {
                ProfilesScreen(
                    profileViewModel = profileViewModel,
                    sessionViewModel = sessionViewModel
                )
            }
            composable("stats") {
                StatsScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}
