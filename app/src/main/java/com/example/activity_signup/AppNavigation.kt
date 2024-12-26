package com.example.activity_signup


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController


enum class AppScreen {
    HomeScreen,
    AddChoiceScreen,
    AddExpenseScreen,
    AddIncomeScreen
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.HomeScreen.name
    ) {

        // HomeScreen route with an optional argument
        composable(
            route = "${AppScreen.HomeScreen.name}?successMessage={successMessage}",
            arguments = listOf(navArgument("successMessage") {
                type = NavType.StringType
                defaultValue = null // Default value if not provided
                nullable = true
            })
        ) { backStackEntry ->
            val successMessage = backStackEntry.arguments?.getString("successMessage")
            HomeScreen(
                successMessage = successMessage,
                onAddButtonClick = { navController.navigate(AppScreen.AddChoiceScreen.name) }
            )
        }

        // AddChoiceScreen Route
        composable(AppScreen.AddChoiceScreen.name) {
            AddChoiceScreen(
                onAddExpenseClick = { navController.navigate(AppScreen.AddExpenseScreen.name) },
                onAddIncomeClick = { navController.navigate(AppScreen.AddIncomeScreen.name) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // AddExpenseScreen Route
        composable(AppScreen.AddExpenseScreen.name) {
            AddExpenseScreen(
                onSaveClick = {
                    navController.navigate("${AppScreen.HomeScreen.name}?successMessage=Expense successfully added!")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // AddIncomeScreen Route
        composable(AppScreen.AddIncomeScreen.name) {
            AddIncomeScreen(
                onSaveClick = {
                    navController.navigate("${AppScreen.HomeScreen.name}?successMessage=Income successfully added!")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}