package com.trackit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Expense::class, Income::class], version = 5, exportSchema = false) // Updated version
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao(): IncomeDao // Added IncomeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Use destructive migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
