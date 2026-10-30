package com.lendflow.navigation

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.lendflow.R
import com.lendflow.feature.account.AccountScreen
import com.lendflow.feature.activity.ActivityScreen
import com.lendflow.feature.dashboard.DashboardScreen
import com.lendflow.feature.loans.LoansScreen

enum class TopLevelTab(val label: String, @param:DrawableRes val icon: Int) {
    Home("Home", R.drawable.ic_home),
    Loans("Loans", R.drawable.ic_bank),
    Activity("Activity", R.drawable.ic_list),
    Account("Account", R.drawable.ic_person),
}

/**
 * Signed-in shell. Tabs swap in place rather than pushing onto the back stack: switching tabs is
 * not "forward" navigation, and Back from any tab other than Home returns to Home first.
 * Detail screens are pushed on the app back stack and cover the bottom bar.
 */
@Composable
fun MainShell(
    onOpenLoan: (String) -> Unit,
    onOpenCalculator: () -> Unit,
    onMakePayment: (String) -> Unit,
    onOpenNotifications: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var tab by rememberSaveable { mutableStateOf(TopLevelTab.Home) }

    BackHandler(enabled = tab != TopLevelTab.Home) { tab = TopLevelTab.Home }

    Scaffold(
        modifier = modifier,
        // Each tab handles the status bar itself (ink hero or ink top bar).
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest) {
                TopLevelTab.entries.forEach { item ->
                    NavigationBarItem(
                        selected = item == tab,
                        onClick = { tab = item },
                        icon = { Icon(painterResource(item.icon), contentDescription = null) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        AnimatedContent(
            targetState = tab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "tab",
            modifier = Modifier.padding(innerPadding),
        ) { current ->
            Box(Modifier.fillMaxSize()) {
                when (current) {
                    TopLevelTab.Home -> DashboardScreen(
                        onOpenLoan = onOpenLoan,
                        onOpenCalculator = onOpenCalculator,
                        onMakePayment = onMakePayment,
                        onOpenNotifications = onOpenNotifications,
                        onSeeAllLoans = { tab = TopLevelTab.Loans },
                        onSeeAllActivity = { tab = TopLevelTab.Activity },
                    )
                    TopLevelTab.Loans -> LoansScreen(onOpenLoan = onOpenLoan, onNewLoan = onOpenCalculator)
                    TopLevelTab.Activity -> ActivityScreen()
                    TopLevelTab.Account -> AccountScreen(onSignOut = onSignOut)
                }
            }
        }
    }
}
