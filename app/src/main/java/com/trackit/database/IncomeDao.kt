package com.trackit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IncomeDao {
    @Insert
    suspend fun insertIncome(income: Income): Long

    @Query("SELECT * FROM income WHERE userId = :userId")
    suspend fun getIncomeForUser(userId: Int): List<Income>

    @Query("DELETE FROM income WHERE userId = :userId")
    suspend fun deleteIncomeByUserId(userId: Int)
}
