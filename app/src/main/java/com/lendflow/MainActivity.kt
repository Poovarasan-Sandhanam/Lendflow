package com.lendflow

import android.os.Bundle
import androidx.activity.ComponentActivity
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
        enableEdgeToEdge()
        setContent {
            LendFlowTheme {
                LendFlowNavDisplay()
            }
        }
    }
}
