package com.trackit.database

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

    @Query("SELECT * FROM User WHERE id = :userId")
    suspend fun findUserById(userId: Int): User?

    @Query("DELETE FROM User WHERE emailOrUsername = :emailOrUsername")
    suspend fun deleteUserByEmail(emailOrUsername: String)

    // Update user's full name
    @Query("UPDATE User SET fullName = :newFullName WHERE id = :userId")
    suspend fun updateFullName(userId: Int, newFullName: String)

    // Update user's username
    @Query("UPDATE User SET emailOrUsername = :newUsername WHERE id = :userId")
    suspend fun updateUsername(userId: Int, newUsername: String)

    // Update user's password
    @Query("UPDATE User SET passwordHash = :newPassword WHERE id = :userId")
    suspend fun updatePassword(userId: Int, newPassword: String)
}
