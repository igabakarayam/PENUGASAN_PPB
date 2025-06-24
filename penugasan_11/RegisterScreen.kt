package com.example.tugasppb

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// (Paste the Composable function from my previous answer here)
@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateOfBirth = sdf.format(selectedDate.time)
            errorMessage = null
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

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
            Text("Create Account", style = MaterialTheme.typography.headlineMedium)
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
                value = fullName,
                onValueChange = { fullName = it; errorMessage = null },
                label = { Text("Full Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = {},
                label = { Text("Date of Birth") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select Date",
                        modifier = Modifier.clickable { datePickerDialog.show() }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() }
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
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = null },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(onClick = {
                if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || fullName.isBlank() || dateOfBirth.isBlank()) {
                    errorMessage = "All fields must be filled."
                } else if (password != confirmPassword) {
                    errorMessage = "Passwords do not match."
                } else if (password.length < 6) {
                    errorMessage = "Password must be at least 6 characters long."
                } else {
                    errorMessage = null
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                user?.let {
                                    val userData = hashMapOf(
                                        "fullName" to fullName,
                                        "dateOfBirth" to dateOfBirth,
                                        "email" to email
                                    )
                                    db.collection("users").document(it.uid).set(userData)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Sign Up Successful! Please log in.", Toast.LENGTH_SHORT).show()
                                            navController.navigate("login") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
                                            user.delete()
                                        }
                                }
                            } else {
                                Toast.makeText(context, task.exception?.message ?: "Sign Up Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Sign Up")
            }
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }) {
                Text("Already have an account? Sign In")
            }
        }
    }
}