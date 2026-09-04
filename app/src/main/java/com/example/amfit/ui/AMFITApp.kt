package com.example.amfit.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.amfit.navigation.Screen
import com.example.amfit.ui.components.BottomBar
import com.example.amfit.ui.components.TopBar
import com.example.amfit.ui.home.HomeScreen
import com.example.amfit.ui.profile.ProfileScreen
import com.example.amfit.ui.workout.WorkoutScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AMFITApp() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentScreen = Screen.entries.firstOrNull {
        it.route == currentRoute
    } ?: Screen.Home

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                title = currentScreen.titleRes,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Workout.route) {
                WorkoutScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}