package com.lendflow.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lendflow.feature.apply.ApplyScreen
import com.lendflow.feature.calculator.CalculatorScreen
import com.lendflow.feature.loans.LoanDetailScreen
import com.lendflow.feature.notifications.NotificationsScreen
import com.lendflow.feature.onboarding.SignInScreen
import com.lendflow.feature.onboarding.WelcomeScreen
import com.lendflow.feature.payment.MakePaymentScreen

private const val SLIDE_MILLIS = 350

// Push slides the new screen in from the end edge; pop reverses it. The covered screen
// drifts a third of the way, which reads as depth without a heavy parallax.
private val pushTransition: ContentTransform =
    slideInHorizontally(tween(SLIDE_MILLIS)) { it } togetherWith slideOutHorizontally(tween(SLIDE_MILLIS)) { -it / 3 }
private val popTransition: ContentTransform =
    slideInHorizontally(tween(SLIDE_MILLIS)) { -it / 3 } togetherWith slideOutHorizontally(tween(SLIDE_MILLIS)) { it }

/**
 * App navigation graph. The back stack is a plain observable list of [NavKey]s:
 * navigating forward is `add`, going back is `removeLastOrNull`.
 *
 * Screens receive navigation as lambdas and never see the back stack, which keeps them
 * reusable and previewable.
 */
@Composable
fun LendFlowNavDisplay(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(WelcomeRoute)
    val back: () -> Unit = { backStack.removeLastOrNull() }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = back,
        transitionSpec = { pushTransition },
        popTransitionSpec = { popTransition },
        predictivePopTransitionSpec = { popTransition },
        entryProvider = entryProvider {
            entry<WelcomeRoute> {
                WelcomeScreen(
                    onGetStarted = { backStack.add(SignInRoute) },
                    onLogIn = { backStack.add(SignInRoute) },
                )
            }
            entry<SignInRoute> {
                // Signing in replaces the whole stack, so Back from Home exits instead of signing out.
                SignInScreen(onBack = back, onSignedIn = { backStack.resetTo(MainRoute) })
            }
            entry<MainRoute> {
                MainShell(
                    onOpenLoan = { backStack.add(LoanDetailRoute(it)) },
                    onOpenCalculator = { backStack.add(CalculatorRoute) },
                    onMakePayment = { backStack.add(MakePaymentRoute(it)) },
                    onOpenNotifications = { backStack.add(NotificationsRoute) },
                    onSignOut = { backStack.resetTo(WelcomeRoute) },
                )
            }
            entry<LoanDetailRoute> { route ->
                LoanDetailScreen(
                    loanId = route.loanId,
                    onBack = back,
                    onMakePayment = { backStack.add(MakePaymentRoute(route.loanId)) },
                )
            }
            entry<CalculatorRoute> {
                CalculatorScreen(
                    onBack = back,
                    onApply = { amount, term -> backStack.add(ApplyRoute(amount, term)) },
                )
            }
            entry<ApplyRoute> { route ->
                ApplyScreen(
                    amount = route.amount,
                    termMonths = route.termMonths,
                    onClose = back,
                    onFinished = { backStack.popTo(MainRoute) },
                )
            }
            entry<MakePaymentRoute> { route ->
                MakePaymentScreen(loanId = route.loanId, onBack = back, onDone = back)
            }
            entry<NotificationsRoute> {
                NotificationsScreen(onBack = back)
            }
        },
    )
}

private fun MutableList<NavKey>.resetTo(route: NavKey) {
    clear()
    add(route)
}

/** Pops entries above [route]; leaves the stack alone if [route] isn't on it. */
private fun MutableList<NavKey>.popTo(route: NavKey) {
    val index = lastIndexOf(route)
    if (index == -1) return
    while (size > index + 1) removeAt(lastIndex)
}
