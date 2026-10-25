package com.lendflow.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Navy700,
    onPrimary = Color.White,
    primaryContainer = Navy100,
    onPrimaryContainer = Navy900,
    secondary = Teal600,
    secondaryContainer = Teal100,
    tertiary = Amber700,
    background = Neutral50,
    onBackground = Neutral900,
    surface = Neutral50,
    onSurface = Neutral900,
)

private val DarkColors = darkColorScheme(
    primary = Sky200,
    onPrimary = Navy900,
    primaryContainer = Navy800,
    onPrimaryContainer = Navy100,
    secondary = Teal300,
    secondaryContainer = Teal800,
    tertiary = Amber300,
    background = Neutral950,
    onBackground = Neutral100,
    surface = Neutral950,
    onSurface = Neutral100,
    surfaceContainer = NeutralSurfaceDark,
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
        content = content,
    )
}
