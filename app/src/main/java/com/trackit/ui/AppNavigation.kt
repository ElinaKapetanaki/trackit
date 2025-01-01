package com.trackit.ui


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


enum class AppScreen {
    LoginScreen,
    SignUpScreen,
    HomeScreen,
    AddChoiceScreen,
    AddExpenseScreen,
    AddIncomeScreen,
    ChartsScreen,
    MoneyConversionScreen, /*change to actual name*/
    EditProfileScreen
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.LoginScreen.name
    ) {
        // Login Screen route
        composable(AppScreen.LoginScreen.name) {
            LoginScreen(
                onSignUpClick = { navController.navigate(AppScreen.SignUpScreen.name) },
                onLoginSuccess = {
                    navController.navigate(AppScreen.HomeScreen.name) {
                        popUpTo(AppScreen.LoginScreen.name) { inclusive = true }
                    }
                }
            )
        }

        // Signup Screen route
        composable(AppScreen.SignUpScreen.name) {
            SignupScreen(
                onLogInClick = {
                    navController.navigate(AppScreen.LoginScreen.name) {
                        popUpTo(AppScreen.LoginScreen.name) { inclusive = true }
                    }
                },
                onSignupComplete = {
                    // After successful signup, navigate to LoginScreen
                    navController.navigate(AppScreen.LoginScreen.name) {
                        popUpTo(AppScreen.SignUpScreen.name) { inclusive = true }
                    }
                }
            )
        }

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
                onHomeClick = { /* Already on Home, no action needed */ },
                onChartsClick = { navController.navigate(AppScreen.ChartsScreen.name) },
                onAddButtonClick = { navController.navigate(AppScreen.AddChoiceScreen.name) },
                onExchangeClick = { navController.navigate(AppScreen.MoneyConversionScreen.name) },
                onEditProfileClick = { navController.navigate(AppScreen.EditProfileScreen.name) }
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

        // ChartsScreen route
        composable(AppScreen.ChartsScreen.name) {
            ChartsScreen(
                onBackClick = { navController.popBackStack() },
                onAddButtonClick = { navController.navigate(AppScreen.AddChoiceScreen.name) },
                onEditProfileClick = { navController.navigate(AppScreen.EditProfileScreen.name) },
                onHomeClick = { navController.navigate(AppScreen.HomeScreen.name) },
                onChartsClick = { /* Already on this screen, no action needed */ },
                onExchangeClick = { navController.navigate(AppScreen.MoneyConversionScreen.name) }
            )
        }

        // EditProfileScreen Route
        composable(AppScreen.EditProfileScreen.name) {
            EditProfileScreen(
                onHomeClick = { navController.navigate(AppScreen.HomeScreen.name) },
                onChartsClick = { navController.navigate(AppScreen.ChartsScreen.name) },
                onAddButtonClick = { navController.navigate(AppScreen.AddChoiceScreen.name) },
                onExchangeClick = { navController.navigate(AppScreen.MoneyConversionScreen.name) },
                onEditProfileClick = { /* Already on Edit Profile, no action */ }
            )
        }
    }
}