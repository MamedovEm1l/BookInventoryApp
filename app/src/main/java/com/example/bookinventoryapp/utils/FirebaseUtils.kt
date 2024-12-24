package com.example.bookinventoryapp.utils

import android.util.Log
import com.example.bookinventoryapp.models.Book
import com.google.firebase.firestore.FirebaseFirestore

fun fetchBooksFromFirebase(
    onResult: (List<Book>) -> Unit,
    onError: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("books")
        .get()
        .addOnSuccessListener { result ->
            val books = result.map { doc ->
                doc.toObject(Book::class.java).copy(key = doc.id)
            }
            onResult(books)
            Log.i("My", result.toString())
        }
        .addOnFailureListener { exception ->
            Log.i("My", exception.toString())
            onError(exception)
        }
}
