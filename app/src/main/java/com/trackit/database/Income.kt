package com.trackit.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Foreign key linking to the User
    val amount: Double,
    val description: String,
    val date: String
)
