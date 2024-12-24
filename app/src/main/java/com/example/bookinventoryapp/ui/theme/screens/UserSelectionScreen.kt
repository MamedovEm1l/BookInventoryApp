package com.example.bookinventoryapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookinventoryapp.models.Order
import com.example.bookinventoryapp.utils.OrderViewModel

@Composable
fun UserSelectionScreen(navController: NavController, orderViewModel: OrderViewModel) {
    var userId by remember { mutableStateOf("") }
    val orders by orderViewModel.orders.collectAsState()
    val users by orderViewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        orderViewModel.fetchAllUsersSafe() // Загружаем список пользователей при загрузке экрана
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("Введите ID пользователя") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { orderViewModel.fetchOrdersSafe(userId) }) {
            Text("Показать заказы")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Список всех пользователей:")
        LazyColumn {
            items(users.size) { index ->
                val user = users[index]
                UserItem(user, onUserClick = { selectedUserId ->
                    userId = selectedUserId
                    orderViewModel.fetchOrdersSafe(selectedUserId)
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (orders.isNotEmpty()) {
            Text("Список заказов:")
            LazyColumn {
                items(orders.size) { index ->
                    OrderItem(order = orders[index], onReturnClick = { selectedOrder ->
                        orderViewModel.processReturnSafe(userId, selectedOrder)
                    })
                }
            }
        }
    }
}

@Composable
fun UserItem(userId: String, onUserClick: (String) -> Unit) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = userId, modifier = Modifier.weight(1f))
            Button(onClick = { onUserClick(userId) }) {
                Text("Выбрать")
            }
        }
    }
}


@Composable
fun OrderItem(order: Order, onReturnClick: (Order) -> Unit) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Адрес: ${order.address}")
            Text("Дата: ${order.date}")
            Text("Состояние: ${if (order.state) "Активен" else "Возвращён"}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onReturnClick(order) },
                enabled = order.state
            ) {
                Text("Вернуть")
            }
        }
    }
}
