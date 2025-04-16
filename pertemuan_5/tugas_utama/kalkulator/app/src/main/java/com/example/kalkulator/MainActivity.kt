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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                KalkulatorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun KalkulatorApp() {
    var input1 by remember { mutableStateOf("") }
    var input2 by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Kalkulator Sederhana",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                TextField(
                    value = input1,
                    onValueChange = { input1 = it },
                    label = { Text("Angka Pertama") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = input2,
                    onValueChange = { input2 = it },
                    label = { Text("Angka Kedua") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val buttonShape = RoundedCornerShape(12.dp)

                    listOf("+", "-", "*", "/").forEach { symbol ->
                        FilledTonalButton(
                            onClick = {
                                hasil = hitung(input1, input2, symbol)
                            },
                            shape = buttonShape,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(symbol, fontSize = 20.sp)
                        }
                    }
                }

                if (hasil.isNotEmpty()) {
                    Text(
                        text = "Hasil: $hasil",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

fun hitung(a: String, b: String, op: String): String {
    val angka1 = a.toDoubleOrNull() ?: return "Input invalid"
    val angka2 = b.toDoubleOrNull() ?: return "Input invalid"
    return when (op) {
        "+" -> (angka1 + angka2).toString()
        "-" -> (angka1 - angka2).toString()
        "*" -> (angka1 * angka2).toString()
        "/" -> if (angka2 != 0.0) (angka1 / angka2).toString() else "Error: ÷ 0"
        else -> "?"
    }
}

@Preview(showBackground = true)
@Composable
fun KalkulatorPreview() {
    MaterialTheme {
        KalkulatorApp()
    }
}
