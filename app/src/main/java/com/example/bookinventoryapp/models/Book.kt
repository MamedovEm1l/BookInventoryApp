package com.example.bookinventoryapp.models

data class Book(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val author: String = "Emil",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val publicationDate: String = "",
    val isStock: Boolean = true,
    val quantity: Int = 1,
    val publisher: String = "Academy"
)