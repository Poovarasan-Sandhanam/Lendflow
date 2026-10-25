package com.lendflow.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val base = Typography()

/** Tabular figures keep digits the same width, so amounts in columns line up. */
private fun TextStyle.tabular() = copy(fontFeatureSettings = "tnum")

// Big, heavy headlines with slightly tight tracking give amounts and titles a confident voice.
internal val LendFlowTypography = base.copy(
    displayMedium = base.displayMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = (-1).sp).tabular(),
    displaySmall = base.displaySmall.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp).tabular(),
    headlineLarge = base.headlineLarge.copy(fontWeight = FontWeight.Bold).tabular(),
    headlineMedium = base.headlineMedium.copy(fontWeight = FontWeight.Bold).tabular(),
    headlineSmall = base.headlineSmall.copy(fontWeight = FontWeight.Bold).tabular(),
    titleLarge = base.titleLarge.copy(fontWeight = FontWeight.Bold).tabular(),
    titleMedium = base.titleMedium.copy(fontWeight = FontWeight.SemiBold).tabular(),
    titleSmall = base.titleSmall.copy(fontWeight = FontWeight.SemiBold),
    labelLarge = base.labelLarge.copy(fontWeight = FontWeight.SemiBold),
)
