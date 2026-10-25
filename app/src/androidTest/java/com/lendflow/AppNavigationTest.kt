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

    @Test
    fun launchesOnDashboard() {
        composeRule.onNodeWithText("Welcome to LendFlow").assertExists()
    }

    @Test
    fun opensCalculatorAndNavigatesBack() {
        composeRule.onNodeWithText("Open loan calculator").performClick()
        composeRule.onNodeWithText("Loan calculator").assertExists()

        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.onNodeWithText("Welcome to LendFlow").assertExists()
    }

    @Test
    fun backStackSurvivesActivityRecreation() {
        composeRule.onNodeWithText("Open loan calculator").performClick()

        composeRule.activityRule.scenario.recreate()

        composeRule.onNodeWithText("Loan calculator").assertExists()
    }
}
