package com.trackit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense): Long

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getExpensesForUser(userId: Int): List<Expense>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    suspend fun getExpensesBetweenDates(
        userId: Int,
        startDate: String,
        endDate: String
    ): List<Expense>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND category = :category")
    suspend fun getExpensesByCategory(userId: Int, category: String): List<Expense>

    @Query("DELETE FROM expenses WHERE userId = :userId")
    suspend fun deleteExpensesByUserId(userId: Int)

}




