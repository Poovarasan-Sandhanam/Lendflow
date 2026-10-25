package com.lendflow.feature.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.R
import com.lendflow.core.data.SampleData
import com.lendflow.core.designsystem.component.InkTopBar
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.component.ListRow
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing
import com.lendflow.core.model.Transaction
import com.lendflow.core.model.TransactionKind
import com.lendflow.core.ui.asDayMonth
import com.lendflow.core.ui.asMoney
import com.lendflow.core.ui.asMonthYear

private enum class ActivityFilter(val label: String, val kinds: Set<TransactionKind>) {
    All("All", TransactionKind.entries.toSet()),
    Repayments("Repayments", setOf(TransactionKind.Repayment)),
    Overpayments("Overpayments", setOf(TransactionKind.Overpayment)),
    Payouts("Payouts", setOf(TransactionKind.Disbursement)),
}

@Composable
fun ActivityScreen(modifier: Modifier = Modifier) {
    var filter by rememberSaveable { mutableStateOf(ActivityFilter.All) }
    val byMonth = SampleData.transactions
        .filter { it.kind in filter.kinds }
        .groupBy { it.date.withDayOfMonth(1) }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = { InkTopBar("Activity") },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(bottom = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                LazyRow(
                    contentPadding = PaddingValues(Spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                ) {
                    items(ActivityFilter.entries) { option ->
                        FilterChip(
                            selected = option == filter,
                            onClick = { filter = option },
                            label = { Text(option.label) },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandColors.Ink,
                                selectedLabelColor = BrandColors.OnInk,
                            ),
                        )
                    }
                }
            }
            byMonth.forEach { (month, transactions) ->
                item(key = month.toString()) {
                    Text(
                        month.asMonthYear(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(horizontal = Spacing.lg, vertical = Spacing.sm)
                            .semantics { heading() },
                    )
                }
                item(key = "$month-list") {
                    LendFlowCard(contentPadding = 0.dp, modifier = Modifier.padding(horizontal = Spacing.md)) {
                        transactions.forEachIndexed { index, transaction ->
                            TransactionRow(transaction)
                            if (index < transactions.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(start = 76.dp))
                            }
                        }
                    }
                }
            }
            if (byMonth.isEmpty()) {
                item {
                    Text(
                        "Nothing here yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(Spacing.lg),
                    )
                }
            }
        }
    }
}

/** One transaction as a list row: money in is green with a "+", money out is plain. */
@Composable
fun TransactionRow(transaction: Transaction, modifier: Modifier = Modifier) {
    val incoming = transaction.amount > 0
    val icon = if (incoming) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
    val amount = (if (incoming) "+" else "") + kotlin.math.abs(transaction.amount).asMoney()
    ListRow(
        title = transaction.title,
        subtitle = transaction.date.asDayMonth(),
        icon = icon,
        trailingText = amount,
        trailingColor = if (incoming) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ActivityScreenPreview() {
    LendFlowTheme {
        ActivityScreen()
    }
}
