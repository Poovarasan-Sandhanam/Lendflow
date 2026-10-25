package com.lendflow.feature.apply

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.lendflow.core.designsystem.component.DetailRow
import com.lendflow.core.designsystem.component.InkGradient
import com.lendflow.core.designsystem.component.InkTopBar
import com.lendflow.core.designsystem.component.LendFlowCard
import com.lendflow.core.designsystem.component.PrimaryButton
import com.lendflow.core.designsystem.component.ProgressBar
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing
import com.lendflow.core.model.monthlyRepayment
import com.lendflow.core.model.representativeApr
import com.lendflow.core.ui.asMoney
import com.lendflow.core.ui.asWholeMoney
import kotlinx.coroutines.delay

private val purposes = listOf("Car", "Home improvements", "Debt consolidation", "Wedding", "Holiday", "Something else")
private val employmentOptions = listOf("Full-time", "Part-time", "Self-employed", "Retired")
private const val FORM_STEPS = 3
private const val DECISION_STEP = FORM_STEPS

/**
 * Multi-step application. Steps are local state rather than separate routes: they share one
 * form, and Close should abandon the whole application in one go. Phase 5 turns this into an
 * explicit state machine in a ViewModel.
 */
@Composable
fun ApplyScreen(
    amount: Int,
    termMonths: Int,
    onClose: () -> Unit,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    var purpose by rememberSaveable { mutableStateOf<String?>(null) }
    var employment by rememberSaveable { mutableStateOf<String?>(null) }
    var income by rememberSaveable { mutableStateOf("") }
    var housing by rememberSaveable { mutableStateOf("") }
    var consent by rememberSaveable { mutableStateOf(false) }

    val apr = representativeApr(amount.toDouble())
    val monthly = monthlyRepayment(amount.toDouble(), apr, termMonths)

    BackHandler(enabled = step in 1 until DECISION_STEP) { step-- }

    if (step == DECISION_STEP) {
        DecisionContent(amount = amount, monthly = monthly, termMonths = termMonths, onDone = onFinished, modifier = modifier)
        return
    }

    val canContinue = when (step) {
        0 -> purpose != null
        1 -> employment != null && (income.toIntOrNull() ?: 0) > 0 && housing.toIntOrNull() != null
        else -> consent
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column(Modifier.background(BrandColors.Ink)) {
                InkTopBar(
                    title = "Step ${step + 1} of $FORM_STEPS",
                    onBack = onClose,
                    navigationIcon = R.drawable.ic_close,
                    navigationLabel = "Close",
                )
                ProgressBar(
                    progress = (step + 1f) / FORM_STEPS,
                    color = BrandColors.Mint,
                    trackColor = BrandColors.OnInk.copy(alpha = 0.15f),
                    height = 6.dp,
                    modifier = Modifier.padding(start = Spacing.md, end = Spacing.md, bottom = Spacing.md),
                )
            }
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surfaceContainerLowest, shadowElevation = 8.dp) {
                PrimaryButton(
                    text = if (step == FORM_STEPS - 1) "Submit application" else "Continue",
                    enabled = canContinue,
                    onClick = { step++ },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(Spacing.md),
                )
            }
        },
    ) { innerPadding ->
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                val forward = targetState > initialState
                slideInHorizontally { if (forward) it else -it } togetherWith slideOutHorizontally { if (forward) -it else it }
            },
            label = "step",
            modifier = Modifier.padding(innerPadding),
        ) { current ->
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.md),
            ) {
                when (current) {
                    0 -> PurposeStep(selected = purpose, onSelect = { purpose = it })
                    1 -> AboutYouStep(
                        employment = employment,
                        onEmployment = { employment = it },
                        income = income,
                        onIncome = { income = it.filter(Char::isDigit).take(7) },
                        housing = housing,
                        onHousing = { housing = it.filter(Char::isDigit).take(6) },
                    )
                    else -> ReviewStep(
                        amount = amount,
                        termMonths = termMonths,
                        apr = apr,
                        monthly = monthly,
                        purpose = purpose.orEmpty(),
                        employment = employment.orEmpty(),
                        income = income,
                        consent = consent,
                        onConsent = { consent = it },
                    )
                }
            }
        }
    }
}

@Composable
private fun StepTitle(title: String, subtitle: String) {
    Column {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(Spacing.xs))
        Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun PurposeStep(selected: String?, onSelect: (String) -> Unit) {
    StepTitle("What's the loan for?", "This helps us find the right rate for you.")
    LendFlowCard(contentPadding = 0.dp) {
        purposes.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(selected = option == selected, onClick = { onSelect(option) }, role = Role.RadioButton)
                    .padding(horizontal = Spacing.md, vertical = Spacing.sm + Spacing.xs),
            ) {
                Text(option, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                RadioButton(selected = option == selected, onClick = null)
            }
        }
    }
}

@Composable
private fun AboutYouStep(
    employment: String?,
    onEmployment: (String) -> Unit,
    income: String,
    onIncome: (String) -> Unit,
    housing: String,
    onHousing: (String) -> Unit,
) {
    StepTitle("About you", "Use made-up numbers. This is a simulation.")
    Text("Employment status", style = MaterialTheme.typography.titleMedium)
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        employmentOptions.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                pair.forEach { option ->
                    FilterChip(
                        selected = option == employment,
                        onClick = { onEmployment(option) },
                        shape = CircleShape,
                        label = { Text(option, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandColors.Ink,
                            selectedLabelColor = BrandColors.OnInk,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                    )
                }
            }
        }
    }
    OutlinedTextField(
        value = income,
        onValueChange = onIncome,
        label = { Text("Annual income before tax") },
        prefix = { Text("£") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = housing,
        onValueChange = onHousing,
        label = { Text("Monthly rent or mortgage") },
        prefix = { Text("£") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ReviewStep(
    amount: Int,
    termMonths: Int,
    apr: Double,
    monthly: Double,
    purpose: String,
    employment: String,
    income: String,
    consent: Boolean,
    onConsent: (Boolean) -> Unit,
) {
    StepTitle("Check your details", "Make sure everything looks right before you submit.")
    LendFlowCard {
        Text("Your loan", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = Spacing.sm))
        DetailRow("Amount", amount.toDouble().asWholeMoney())
        DetailRow("Term", "$termMonths months")
        DetailRow("Monthly repayment", monthly.asMoney())
        DetailRow("Representative APR", "$apr%")
        DetailRow("Total repayable", (monthly * termMonths).asMoney())
    }
    LendFlowCard {
        Text("About you", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = Spacing.sm))
        DetailRow("Purpose", purpose)
        DetailRow("Employment", employment)
        DetailRow("Annual income", (income.toDoubleOrNull() ?: 0.0).asWholeMoney())
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .toggleable(value = consent, onValueChange = onConsent, role = Role.Checkbox)
            .padding(vertical = Spacing.sm),
    ) {
        Checkbox(checked = consent, onCheckedChange = null)
        Spacer(Modifier.width(Spacing.sm))
        Text(
            "I understand this is a simulated application and no real credit check or loan will happen.",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun DecisionContent(
    amount: Int,
    monthly: Double,
    termMonths: Int,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // A short pause makes the simulated decision feel considered rather than instant.
    var deciding by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (deciding) {
            delay(1_600)
            deciding = false
        }
    }
    BackHandler(enabled = true) { if (!deciding) onDone() }

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
        if (deciding) {
            CircularProgressIndicator(color = BrandColors.Mint, strokeWidth = 5.dp, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(Spacing.lg))
            Text("Checking your application…", color = BrandColors.OnInk, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Spacer(Modifier.weight(1f))
        } else {
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
            Text("You're approved!", color = BrandColors.OnInk, style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
            Spacer(Modifier.height(Spacing.md))
            Text(
                "${amount.toDouble().asWholeMoney()} over $termMonths months at ${monthly.asMoney()} a month. " +
                    "In the real world the money would land in your account within hours.",
                color = BrandColors.OnInkMuted,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.weight(1f))
            PrimaryButton("Back to home", onClick = onDone, onInk = true)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ApplyScreenPreview() {
    LendFlowTheme {
        ApplyScreen(amount = 7_500, termMonths = 36, onClose = {}, onFinished = {})
    }
}
