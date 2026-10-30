package com.lendflow.core.model

import kotlin.math.pow

/**
 * Standard amortising-loan payment: P * r / (1 - (1 + r)^-n), with r the monthly rate.
 * Phase 1 moves this into a tested domain layer with exact (BigDecimal) arithmetic.
 */
fun monthlyRepayment(principal: Double, aprPercent: Double, termMonths: Int): Double {
    if (termMonths <= 0) return 0.0
    val r = aprPercent / 100 / 12
    if (r == 0.0) return principal / termMonths
    return principal * r / (1 - (1 + r).pow(-termMonths))
}

/** Simulated pricing tiers: larger loans get a lower representative APR. */
fun representativeApr(amount: Double): Double = when {
    amount < 3_000 -> 19.9
    amount < 7_500 -> 9.9
    amount < 15_000 -> 7.4
    else -> 6.9
}
