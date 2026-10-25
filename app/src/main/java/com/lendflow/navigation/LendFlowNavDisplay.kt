package com.lendflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lendflow.feature.calculator.CalculatorScreen
import com.lendflow.feature.dashboard.DashboardScreen

/**
 * App navigation graph. The back stack is a plain observable list of [androidx.navigation3.runtime.NavKey]s:
 * navigating forward is `add`, going back is `removeLastOrNull`.
 *
 * Screens receive navigation as lambdas and never see the back stack, which keeps them
 * reusable and previewable.
 */
@Composable
fun LendFlowNavDisplay(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(DashboardRoute)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<DashboardRoute> {
                DashboardScreen(onOpenCalculator = { backStack.add(CalculatorRoute) })
            }
            entry<CalculatorRoute> {
                CalculatorScreen(onBack = { backStack.removeLastOrNull() })
            }
        },
    )
}
