package com.trackit.repository

import com.trackit.database.User

interface AppRepository {
    // Χρήστες
    suspend fun getUser(emailOrUsername: String, password: String): User? // Επαλήθευση χρήστη
    suspend fun insertUser(user: User): Long                              // Εισαγωγή χρήστη
    suspend fun findUserByUsername(emailOrUsername: String): User?        // Εύρεση χρήστη

    // Μπορείς να προσθέσεις και άλλες λειτουργίες, π.χ. για άλλα αντικείμενα:
    // fun getAllItemsStream(): Flow<List<Item>>
    // suspend fun insertItem(item: Item)
}
