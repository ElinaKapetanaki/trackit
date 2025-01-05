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

    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId 
        AND date >= strftime('%Y-%m-%d', 'now', '-7 days')
    """)
    suspend fun getWeeklyExpensesForUser(userId: Int): List<Expense>

    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId 
        AND date >= strftime('%Y-%m-%d', 'now', '-30 days')
    """)
    suspend fun getMonthlyExpensesForUser(userId: Int): List<Expense>

    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId 
        AND category = :category
    """)
    suspend fun getCategoryExpensesForUser(userId: Int, category: String): List<Expense>

    @Query("DELETE FROM expenses WHERE userId = :userId")
    suspend fun deleteExpensesByUserId(userId: Int)
}
