package com.example.tugasppb

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(navController: NavHostController) {
    val currentUser = Firebase.auth.currentUser

    if (currentUser == null) {
        UnauthenticatedHomeScreen(navController = navController)
    } else {
        AuthenticatedHomeScreen(navController = navController, currentUser.uid)
    }
}

@Composable
fun AuthenticatedHomeScreen(navController: NavHostController, userId: String) {
    var fullName by remember { mutableStateOf<String?>(null) }
    val db = Firebase.firestore
    val context = LocalContext.current

    LaunchedEffect(userId) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    fullName = document.getString("fullName")
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.starbucks_logo),
                contentDescription = "Starbucks Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Welcome${fullName?.let { ", $it" } ?: ""}!",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                signOut { // Simplified call to signOut
                    Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }) { Text("Logout") }
        }
    }
}

@Composable
fun UnauthenticatedHomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.starbucks_logo),
                contentDescription = "Starbucks Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Welcome to Starbucks Rewards!",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text("Login to Continue")
            }
        }
    }
}

// The signOut function is now much simpler!
fun signOut(onComplete: () -> Unit) {
    Firebase.auth.signOut()
    onComplete()
}