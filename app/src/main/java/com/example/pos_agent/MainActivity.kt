package com.example.pos_agent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.pos_agent.core.ui.theme.POSAGENTTheme
import com.example.pos_agent.ui.main.MainViewModel
import com.example.pos_agent.ui.navigation.AppRoot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            POSAGENTTheme {
                AppRoot(mainViewModel = mainViewModel)
            }
        }
    }
}
