package com.lendflow.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// Brand palette: deep indigo "ink" for headers and trust, a vivid indigo for actions,
// mint for positive moments and primary CTAs on dark surfaces, coral reserved for problems.
// Only the roles we deliberately override are listed; Material 3 derives the rest.

internal val Ink950 = Color(0xFF0C0A24)
internal val Ink900 = Color(0xFF16123F)
internal val Ink800 = Color(0xFF221B5E)
internal val Ink700 = Color(0xFF30268A)

internal val Indigo600 = Color(0xFF4B3BFF)
internal val Indigo300 = Color(0xFFA9A0FF)
internal val Indigo100 = Color(0xFFE7E4FF)
internal val Indigo900 = Color(0xFF130A66)

internal val Mint400 = Color(0xFF2EE6B0)
internal val Mint100 = Color(0xFFC9F7E8)
internal val Mint700 = Color(0xFF00805A)
internal val Mint900 = Color(0xFF00382A)

internal val Coral600 = Color(0xFFD93A45)
internal val Coral300 = Color(0xFFFF9EA3)
internal val Amber600 = Color(0xFFB07700)
internal val Amber300 = Color(0xFFF2C14E)

internal val Neutral0 = Color(0xFFFFFFFF)
internal val Neutral50 = Color(0xFFF6F5FB)
internal val Neutral100 = Color(0xFFECEAF4)
internal val Neutral200 = Color(0xFFDDDAE8)
internal val Neutral500 = Color(0xFF6E6A85)
internal val Neutral900 = Color(0xFF15132B)
internal val NeutralDark100 = Color(0xFFE6E3F5)
internal val NeutralDark400 = Color(0xFFA19DBA)
internal val NeutralDarkSurface = Color(0xFF1A1738)
internal val NeutralDarkSurfaceHigh = Color(0xFF242046)

/**
 * Colours that sit outside the Material roles: the ink header shown on every screen in both
 * themes, and the mint accent used for CTAs on top of it.
 */
object BrandColors {
    val Ink = Ink900
    val InkDeep = Ink950
    val InkRaised = Ink800
    val InkGlow = Ink700
    val OnInk = Neutral0
    val OnInkMuted = Color(0xB3FFFFFF)
    val Mint = Mint400
    val OnMint = Mint900
    val Positive = Mint700
    val Warning = Amber600
}
