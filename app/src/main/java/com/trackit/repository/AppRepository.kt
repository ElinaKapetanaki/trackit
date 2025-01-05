package com.trackit.repository

import com.trackit.database.Expense
import com.trackit.database.Income
import com.trackit.database.User

interface AppRepository {
    // Users
    suspend fun getUser(emailOrUsername: String, password: String): User?
    suspend fun insertUser(user: User): Long
    suspend fun findUserByUsername(emailOrUsername: String): User?
    suspend fun findUserById(userId: Int): User?
    suspend fun updateUserProfile(userId: Int, fullName: String, username: String, passwordHash: String?)

    // Expenses
    suspend fun insertExpense(userId: Int, amount: Double, category: String, description: String, date: String)
    suspend fun getExpensesForUser(userId: Int): List<Expense>

    suspend fun getExpensesForUserBetweenDates(userId: Int, startDate: String, endDate: String): List<Expense>
    suspend fun getCategoryExpensesForUser(userId: Int, category: String): List<Expense>


    // Income
    suspend fun insertIncome(userId: Int, amount: Double, description: String, date: String)
    suspend fun getIncomeForUser(userId: Int): List<Income>

    // To delete test data
    suspend fun deleteUserExpensesByUsername(username: String)
    suspend fun deleteUserByUsername(username: String)
}
