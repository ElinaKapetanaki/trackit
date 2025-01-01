package com.trackit.database // Should be the same package as UserDao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "emailOrUsername") val emailOrUsername: String,
    @ColumnInfo(name = "passwordHash") val passwordHash: String
)