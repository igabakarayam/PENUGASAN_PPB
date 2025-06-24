package com.example.tugasppb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tugasppb.ui.theme.TugasPPBTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TugasPPBTheme {
                AuthApp()
            }
        }
    }
}

@Composable
fun AuthApp() {
    val navController = rememberNavController()
    val currentUser = Firebase.auth.currentUser

    // If user is already logged in, go to home. Otherwise, start at onboarding.
    val startDestination = if (currentUser != null) {
        "home"
    } else {
        "onboarding"
    }

    NavHost(navController, startDestination = startDestination) {
        composable("onboarding") { OnboardingScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
    }
}