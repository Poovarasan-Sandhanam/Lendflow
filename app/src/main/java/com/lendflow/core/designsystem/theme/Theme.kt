package com.lendflow.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Indigo600,
    onPrimary = Neutral0,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo900,
    secondary = Mint700,
    onSecondary = Neutral0,
    secondaryContainer = Mint100,
    onSecondaryContainer = Mint900,
    tertiary = Amber600,
    error = Coral600,
    background = Neutral50,
    onBackground = Neutral900,
    surface = Neutral50,
    onSurface = Neutral900,
    onSurfaceVariant = Neutral500,
    surfaceContainerLowest = Neutral0,
    surfaceContainerLow = Neutral0,
    surfaceContainer = Neutral100,
    surfaceContainerHigh = Neutral100,
    outlineVariant = Neutral200,
)

private val DarkColors = darkColorScheme(
    primary = Indigo300,
    onPrimary = Indigo900,
    primaryContainer = Ink800,
    onPrimaryContainer = Indigo100,
    secondary = Mint400,
    onSecondary = Mint900,
    secondaryContainer = Mint900,
    onSecondaryContainer = Mint100,
    tertiary = Amber300,
    error = Coral300,
    background = Ink950,
    onBackground = NeutralDark100,
    surface = Ink950,
    onSurface = NeutralDark100,
    onSurfaceVariant = NeutralDark400,
    surfaceContainerLowest = NeutralDarkSurface,
    surfaceContainerLow = NeutralDarkSurface,
    surfaceContainer = NeutralDarkSurfaceHigh,
    surfaceContainerHigh = NeutralDarkSurfaceHigh,
    outlineVariant = NeutralDarkSurfaceHigh,
)

// Generous radii give the soft, friendly card look; buttons use CircleShape (pills) directly.
private val LendFlowShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
)

/**
 * LendFlow's Material 3 theme.
 *
 * Dynamic color (Material You) is intentionally not used: a financial brand should look the same
 * on every device so users can recognise genuine app screens.
 */
@Composable
fun LendFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = LendFlowTypography,
        shapes = LendFlowShapes,
        content = content,
    )
}
