package com.trackit.repository

import com.trackit.database.AppDatabase
import com.trackit.database.User

class OfflineAppRepository(private val database: AppDatabase) : AppRepository {

    // Διαχείριση Χρηστών
    override suspend fun getUser(emailOrUsername: String, password: String): User? {
        return database.userDao().getUser(emailOrUsername, password)
    }

    override suspend fun insertUser(user: User): Long {
        return database.userDao().insertUser(user)
    }

    override suspend fun findUserByUsername(emailOrUsername: String): User? {
        return database.userDao().findUserByUsername(emailOrUsername)
    }

    // Αν στο μέλλον προσθέσεις αντικείμενα, μπορείς να τα διαχειριστείς εδώ:
    // override fun getAllItemsStream(): Flow<List<Item>> = database.itemDao().getAllItems()
    // override suspend fun insertItem(item: Item) = database.itemDao().insert(item)
}
