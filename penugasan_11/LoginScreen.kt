package com.example.tugasppb

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var forgotPasswordDialogBox by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = Firebase.auth
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.starbucks_logo),
                contentDescription = "Starbucks Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Welcome Back!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { forgotPasswordDialogBox = true }) {
                    Text("Forgot Password?")
                }
            }
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Email and password cannot be empty."
                    } else {
                        errorMessage = null
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, task.exception?.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Login")
            }

            // The Google Sign-In button and its logic have been completely removed.

            if (forgotPasswordDialogBox) {
                var resetEmail by remember { mutableStateOf("") }
                AlertDialog(
                    title = { Text("Forgot Password") },
                    text = {
                        OutlinedTextField(
                            value = resetEmail,
                            onValueChange = { resetEmail = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onDismissRequest = { forgotPasswordDialogBox = false },
                    confirmButton = {
                        TextButton(onClick = {
                            if (resetEmail.isNotBlank()) {
                                auth.sendPasswordResetEmail(resetEmail)
                                    .addOnCompleteListener { task ->
                                        val message = if (task.isSuccessful) "Check your email to reset password" else "Registered email not found"
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        forgotPasswordDialogBox = false
                                    }
                            } else {
                                Toast.makeText(context, "Please enter your registered email", Toast.LENGTH_SHORT).show()
                            }
                        }) { Text("Submit") }
                    },
                    dismissButton = {
                        TextButton(onClick = { forgotPasswordDialogBox = false }) { Text("Cancel") }
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                navController.navigate("register") {
                    popUpTo("login") { inclusive = true }
                }
            }) {
                Text("Don't have an account? Sign Up")
            }
        }
    }
}