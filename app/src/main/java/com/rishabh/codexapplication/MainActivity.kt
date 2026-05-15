package com.rishabh.codexapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rishabh.codexapplication.navigation.TodoNavHost
import com.rishabh.codexapplication.ui.theme.CodexApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodexApplicationTheme {
                TodoNavHost()
            }
        }
    }
}
