package com.trackit.viewmodel

import android.content.Context
import com.trackit.database.AppDatabase
import com.trackit.repository.AppRepository
import com.trackit.repository.OfflineAppRepository

object AppViewModelProvider {
    // Lazy initialization of the repository and userSessionViewModel
    private lateinit var repository: AppRepository
    private lateinit var userSessionViewModel: UserSessionViewModel

    // Initialize the repository and userSessionViewModel (call this from MainActivity)
    fun initialize(context: Context) {
        val database = AppDatabase.getDatabase(context)
        repository = OfflineAppRepository(database)
        userSessionViewModel = UserSessionViewModel() // Instantiate UserSessionViewModel
    }

    // ViewModel Factory
    val Factory: AppViewModelFactory
        get() {
            if (!::repository.isInitialized || !::userSessionViewModel.isInitialized) {
                throw IllegalStateException("AppViewModelProvider is not initialized. Call initialize() first.")
            }
            return AppViewModelFactory(repository, userSessionViewModel) // Περνάμε το userSessionViewModel
        }
}
