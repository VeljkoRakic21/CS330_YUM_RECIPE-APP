package com.example.project_yum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.demo.ui.theme.Project_YUMTheme
import com.example.project_yum.navigation.Nav
import com.example.project_yum.ui.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project_YUMTheme {
                val navController = rememberNavController()
                Nav(navController = navController)
            }
        }
    }
}
