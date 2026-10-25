package com.lendflow.feature.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.core.designsystem.component.DetailRow
import com.lendflow.core.designsystem.component.InkTopBar
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.component.PrimaryButton
import com.lendflow.core.designsystem.component.SimulationNote
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing
import com.lendflow.core.model.monthlyRepayment
import com.lendflow.core.model.representativeApr
import com.lendflow.core.ui.asMoney
import com.lendflow.core.ui.asWholeMoney
import kotlin.math.roundToInt

private const val MIN_AMOUNT = 1_000f
private const val MAX_AMOUNT = 25_000f
private const val AMOUNT_STEP = 250f
private val terms = listOf(12, 24, 36, 48, 60)

// State is held in rememberSaveable for now. Phase 1 moves it into a tested ViewModel.
@Composable
fun CalculatorScreen(
    onBack: () -> Unit,
    onApply: (amount: Int, termMonths: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var amount by rememberSaveable { mutableFloatStateOf(7_500f) }
    var term by rememberSaveable { mutableIntStateOf(36) }

    val apr = representativeApr(amount.toDouble())
    val monthly = monthlyRepayment(amount.toDouble(), apr, term)
    val totalRepayable = monthly * term

    Scaffold(
        modifier = modifier,
        topBar = { InkTopBar("Loan calculator", onBack = onBack) },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLowest, shadowElevation = 8.dp) {
                PrimaryButton(
                    "Apply for ${amount.toDouble().asWholeMoney()}",
                    onClick = { onApply(amount.roundToInt(), term) },
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
                .verticalScroll(rememberScrollState()),
        ) {
            // The ink panel continues the top bar, so amount entry reads as the screen's headline.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(BrandColors.Ink)
                    .padding(horizontal = Spacing.lg)
                    .padding(top = Spacing.sm, bottom = Spacing.lg),
            ) {
                Text("How much would you like to borrow?", color = BrandColors.OnInkMuted, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(Spacing.sm))
                Text(
                    amount.toDouble().asWholeMoney(),
                    color = BrandColors.OnInk,
                    style = MaterialTheme.typography.displayMedium,
                )
                Spacer(Modifier.height(Spacing.md))
                Slider(
                    value = amount,
                    onValueChange = { amount = (it / AMOUNT_STEP).roundToInt() * AMOUNT_STEP },
                    valueRange = MIN_AMOUNT..MAX_AMOUNT,
                    colors = SliderDefaults.colors(
                        thumbColor = BrandColors.Mint,
                        activeTrackColor = BrandColors.Mint,
                        inactiveTrackColor = BrandColors.OnInk.copy(alpha = 0.2f),
                    ),
                    modifier = Modifier.semantics { contentDescription = "Loan amount" },
                )
                Row {
                    Text(MIN_AMOUNT.toDouble().asWholeMoney(), color = BrandColors.OnInkMuted, style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f))
                    Text(MAX_AMOUNT.toDouble().asWholeMoney(), color = BrandColors.OnInkMuted, style = MaterialTheme.typography.labelMedium)
                }
            }

            Column(Modifier.padding(horizontal = Spacing.md)) {
                Text("Over how long?", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = Spacing.sm))
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.fillMaxWidth()) {
                    terms.forEach { months ->
                        FilterChip(
                            selected = months == term,
                            onClick = { term = months },
                            shape = CircleShape,
                            label = {
                                Text(
                                    "${months / 12} yr",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandColors.Ink,
                                selectedLabelColor = BrandColors.OnInk,
                            ),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            LendFlowCard(modifier = Modifier.padding(horizontal = Spacing.md)) {
                Text("Your monthly repayment", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                AnimatedContent(
                    targetState = monthly.asMoney(),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "monthly",
                ) { value ->
                    Text(value, style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
                }
                Text("for $term months", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(vertical = Spacing.md))
                DetailRow("Representative APR", "$apr%")
                DetailRow("Total interest", (totalRepayable - amount).asMoney())
                DetailRow("Total repayable", totalRepayable.asMoney())
            }

            SimulationNote(
                "Illustrative figures only. Checking your rate won't affect your credit score.",
                modifier = Modifier.padding(horizontal = Spacing.md),
            )
            Spacer(Modifier.height(Spacing.md))
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CalculatorScreenPreview() {
    LendFlowTheme {
        CalculatorScreen(onBack = {}, onApply = { _, _ -> })
    }
}
