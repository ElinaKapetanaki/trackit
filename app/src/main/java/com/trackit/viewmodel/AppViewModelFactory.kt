package com.trackit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trackit.repository.AppRepository

class AppViewModelFactory(private val repository: AppRepository,private val userSessionViewModel: UserSessionViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                LoginViewModel(repository, userSessionViewModel) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                EditProfileViewModel(repository, userSessionViewModel) as T
            }
            modelClass.isAssignableFrom(ExpenseViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                ExpenseViewModel(repository, userSessionViewModel) as T
            }
            modelClass.isAssignableFrom(IncomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                IncomeViewModel(repository, userSessionViewModel) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeViewModel(repository, userSessionViewModel) as T
            }
            // Add other ViewModels here as needed
            // Example:
            // modelClass.isAssignableFrom(AnotherViewModel::class.java) -> {
            //     AnotherViewModel(repository) as T
            // }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}