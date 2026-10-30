package com.lendflow.core.ui

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Formatting is fixed to en-GB (pounds, "25 Oct 2026") because the simulated product is UK-style.

private val currency = NumberFormat.getCurrencyInstance(Locale.UK)
private val wholeCurrency = NumberFormat.getCurrencyInstance(Locale.UK).apply { maximumFractionDigits = 0 }
private val shortDate = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.UK)
private val dayMonth = DateTimeFormatter.ofPattern("d MMM", Locale.UK)
private val monthYear = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.UK)

fun Double.asMoney(): String = currency.format(this)

fun Double.asWholeMoney(): String = wholeCurrency.format(this)

fun LocalDate.asShortDate(): String = format(shortDate)

fun LocalDate.asDayMonth(): String = format(dayMonth)

fun LocalDate.asMonthYear(): String = format(monthYear)
