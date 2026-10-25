package com.lendflow.feature.loans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lendflow.R
import com.lendflow.core.data.SampleData
import com.lendflow.core.designsystem.component.IconBadge
import com.lendflow.core.designsystem.component.InkTopBar
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.component.ProgressBar
import com.lendflow.core.designsystem.component.SectionHeader
import com.lendflow.core.designsystem.component.StatusPill
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing
import com.lendflow.core.model.Loan
import com.lendflow.core.model.LoanStatus
import com.lendflow.core.ui.asDayMonth
import com.lendflow.core.ui.asMoney

@Composable
fun LoansScreen(
    onOpenLoan: (String) -> Unit,
    onNewLoan: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (active, closed) = SampleData.loans.partition { it.status != LoanStatus.PaidOff }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = { InkTopBar("Your loans") },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            modifier = Modifier.padding(innerPadding),
        ) {
            item { SectionHeader("Active") }
            items(active, key = { it.id }) { loan -> LoanCard(loan, onClick = { onOpenLoan(loan.id) }) }

            item {
                LendFlowCard(onClick = onNewLoan, containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconBadge(R.drawable.ic_add, containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                        Spacer(Modifier.width(Spacing.md))
                        Column(Modifier.weight(1f)) {
                            Text("Get a new loan", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text(
                                "Borrow £1,000 to £25,000. See your rate in minutes.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }
            }

            if (closed.isNotEmpty()) {
                item { SectionHeader("Paid off") }
                items(closed, key = { it.id }) { loan -> LoanCard(loan, onClick = { onOpenLoan(loan.id) }) }
            }
        }
    }
}

@Composable
internal fun LoanCard(loan: Loan, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val paidOff = loan.status == LoanStatus.PaidOff
    LendFlowCard(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(R.drawable.ic_bank)
            Spacer(Modifier.width(Spacing.md))
            Column(Modifier.weight(1f)) {
                Text(loan.name, style = MaterialTheme.typography.titleMedium)
                Text("${loan.aprPercent}% APR · ${loan.termMonths} months", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (paidOff) {
                StatusPill("Paid off")
            } else {
                StatusPill(
                    "Active",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Spacer(Modifier.height(Spacing.lg))
        if (paidOff) {
            Text(loan.principal.asMoney(), style = MaterialTheme.typography.headlineMedium)
            Text("borrowed and fully repaid", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Text(loan.balance.asMoney(), style = MaterialTheme.typography.headlineMedium)
            Text("left to repay of ${loan.principal.asMoney()}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(Spacing.md))
        ProgressBar(loan.progress)
        Spacer(Modifier.height(Spacing.sm))
        Row {
            Text(
                "${loan.paymentsMade} of ${loan.termMonths} payments",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            loan.nextPaymentDate?.let {
                Text("Next ${loan.monthlyPayment.asMoney()} on ${it.asDayMonth()}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoansScreenPreview() {
    LendFlowTheme {
        LoansScreen(onOpenLoan = {}, onNewLoan = {})
    }
}
