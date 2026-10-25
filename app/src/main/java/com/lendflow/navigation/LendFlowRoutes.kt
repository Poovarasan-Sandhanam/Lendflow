package com.lendflow.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Each destination is a type. @Serializable lets rememberNavBackStack save the whole
// back stack into the saved-instance-state Bundle, so it survives process death.
// Destinations that need arguments become data classes (e.g. LoanDetailRoute(val loanId: String)).

@Serializable
data object DashboardRoute : NavKey

@Serializable
data object CalculatorRoute : NavKey
