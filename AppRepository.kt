package com.trackit.repository

import com.trackit.database.Expense
import com.trackit.database.Income
import com.trackit.database.User

interface AppRepository {
    // Users
    suspend fun getUser(emailOrUsername: String, password: String): User? // Επαλήθευση χρήστη
    suspend fun insertUser(user: User): Long                              // Εισαγωγή χρήστη
    suspend fun findUserByUsername(emailOrUsername: String): User?        // Εύρεση χρήστη
    suspend fun findUserById(userId: Int): User?
    suspend fun updateUserProfile(userId: Int, fullName: String, username: String, passwordHash: String?)
    suspend fun updateUserPhoto(userId: Int, photoUri: String)            // Νέα μέθοδος για την εικόνα

    // Expenses
    suspend fun insertExpense(userId: Int, amount: Double, category: String, description: String, date: String)
    suspend fun getExpensesForUser(userId: Int): List<Expense>

    // Income
    suspend fun insertIncome(userId: Int, amount: Double, description: String, date: String) // Εισαγωγή εισοδήματος
    suspend fun getIncomeForUser(userId: Int): List<Income> // Ανάκτηση εισοδημάτων για χρήστη

    // To delete test data
    suspend fun deleteUserExpensesByUsername(username: String)
    suspend fun deleteUserByUsername(username: String)
}
