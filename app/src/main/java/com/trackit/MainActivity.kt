package com.trackit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.trackit.ui.AppNavigation
import com.trackit.ui.theme.SignUpActivityTheme
import com.trackit.viewmodel.AppViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppViewModelProvider.initialize(this)

        setContent {
            SignUpActivityTheme {
                AppNavigation()
            }
        }

    }
}
