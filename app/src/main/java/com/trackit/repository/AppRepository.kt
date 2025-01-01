package com.trackit.repository

import com.trackit.database.Expense
import com.trackit.database.User

interface AppRepository {
    // Users
    suspend fun getUser(emailOrUsername: String, password: String): User? // Επαλήθευση χρήστη
    suspend fun insertUser(user: User): Long                              // Εισαγωγή χρήστη
    suspend fun findUserByUsername(emailOrUsername: String): User?        // Εύρεση χρήστη
    suspend fun findUserById(userId: Int): User?

    // Expenses
    suspend fun insertExpense(userId: Int, amount: Double, category: String, description: String, date: String)
    suspend fun getExpensesForUser(userId: Int): List<Expense>

    // To delete test data
    suspend fun deleteUserExpensesByUsername(username: String)
    suspend fun deleteUserByUsername(username: String)
}
