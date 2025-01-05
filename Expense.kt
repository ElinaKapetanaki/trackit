package com.trackit.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Foreign key linking to the User
    val amount: Double,
    val category: String,
    val description: String,
    val date: String
)
