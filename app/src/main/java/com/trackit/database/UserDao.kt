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

}