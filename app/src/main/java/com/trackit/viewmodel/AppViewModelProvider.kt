package com.trackit.viewmodel

import android.content.Context
import com.trackit.database.AppDatabase
import com.trackit.repository.AppRepository
import com.trackit.repository.OfflineAppRepository

object AppViewModelProvider {
    // Lazy initialization of the repository
    private lateinit var repository: AppRepository

    // Initialize the repository (call this from MainActivity)
    fun initialize(context: Context) {
        val database = AppDatabase.getDatabase(context)
        repository = OfflineAppRepository(database)
    }

    // ViewModel Factory
    val Factory: AppViewModelFactory
        get() = AppViewModelFactory(repository)
}
