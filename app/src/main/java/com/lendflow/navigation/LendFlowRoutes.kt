package com.lendflow.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Each destination is a type. @Serializable lets rememberNavBackStack save the whole
// back stack into the saved-instance-state Bundle, so it survives process death.
// Destinations that need arguments are data classes.

/** Signed-out landing carousel. The back stack starts here. */
@Serializable
data object WelcomeRoute : NavKey

/** Simulated passcode sign-in. */
@Serializable
data object SignInRoute : NavKey

/** Signed-in shell with the bottom navigation bar; the selected tab is state inside it. */
@Serializable
data object MainRoute : NavKey

@Serializable
data class LoanDetailRoute(val loanId: String) : NavKey

@Serializable
data object CalculatorRoute : NavKey

@Serializable
data class ApplyRoute(val amount: Int, val termMonths: Int) : NavKey

@Serializable
data class MakePaymentRoute(val loanId: String) : NavKey

@Serializable
data object NotificationsRoute : NavKey
