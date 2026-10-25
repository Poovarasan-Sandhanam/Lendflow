package com.lendflow.feature.loans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.R
import com.lendflow.core.data.SampleData
import com.lendflow.core.designsystem.component.DetailRow
import com.lendflow.core.designsystem.component.InkHero
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.component.PrimaryButton
import com.lendflow.core.designsystem.component.ProgressBar
import com.lendflow.core.designsystem.component.SectionHeader
import com.lendflow.core.designsystem.component.SecondaryButton
import com.lendflow.core.designsystem.component.StatusBarScrim
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing
import com.lendflow.core.model.LoanStatus
import com.lendflow.core.ui.asMoney
import com.lendflow.core.ui.asShortDate
import com.lendflow.feature.activity.TransactionRow

@Composable
fun LoanDetailScreen(
    loanId: String,
    onBack: () -> Unit,
    onMakePayment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val loan = SampleData.loan(loanId) ?: return
    val history = SampleData.transactionsFor(loanId)
    val active = loan.status == LoanStatus.Active

    Box(modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = Spacing.lg + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                InkHero {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
                            Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = "Back", tint = BrandColors.OnInk)
                        }
                        Text(loan.name, color = BrandColors.OnInk, style = MaterialTheme.typography.titleLarge)
                    }
                    Spacer(Modifier.height(Spacing.lg))
                    Text(
                        if (active) "Remaining balance" else "Fully repaid",
                        color = BrandColors.OnInkMuted,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        (if (active) loan.balance else loan.principal).asMoney(),
                        color = BrandColors.OnInk,
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.semantics { heading() },
                    )
                    Spacer(Modifier.height(Spacing.lg))
                    ProgressBar(loan.progress, color = BrandColors.Mint, trackColor = BrandColors.OnInk.copy(alpha = 0.15f))
                    Spacer(Modifier.height(Spacing.sm))
                    Row {
                        Text(
                            "${(loan.progress * 100).toInt()}% repaid",
                            color = BrandColors.Mint,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f),
                        )
                        Text("${loan.principal.asMoney()} borrowed", color = BrandColors.OnInkMuted, style = MaterialTheme.typography.labelLarge)
                    }
                    if (active) {
                        Spacer(Modifier.height(Spacing.lg))
                        PrimaryButton("Make a payment", onClick = onMakePayment, onInk = true)
                        Spacer(Modifier.height(Spacing.sm))
                        SecondaryButton("Overpay or settle early", onClick = onMakePayment, onInk = true)
                    }
                    Spacer(Modifier.height(Spacing.sm))
                }
            }

            item {
                LendFlowCard(modifier = Modifier.padding(horizontal = Spacing.md)) {
                    Text("Loan details", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = Spacing.sm))
                    DetailRow("Monthly payment", loan.monthlyPayment.asMoney())
                    DetailRow("Representative APR", "${loan.aprPercent}%")
                    DetailRow("Term", "${loan.termMonths} months")
                    DetailRow("Payments made", "${loan.paymentsMade} of ${loan.termMonths}")
                    loan.nextPaymentDate?.let { DetailRow("Next payment", it.asShortDate()) }
                    DetailRow("Purpose", loan.purpose)
                    DetailRow("Loan reference", loan.id.uppercase())
                }
            }

            if (history.isNotEmpty()) {
                item { SectionHeader("Payment history", modifier = Modifier.padding(horizontal = Spacing.md)) }
                item {
                    LendFlowCard(contentPadding = 0.dp, modifier = Modifier.padding(horizontal = Spacing.md)) {
                        history.forEachIndexed { index, transaction ->
                            TransactionRow(transaction)
                            if (index < history.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(start = 76.dp))
                            }
                        }
                    }
                }
            }
        }
        StatusBarScrim()
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoanDetailScreenPreview() {
    LendFlowTheme {
        LoanDetailScreen(loanId = SampleData.loans.first().id, onBack = {}, onMakePayment = {})
    }
}
