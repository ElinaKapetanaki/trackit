package com.example.activity_signup.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM User WHERE emailOrUsername = :emailOrUsername AND passwordHash = :password")
    suspend fun getUser(emailOrUsername: String, password: String): User?

    @Query("SELECT * FROM User WHERE emailOrUsername = :emailOrUsername")
    suspend fun findUserByUsername(emailOrUsername: String): User?
}