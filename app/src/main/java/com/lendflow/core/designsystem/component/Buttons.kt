package com.lendflow.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lendflow.core.designsystem.theme.BrandColors

private val ButtonHeight = 56.dp

/**
 * Full-width pill button for the one main action on a screen.
 * [onInk] switches to the mint style used on top of the dark ink header.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onInk: Boolean = false,
) {
    val colors = if (onInk) {
        ButtonDefaults.buttonColors(
            containerColor = BrandColors.Mint,
            contentColor = BrandColors.OnMint,
            disabledContainerColor = BrandColors.Mint.copy(alpha = 0.3f),
            disabledContentColor = BrandColors.OnMint.copy(alpha = 0.6f),
        )
    } else {
        ButtonDefaults.buttonColors()
    }
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        colors = colors,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ButtonHeight),
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

/** Outlined pill for the secondary action next to a [PrimaryButton]. */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onInk: Boolean = false,
) {
    val contentColor = if (onInk) BrandColors.OnInk else MaterialTheme.colorScheme.primary
    OutlinedButton(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.5.dp, contentColor.copy(alpha = if (onInk) 0.5f else 1f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ButtonHeight),
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}
