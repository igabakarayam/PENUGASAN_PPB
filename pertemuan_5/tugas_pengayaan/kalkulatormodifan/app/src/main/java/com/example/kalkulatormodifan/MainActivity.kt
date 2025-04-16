package com.example.kalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8FAFC)) {
                    BMICalculatorUI()
                }
            }
        }
    }
}

@Composable
fun BMICalculatorUI() {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf("") }
    var resultColor by remember { mutableStateOf(Color.Gray) }

    val focusManager = LocalFocusManager.current

    fun calculateBMI() {
        val tinggi = height.toFloatOrNull()?.div(100) // convert ke meter
        val berat = weight.toFloatOrNull()

        if (tinggi != null && berat != null && tinggi > 0) {
            val bmi = berat / tinggi.pow(2)
            val category = when {
                bmi < 18.5 -> {
                    resultColor = Color(0xFF42A5F5); "Kurus"
                }
                bmi < 24.9 -> {
                    resultColor = Color(0xFF66BB6A); "Normal"
                }
                bmi < 29.9 -> {
                    resultColor = Color(0xFFFFB74D); "Gemuk"
                }
                else -> {
                    resultColor = Color(0xFFEF5350); "Obesitas"
                }
            }
            bmiResult = "BMI kamu: %.2f\nKategori: %s".format(bmi, category)
        } else {
            bmiResult = "Masukkan tinggi dan berat yang valid!"
            resultColor = Color.Gray
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Kalkulator BMI",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Tinggi (cm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Berat (kg)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        calculateBMI()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Hitung", fontSize = 16.sp)
                }

                if (bmiResult.isNotEmpty()) {
                    Text(
                        text = bmiResult,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = resultColor,
                        lineHeight = 28.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewBMIUI() {
    MaterialTheme {
        BMICalculatorUI()
    }
}
