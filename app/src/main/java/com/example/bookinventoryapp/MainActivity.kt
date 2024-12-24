package com.example.bookinventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookinventoryapp.ui.screens.FirebaseBookListScreen
import com.example.bookinventoryapp.ui.screens.UserSelectionScreen
import com.example.bookinventoryapp.ui.theme.BookInventoryAppTheme
import com.example.bookinventoryapp.ui.theme.screens.QRCodeGeneratorScreen
import com.example.bookinventoryapp.utils.OrderViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookInventoryAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val orderViewModel = OrderViewModel()
    AppNavigation(navController, orderViewModel)
}

@Composable
fun AppNavigation(navController: NavHostController, orderViewModel: OrderViewModel) {
    NavHost(
        navController = navController,
        startDestination = "book_list"
    ) {
        composable("book_list") {
            FirebaseBookListScreen(navController = navController) { qrData ->
                navController.navigate("qr_generator/$qrData")
            }
        }
        composable("user_selection") {
            UserSelectionScreen(navController = navController, orderViewModel = orderViewModel)
        }
        composable(
            route = "qr_generator?title={title}&author={author}&stock={stock}&quantity={quantity}&price={price}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType },
                navArgument("stock") { type = NavType.StringType },
                navArgument("quantity") { type = NavType.IntType },
                navArgument("price") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val author = backStackEntry.arguments?.getString("author") ?: ""
            val stock = backStackEntry.arguments?.getString("stock") ?: ""
            val quantity = backStackEntry.arguments?.getInt("quantity") ?: 1
            val price = backStackEntry.arguments?.getString("price") ?: ""

            QRCodeGeneratorScreen(
                navController = navController,
                bookTitle = title,
                bookAuthor = author,
                stockCount = stock,
                quantity = quantity,
                price = price
            )
        }
    }
}
