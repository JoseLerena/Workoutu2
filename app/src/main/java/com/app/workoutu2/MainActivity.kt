package com.app.workoutu2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.workoutu2.game.GameScreen
import com.app.workoutu2.ui.theme.Workoutu2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Workoutu2Theme {
                GameScreen()
            }
        }
    }
}
