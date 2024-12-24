package com.example.bookinventoryapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.bookinventoryapp.models.Book
import com.example.bookinventoryapp.utils.fetchBooksFromFirebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseBookListScreen(navController: NavHostController,onGenerateQR: (String) -> Unit) {
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        fetchBooksFromFirebase(
            onResult = { fetchedBooks ->
                books = fetchedBooks
            },
            onError = { exception ->
                errorMessage = exception.message
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Список книг") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Button(onClick = { navController.navigate("user_selection") }) {
                Text("Перейти к управлению пользователями")
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(books.size) { index ->
                    val book = books[index]
                    BookItem(
                        book, onGenerateQR,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun BookItem(book: Book, onGenerateQR: (String) -> Unit,navController:NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Название: ${book.title}")
            Text(text = "Автор: ${book.author}")
            Text(text = "Наличие: ${book.isStock}")
            Text(text = "Количество: ${book.quantity}")
            Text(text = "Цена: ${book.price}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                navController.navigate(
                    "qr_generator?title=${book.title}&author=${book.author}&stock=${book.isStock}&quantity=${book.quantity}&price=${book.price}"
                )
            }) {
                Text("Сгенерировать QR-код")
            }

        }
    }
}
