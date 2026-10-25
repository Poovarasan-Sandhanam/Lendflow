package com.lendflow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun signIn() {
        composeRule.onNodeWithText("Get started").performClick()
        repeat(6) { composeRule.onNodeWithText("1").performClick() }
        composeRule.onNodeWithText("Total left to repay").assertExists()
    }

    @Test
    fun launchesOnWelcome() {
        composeRule.onNodeWithText("Get started").assertExists()
    }

    @Test
    fun passcodeSignsInToHome() {
        signIn()
    }

    @Test
    fun opensCalculatorAndNavigatesBack() {
        signIn()
        composeRule.onNodeWithText("Calculator").performClick()
        composeRule.onNodeWithText("Loan calculator").assertExists()

        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.onNodeWithText("Total left to repay").assertExists()
    }

    @Test
    fun bottomBarSwitchesTabsAndBackReturnsHome() {
        signIn()
        composeRule.onNodeWithText("Activity").performClick()
        composeRule.onNodeWithText("Repayments").assertExists()

        composeRule.runOnUiThread { composeRule.activity.onBackPressedDispatcher.onBackPressed() }
        composeRule.onNodeWithText("Total left to repay").assertExists()
    }

    @Test
    fun backStackSurvivesActivityRecreation() {
        signIn()
        composeRule.onNodeWithText("Calculator").performClick()

        composeRule.activityRule.scenario.recreate()

        composeRule.onNodeWithText("Loan calculator").assertExists()
    }
}
