package com.example.bookinventoryapp.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookinventoryapp.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _users = MutableStateFlow<List<String>>(emptyList()) // Список пользователей
    val users: StateFlow<List<String>> = _users

    // Загрузка всех пользователей
    fun fetchAllUsersSafe() {
        viewModelScope.launch {
            fetchAllUsers()
        }
    }

    private suspend fun fetchAllUsers() {
        try {
            val snapshot = firestore.collection("users").get().await()
            val userList = snapshot.documents.map { it.id } // Сохраняем список ID пользователей
            _users.value = userList
        } catch (e: Exception) {
            Log.e("OrderViewModel", "Error fetching users: ${e.localizedMessage}")
        }
    }

    fun fetchOrdersSafe(userId: String) {
        viewModelScope.launch {
            fetchOrders(userId)
        }
    }

    private suspend fun fetchOrders(userId: String) {
        if (firestore.collection("users").document(userId).get().await().exists()) {
            try {
                val snapshot = firestore.collection("users")
                    .document(userId)
                    .collection("orders")
                    .get()
                    .await()

                val ordersList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Order::class.java)
                }.filter { it.state }

                _orders.value = ordersList
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error fetching orders: ${e.localizedMessage}")
            }
        } else {
            Log.e("OrderViewModel", "Пользователь не авторизован.")
        }
    }


    fun processReturnSafe(userId: String, order: Order) {
        viewModelScope.launch {
            processReturn(userId, order)
        }
    }

    private suspend fun processReturn(userId: String, order: Order) {
        val orderRef = firestore.collection("users").document(userId)
            .collection("orders").document(order.id)

        if (!order.state) {
            Log.e("OrderViewModel", "Order already returned.")
            return
        }

        try {
            // Обновление состояния заказа
            orderRef.update("state", false).await()

            // Увеличение количества книг в коллекции books
            order.items.forEach { item ->
                val bookRef = firestore.collection("books").document(item.book.key)
                bookRef.update("quantity", FieldValue.increment(item.quantity.toLong())).await()
            }

            // Удаление заказа из текущего состояния
            _orders.value = _orders.value.filter { it.id != order.id }

            Log.i("OrderViewModel", "Order successfully returned.")
        } catch (e: Exception) {
            Log.e("OrderViewModel", "Error processing return: ${e.localizedMessage}")
        }
    }

}
