package com.trackit.repository

import com.trackit.database.AppDatabase
import com.trackit.database.Expense
import com.trackit.database.Income
import com.trackit.database.User

class OfflineAppRepository(private val database: AppDatabase) : AppRepository {

    // Διαχείριση Χρηστών
    override suspend fun getUser(emailOrUsername: String, password: String): User? {
        return database.userDao().getUser(emailOrUsername, password)
    }

    override suspend fun insertUser(user: User): Long {
        return database.userDao().insertUser(user)
    }

    override suspend fun findUserByUsername(emailOrUsername: String): User? {
        return database.userDao().findUserByUsername(emailOrUsername)
    }

    override suspend fun findUserById(userId: Int): User? {
        return database.userDao().findUserById(userId)
    }

    override suspend fun updateUserProfile(userId: Int, fullName: String, username: String, passwordHash: String?) {
        val user = database.userDao().findUserById(userId)
            ?: throw IllegalArgumentException("User with ID $userId does not exist.")

        val updatedUser = user.copy(
            fullName = fullName,
            emailOrUsername = username,
            passwordHash = passwordHash ?: user.passwordHash
        )

        database.userDao().insertUser(updatedUser)
    }

    override suspend fun updateUserPhoto(userId: Int, photoUri: String) {
        database.userDao().updateProfileImage(userId, photoUri)
    }

    // Expenses
    override suspend fun insertExpense(userId: Int, amount: Double, category: String, description: String, date: String) {
        database.expenseDao().insertExpense(
            Expense(
                userId = userId,
                amount = amount,
                category = category,
                description = description,
                date = date
            )
        )
    }

    override suspend fun getExpensesForUser(userId: Int): List<Expense> {
        return database.expenseDao().getExpensesForUser(userId)
    }

    // Income
    override suspend fun insertIncome(userId: Int, amount: Double, description: String, date: String) {
        database.incomeDao().insertIncome(
            Income(
                userId = userId,
                amount = amount,
                description = description,
                date = date
            )
        )
    }

    override suspend fun getIncomeForUser(userId: Int): List<Income> {
        return database.incomeDao().getIncomeForUser(userId)
    }

    // Διαγραφή δεδομένων χρήστη (για test)
    override suspend fun deleteUserExpensesByUsername(username: String) {
        val user = database.userDao().findUserByUsername(username)
            ?: throw IllegalArgumentException("User with username $username does not exist.")

        try {
            database.expenseDao().deleteExpensesByUserId(user.id) // Διαγραφή εξόδων χρήστη
        } catch (e: Exception) {
            throw Exception("Failed to delete expenses for user $username: ${e.message}")
        }
    }

    override suspend fun deleteUserByUsername(username: String) {
        try {
            database.userDao().deleteUserByEmail(username) // Διαγραφή χρήστη
        } catch (e: Exception) {
            throw Exception("Failed to delete user $username: ${e.message}")
        }
    }
}
