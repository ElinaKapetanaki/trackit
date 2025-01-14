package com.trackit.repository

import com.trackit.database.AppDatabase
import com.trackit.database.Expense
import com.trackit.database.Income
import com.trackit.database.User

class OfflineAppRepository(private val database: AppDatabase) : AppRepository {
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


    override suspend fun insertExpense(userId: Int, amount: Double, category: String, description: String, date: String) {
        database.expenseDao().insertExpense(
            Expense(userId = userId, amount = amount, category = category, description = description, date = date)
        )
    }

    override suspend fun getExpensesForUser(userId: Int): List<Expense> {
        return database.expenseDao().getExpensesForUser(userId)
    }


    override suspend fun getExpensesForUserBetweenDates(userId: Int, startDate: String, endDate: String): List<Expense> {
        return database.expenseDao().getExpensesBetweenDates(userId, startDate, endDate)
    }

    override suspend fun getCategoryExpensesForUser(userId: Int, category: String): List<Expense> {
        return database.expenseDao().getExpensesByCategory(userId, category)
    }


    override suspend fun insertIncome(userId: Int, amount: Double, description: String, date: String) {
        database.incomeDao().insertIncome(
            Income(userId = userId, amount = amount, description = description, date = date)
        )
    }

    override suspend fun getIncomeForUser(userId: Int): List<Income> {
        return database.incomeDao().getIncomeForUser(userId)
    }
}
