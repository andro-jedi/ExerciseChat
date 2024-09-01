package com.exercisechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.exercisechat.presentation.navigation.AppNavigation
import com.exercisechat.presentation.theme.ExerciseChatTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExerciseChatTheme {
                AppNavigation()
            }
        }
    }
}

