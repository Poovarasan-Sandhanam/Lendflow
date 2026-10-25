package com.lendflow.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

private val base = Typography()

/** Tabular figures keep digits the same width, so amounts in columns line up. */
private fun TextStyle.tabular() = copy(fontFeatureSettings = "tnum")

internal val LendFlowTypography = base.copy(
    displaySmall = base.displaySmall.tabular(),
    headlineLarge = base.headlineLarge.tabular(),
    headlineMedium = base.headlineMedium.tabular(),
    headlineSmall = base.headlineSmall.tabular(),
    titleLarge = base.titleLarge.tabular(),
)
