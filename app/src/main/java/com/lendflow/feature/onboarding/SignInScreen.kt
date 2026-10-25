package com.lendflow.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lendflow.R
import com.lendflow.core.data.SampleData
import com.lendflow.core.designsystem.component.InkGradient
import com.lendflow.core.designsystem.theme.BrandColors
import com.lendflow.core.designsystem.theme.LendFlowTheme
import com.lendflow.core.designsystem.theme.Spacing

private const val PASSCODE_LENGTH = 6

/**
 * Passcode entry on a custom keypad, like most banking apps (no system keyboard, no autofill).
 * Simulation: any six digits, or the fingerprint key, signs in. Phase 6 adds real biometrics.
 */
@Composable
fun SignInScreen(
    onBack: () -> Unit,
    onSignedIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var passcode by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(passcode) {
        if (passcode.length == PASSCODE_LENGTH) onSignedIn()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(InkGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = Spacing.lg),
    ) {
        Box(Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = "Back", tint = BrandColors.OnInk)
            }
            Wordmark(Modifier.align(Alignment.Center))
        }

        Spacer(Modifier.weight(1f))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(BrandColors.InkGlow),
        ) {
            Icon(painterResource(R.drawable.ic_lock), contentDescription = null, tint = BrandColors.Mint)
        }
        Spacer(Modifier.height(Spacing.lg))
        Text(
            "Welcome back, ${SampleData.customer.firstName}",
            color = BrandColors.OnInk,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Spacing.sm))
        Text("Enter your 6-digit passcode", color = BrandColors.OnInkMuted, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(Spacing.lg))

        PasscodeDots(filled = passcode.length)

        Spacer(Modifier.height(Spacing.md))
        Text(
            "Simulation: any 6 digits will work",
            color = BrandColors.OnInkMuted,
            style = MaterialTheme.typography.bodySmall,
        )

        Spacer(Modifier.weight(1f))

        Keypad(
            onDigit = { if (passcode.length < PASSCODE_LENGTH) passcode += it },
            onDelete = { passcode = passcode.dropLast(1) },
            onBiometric = onSignedIn,
        )
        Spacer(Modifier.height(Spacing.lg))
    }
}

@Composable
private fun PasscodeDots(filled: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = Modifier.semantics { contentDescription = "$filled of $PASSCODE_LENGTH digits entered" },
    ) {
        repeat(PASSCODE_LENGTH) { index ->
            val color by animateColorAsState(
                if (index < filled) BrandColors.Mint else BrandColors.Mint.copy(alpha = 0f),
                label = "dot",
            )
            Box(
                Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(2.dp, BrandColors.Mint, CircleShape),
            )
        }
    }
}

@Composable
private fun Keypad(
    onDigit: (Char) -> Unit,
    onDelete: () -> Unit,
    onBiometric: () -> Unit,
) {
    val rows = listOf("123", "456", "789")
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.lg)) {
                row.forEach { digit -> KeypadKey(onClick = { onDigit(digit) }) { KeyLabel("$digit") } }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.lg)) {
            KeypadKey(onClick = onBiometric, filled = false) { KeyIcon(R.drawable.ic_fingerprint, "Use fingerprint") }
            KeypadKey(onClick = { onDigit('0') }) { KeyLabel("0") }
            KeypadKey(onClick = onDelete, filled = false) { KeyIcon(R.drawable.ic_backspace, "Delete") }
        }
    }
}

@Composable
private fun KeypadKey(
    onClick: () -> Unit,
    filled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (filled) BrandColors.OnInk.copy(alpha = 0.08f) else BrandColors.OnInk.copy(alpha = 0f),
        contentColor = BrandColors.OnInk,
        modifier = Modifier
            .size(76.dp)
            .aspectRatio(1f),
    ) {
        Box(contentAlignment = Alignment.Center) { content() }
    }
}

@Composable
private fun KeyLabel(text: String) {
    Text(text, style = MaterialTheme.typography.headlineMedium)
}

@Composable
private fun KeyIcon(@DrawableRes icon: Int, label: String) {
    Icon(painterResource(icon), contentDescription = label, modifier = Modifier.size(28.dp))
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview() {
    LendFlowTheme {
        SignInScreen(onBack = {}, onSignedIn = {})
    }
}
