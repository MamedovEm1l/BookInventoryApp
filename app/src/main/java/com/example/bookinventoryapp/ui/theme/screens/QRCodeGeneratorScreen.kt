package com.example.bookinventoryapp.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookinventoryapp.utils.generateQRCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeGeneratorScreen(
    navController: NavController,
    bookTitle: String,
    bookAuthor: String,
    stockCount: String,
    quantity: Int,
    price: String
) {
    val qrData = remember {
        "Название: $bookTitle\nАвтор: $bookAuthor\nКоличество на складе: $stockCount \nКоличество: $quantity \nЦена: $price"
    }

    val qrBitmap = remember { generateQRCode(qrData) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QR-код книги") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            qrBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "QR-код книги",
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}
