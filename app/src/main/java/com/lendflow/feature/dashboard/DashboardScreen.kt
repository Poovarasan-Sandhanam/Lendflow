package com.lendflow.feature.dashboard

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.R
import com.lendflow.core.data.SampleData
import com.lendflow.core.designsystem.component.Avatar
import com.lendflow.core.designsystem.component.IconBadge
import com.lendflow.core.designsystem.component.InkGradient
import com.lendflow.core.designsystem.component.InkHero
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.component.PrimaryButton
import com.lendflow.core.designsystem.component.ProgressBar
import com.lendflow.core.designsystem.component.ScoreRing
import com.lendflow.core.designsystem.component.SectionHeader
import com.lendflow.core.designsystem.component.StatusBarScrim
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing
import com.lendflow.core.model.Loan
import com.lendflow.core.model.LoanStatus
import com.lendflow.core.ui.asDayMonth
import com.lendflow.core.ui.asMoney
import com.lendflow.feature.activity.TransactionRow
import java.time.temporal.ChronoUnit

// Reads SampleData directly until Phase 2 introduces a ViewModel and repository.
@Composable
fun DashboardScreen(
    onOpenLoan: (String) -> Unit,
    onOpenCalculator: () -> Unit,
    onMakePayment: (String) -> Unit,
    onOpenNotifications: () -> Unit,
    onSeeAllLoans: () -> Unit,
    onSeeAllActivity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val customer = SampleData.customer
    val activeLoans = SampleData.loans.filter { it.status == LoanStatus.Active }
    val nextDue = activeLoans.filter { it.nextPaymentDate != null }.minByOrNull { it.nextPaymentDate!! }
    val unread = SampleData.notices.count { it.unread }

    Box(modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                InkHero {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar("${customer.firstName.first()}${customer.lastName.first()}")
                        Spacer(Modifier.width(Spacing.md))
                        Column(Modifier.weight(1f)) {
                            Text("Good morning,", color = BrandColors.OnInkMuted, style = MaterialTheme.typography.bodyMedium)
                            Text(customer.firstName, color = BrandColors.OnInk, style = MaterialTheme.typography.titleLarge)
                        }
                        IconButton(onClick = onOpenNotifications) {
                            BadgedBox(badge = { if (unread > 0) Badge { Text("$unread") } }) {
                                Icon(painterResource(R.drawable.ic_notifications), contentDescription = "Notifications", tint = BrandColors.OnInk)
                            }
                        }
                    }
                    Spacer(Modifier.height(Spacing.xl))
                    Text("Total left to repay", color = BrandColors.OnInkMuted, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        activeLoans.sumOf { it.balance }.asMoney(),
                        color = BrandColors.OnInk,
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.semantics { heading() },
                    )
                    Text(
                        "Across ${activeLoans.size} active loans",
                        color = BrandColors.Mint,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Spacer(Modifier.height(Spacing.lg))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        QuickAction(R.drawable.ic_card, "Pay") { nextDue?.let { onMakePayment(it.id) } }
                        QuickAction(R.drawable.ic_add, "Borrow", onOpenCalculator)
                        QuickAction(R.drawable.ic_calculate, "Calculator", onOpenCalculator)
                        QuickAction(R.drawable.ic_bank, "Loans", onSeeAllLoans)
                    }
                    Spacer(Modifier.height(Spacing.sm))
                }
            }

            if (nextDue != null) {
                item { NextRepaymentCard(nextDue, onPay = { onMakePayment(nextDue.id) }, modifier = Modifier.padding(horizontal = Spacing.md)) }
            }

            item { SectionHeader("Your loans", actionLabel = "See all", onAction = onSeeAllLoans, modifier = Modifier.padding(start = Spacing.md, end = Spacing.sm)) }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = Spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                ) {
                    items(activeLoans, key = { it.id }) { loan ->
                        LoanTile(loan, onClick = { onOpenLoan(loan.id) })
                    }
                }
            }

            item { CreditScoreCard(Modifier.padding(horizontal = Spacing.md)) }
            item { OfferCard(onCheckRate = onOpenCalculator, modifier = Modifier.padding(horizontal = Spacing.md)) }

            item { SectionHeader("Recent activity", actionLabel = "See all", onAction = onSeeAllActivity, modifier = Modifier.padding(start = Spacing.md, end = Spacing.sm)) }
            item {
                LendFlowCard(contentPadding = 0.dp, modifier = Modifier.padding(horizontal = Spacing.md)) {
                    val recent = SampleData.transactions.take(4)
                    recent.forEachIndexed { index, transaction ->
                        TransactionRow(transaction)
                        if (index < recent.lastIndex) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(start = 76.dp))
                    }
                }
            }
        }
        StatusBarScrim()
    }
}

@Composable
private fun QuickAction(@DrawableRes icon: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(Spacing.sm),
    ) {
        IconBadge(icon, containerColor = BrandColors.OnInk.copy(alpha = 0.12f), contentColor = BrandColors.Mint, size = 56.dp)
        Spacer(Modifier.height(Spacing.sm))
        Text(label, color = BrandColors.OnInk, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
    }
}

@Composable
private fun NextRepaymentCard(loan: Loan, onPay: () -> Unit, modifier: Modifier = Modifier) {
    val date = loan.nextPaymentDate ?: return
    val days = ChronoUnit.DAYS.between(SampleData.today, date)
    LendFlowCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(R.drawable.ic_card, containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(Modifier.width(Spacing.md))
            Column(Modifier.weight(1f)) {
                Text("Next repayment", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(loan.monthlyPayment.asMoney(), style = MaterialTheme.typography.headlineSmall)
                Text("${loan.name} · ${date.asDayMonth()} · in $days days", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(Spacing.md))
        PrimaryButton("Pay now", onClick = onPay)
    }
}

@Composable
private fun LoanTile(loan: Loan, onClick: () -> Unit) {
    LendFlowCard(onClick = onClick, modifier = Modifier.width(240.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(R.drawable.ic_bank, size = 36.dp)
            Spacer(Modifier.width(Spacing.sm))
            Text(loan.name, style = MaterialTheme.typography.titleSmall, maxLines = 1)
        }
        Spacer(Modifier.height(Spacing.md))
        Text(loan.balance.asMoney(), style = MaterialTheme.typography.headlineSmall)
        Text("left of ${loan.principal.asMoney()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(Spacing.md))
        ProgressBar(loan.progress)
        Spacer(Modifier.height(Spacing.sm))
        Text("${(loan.progress * 100).toInt()}% repaid", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun CreditScoreCard(modifier: Modifier = Modifier) {
    val customer = SampleData.customer
    LendFlowCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Your credit score", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(Spacing.xs))
                Text("Good", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.height(Spacing.xs))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_trending_up), contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(Spacing.xs))
                    Text("Up 12 points this month", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            ScoreRing(customer.creditScore, customer.creditScoreMax)
        }
    }
}

@Composable
private fun OfferCard(onCheckRate: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(InkGradient)
            .padding(Spacing.lg),
    ) {
        Box {
            Box(
                Modifier
                    .size(120.dp)
                    .offset(x = 220.dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(BrandColors.Mint.copy(alpha = 0.15f)),
            )
            Column {
                Text("Pre-approved offer", color = BrandColors.Mint, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(Spacing.sm))
                Text("You could borrow up to £15,000", color = BrandColors.OnInk, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(Spacing.sm))
                Text(
                    "Check your personalised rate. It won't affect your credit score.",
                    color = BrandColors.OnInkMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Spacer(Modifier.height(Spacing.md))
        PrimaryButton("Check my rate", onClick = onCheckRate, onInk = true)
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardScreenPreview() {
    LendFlowTheme {
        DashboardScreen({}, {}, {}, {}, {}, {})
    }
}
