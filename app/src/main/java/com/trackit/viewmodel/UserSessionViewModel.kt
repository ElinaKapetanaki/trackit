package com.trackit.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserSessionViewModel : ViewModel() {
    // User ID state
    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId

    // Set user ID after login
    fun setUserId(id: Int) {
        _userId.value = id
    }
}
