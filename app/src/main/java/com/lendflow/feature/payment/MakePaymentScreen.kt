package com.lendflow.feature.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.R
import com.lendflow.core.data.SampleData
import com.lendflow.core.designsystem.component.IconBadge
import com.lendflow.core.designsystem.component.InkGradient
import com.lendflow.core.designsystem.component.InkTopBar
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.component.PrimaryButton
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing
import com.lendflow.core.ui.asMoney

private enum class PaymentOption { NextPayment, PayOff, Other }

@Composable
fun MakePaymentScreen(
    loanId: String,
    onBack: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val loan = SampleData.loan(loanId) ?: return
    var option by rememberSaveable { mutableStateOf(PaymentOption.NextPayment) }
    var otherAmount by rememberSaveable { mutableStateOf("") }
    var paid by rememberSaveable { mutableStateOf(false) }

    val amount = when (option) {
        PaymentOption.NextPayment -> loan.monthlyPayment
        PaymentOption.PayOff -> loan.balance
        PaymentOption.Other -> (otherAmount.toDoubleOrNull() ?: 0.0).coerceAtMost(loan.balance)
    }

    if (paid) {
        PaymentSent(amount = amount, loanName = loan.name, onDone = onDone, modifier = modifier)
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = { InkTopBar("Make a payment", onBack = onBack) },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLowest, shadowElevation = 8.dp) {
                PrimaryButton(
                    text = if (amount > 0) "Pay ${amount.asMoney()}" else "Pay",
                    enabled = amount > 0,
                    onClick = { paid = true },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(Spacing.md),
                )
            }
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(Spacing.md),
        ) {
            Text(loan.name, style = MaterialTheme.typography.headlineMedium)
            Text(
                "${loan.balance.asMoney()} left to repay",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            LendFlowCard(contentPadding = 0.dp, modifier = Modifier.selectableGroup()) {
                OptionRow("Next monthly payment", loan.monthlyPayment.asMoney(), option == PaymentOption.NextPayment) {
                    option = PaymentOption.NextPayment
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                OptionRow("Pay off in full", loan.balance.asMoney(), option == PaymentOption.PayOff) {
                    option = PaymentOption.PayOff
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                OptionRow("Another amount", "Overpay with no fees", option == PaymentOption.Other) {
                    option = PaymentOption.Other
                }
                if (option == PaymentOption.Other) {
                    OutlinedTextField(
                        value = otherAmount,
                        onValueChange = { new -> otherAmount = new.filter { it.isDigit() || it == '.' }.take(9) },
                        label = { Text("Amount") },
                        prefix = { Text("£") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = MaterialTheme.shapes.medium,
                        supportingText = { Text("Up to ${loan.balance.asMoney()}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Spacing.md, end = Spacing.md, bottom = Spacing.md),
                    )
                }
            }

            Text("Pay from", style = MaterialTheme.typography.titleMedium)
            LendFlowCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconBadge(R.drawable.ic_card)
                    Spacer(Modifier.width(Spacing.md))
                    Column {
                        Text("Current account ••4821", style = MaterialTheme.typography.titleSmall)
                        Text("Simulated bank account", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionRow(title: String, detail: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .padding(horizontal = Spacing.md, vertical = Spacing.md),
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(detail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        RadioButton(selected = selected, onClick = null)
    }
}

@Composable
private fun PaymentSent(amount: Double, loanName: String, onDone: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(InkGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(Spacing.lg),
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .background(BrandColors.Mint),
        ) {
            Icon(painterResource(R.drawable.ic_check), contentDescription = null, tint = BrandColors.OnMint, modifier = Modifier.size(56.dp))
        }
        Spacer(Modifier.height(Spacing.xl))
        Text("Payment sent", color = BrandColors.OnInk, style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
        Spacer(Modifier.height(Spacing.md))
        Text(
            "${amount.asMoney()} is on its way to your $loanName. It can take up to 2 working days to show.",
            color = BrandColors.OnInkMuted,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.weight(1f))
        PrimaryButton("Done", onClick = onDone, onInk = true)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MakePaymentScreenPreview() {
    LendFlowTheme {
        MakePaymentScreen(loanId = SampleData.loans.first().id, onBack = {}, onDone = {})
    }
}
