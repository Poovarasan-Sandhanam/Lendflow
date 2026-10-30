package com.lendflow

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.navigation.LendFlowNavDisplay

/**
 * The single Activity. It only hosts Compose; screens, navigation and state live below it.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Every screen puts dark ink under the status bar, so its icons are always light.
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            LendFlowTheme {
                LendFlowNavDisplay()
            }
        }
    }
}
