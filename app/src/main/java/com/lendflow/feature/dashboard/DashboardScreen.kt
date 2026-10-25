package com.lendflow.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing

// Placeholder until Phase 2 replaces this with the Room-backed loan dashboard.
private val upcomingMilestones = listOf(
    "Phase 1 · Loan calculator",
    "Phase 2 · Loan dashboard (Retrofit + Room)",
    "Phase 3 · Dependency injection (Hilt)",
    "Phase 4 · Background sync (WorkManager)",
    "Phase 5 · Loan application and KYC",
    "Phase 6 · Secure session and repayments",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onOpenCalculator: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("LendFlow") }) },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                        modifier = Modifier.padding(Spacing.md),
                    ) {
                        Text(
                            text = "Welcome to LendFlow",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.semantics { heading() },
                        )
                        Text(
                            text = "A simulated lending app. No real money, credit checks or personal data.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Button(onClick = onOpenCalculator) {
                            Text("Open loan calculator")
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Coming next",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.semantics { heading() },
                )
            }
            items(upcomingMilestones) { milestone ->
                Text(text = milestone, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardScreenPreview() {
    LendFlowTheme {
        DashboardScreen(onOpenCalculator = {})
    }
}
