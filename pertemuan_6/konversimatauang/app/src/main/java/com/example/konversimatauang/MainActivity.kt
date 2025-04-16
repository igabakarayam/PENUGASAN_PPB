package com.example.kalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
                KonversiMataUangApp()
            }
        }
    }
}

@Composable
fun KonversiMataUangApp() {
    var jumlah by remember { mutableStateOf("") }
    var hasilKonversi by remember { mutableStateOf("") }
    var mataUangAsal by remember { mutableStateOf("USD") }
    var mataUangTujuan by remember { mutableStateOf("IDR") }

    val nilaiTukar = mapOf(
        "USD" to 16814.3,
        "EUR" to 19065.9,
        "JPY" to 118.09,
        "SGD" to 12809.3,
        "GBP" to 22340.3,
        "IDR" to 1.0
    )

    val daftarMataUang = listOf("USD", "EUR", "JPY", "SGD", "GBP", "IDR")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF6FF)),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Konversi Mata Uang",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                OutlinedTextField(
                    value = jumlah,
                    onValueChange = { jumlah = it },
                    label = { Text("Jumlah") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dari:", fontWeight = FontWeight.SemiBold)
                        DropdownMataUang(
                            selected = mataUangAsal,
                            onSelected = { mataUangAsal = it },
                            options = daftarMataUang
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Ke:", fontWeight = FontWeight.SemiBold)
                        DropdownMataUang(
                            selected = mataUangTujuan,
                            onSelected = { mataUangTujuan = it },
                            options = daftarMataUang
                        )
                    }
                }

                Button(
                    onClick = {
                        val angka = jumlah.toDoubleOrNull()
                        hasilKonversi = if (angka != null) {
                            val asal = nilaiTukar[mataUangAsal] ?: 1.0
                            val tujuan = nilaiTukar[mataUangTujuan] ?: 1.0
                            val hasil = angka * (tujuan / asal)
                            "Hasil: %.2f $mataUangTujuan".format(hasil)
                        } else {
                            "Input tidak valid"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Konversi", fontSize = 16.sp)
                }

                if (hasilKonversi.isNotEmpty()) {
                    Text(
                        text = hasilKonversi,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun DropdownMataUang(
    selected: String,
    onSelected: (String) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expanded = true },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(selected)
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { item ->
            DropdownMenuItem(
                text = { Text(item) },
                onClick = {
                    onSelected(item)
                    expanded = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKonversi() {
    MaterialTheme {
        KonversiMataUangApp()
    }
}
