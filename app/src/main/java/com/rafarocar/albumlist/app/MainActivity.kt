package com.rafarocar.albumlist.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rafarocar.albumlist.app.navigation.AppNavHost
import com.rafarocar.albumlist.ui.theme.AlbumListTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the app
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlbumListTheme {
                AppNavHost()
            }
        }
    }
}